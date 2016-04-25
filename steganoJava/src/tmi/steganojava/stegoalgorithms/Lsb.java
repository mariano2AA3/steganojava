package tmi.steganojava.stegoalgorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import tmi.steganojava.utils.Pair;

public class Lsb implements StegoAlgorithm {
	
	
	@Override
	public BufferedImage encode(BufferedImage img, byte[] fileBytes,char[] extension) {	
		int input = fileBytes.length+4;						// File size + Mime (4 char, for example: jpg, txt, docx)
		int i = 0;											// Count
		boolean[] bits = new boolean[24+(input*8)];			// For encode: 	1 - The file size   = 24 bits
															//				2 - The file + mime = input * 8 bits
		
		// Now we represent the file size in 24 bits
		i = 23;
		while(i >= 0){
			bits[i] = (input & (1 << i)) != 0;
			i--;
		}
	    
		// Then we transcript the file in bytes into bits
		i = 0;
		while(i < fileBytes.length){
	    	bits[i*8+24]     = (((fileBytes[i])) & 0x1<<7)>>7 != 0;
	    	bits[(i*8)+1+24] = (((fileBytes[i])) & 0x1<<6)>>6 != 0;
	    	bits[(i*8)+2+24] = (((fileBytes[i])) & 0x1<<5)>>5 != 0;
	    	bits[(i*8)+3+24] = (((fileBytes[i])) & 0x1<<4)>>4 != 0;
	    	bits[(i*8)+4+24] = (((fileBytes[i])) & 0x1<<3)>>3 != 0;
	    	bits[(i*8)+5+24] = (((fileBytes[i])) & 0x1<<2)>>2 != 0;
	    	bits[(i*8)+6+24] = (((fileBytes[i])) & 0x1<<1)>>1 != 0;
			bits[(i*8)+7+24] = (((fileBytes[i])) & 0x1<<0)>>0 != 0;
			i++;
	    }
	    
		// After that we do the same with the mime type of the file
		i = 0;
		while(i < 4){
	    	bits[((fileBytes.length+i)*8)+24]   = (((extension[i])) & 0x1<<7)>>7 != 0;
	    	bits[((fileBytes.length+i)*8)+24+1] = (((extension[i])) & 0x1<<6)>>6 != 0;
	    	bits[((fileBytes.length+i)*8)+24+2] = (((extension[i])) & 0x1<<5)>>5 != 0;
	    	bits[((fileBytes.length+i)*8)+24+3] = (((extension[i])) & 0x1<<4)>>4 != 0;
	    	bits[((fileBytes.length+i)*8)+24+4] = (((extension[i])) & 0x1<<3)>>3 != 0;
	    	bits[((fileBytes.length+i)*8)+24+5] = (((extension[i])) & 0x1<<2)>>2 != 0;
	    	bits[((fileBytes.length+i)*8)+24+6] = (((extension[i])) & 0x1<<1)>>1 != 0;
	    	bits[((fileBytes.length+i)*8)+24+7] = (((extension[i])) & 0x1<<0)>>0 != 0;
	    	i++;
	    }
	    
		// Finally we encode all the bits array into the image. For that we change the least significant bit
		// of the 3 components (Red, Green and blue) of the pixel.
		// For encode a byte (8bits) we use 3 pixels (3 * 3 = 9 bits). The final bit in the third pixel isn't used.
		// P1.Red    P1.Green    P1.Blue    P2.Red    P2.Green    P2.Blue    P3.Red    P3.Green    P3.Blue
		//   X          X          X          X          X          X          X          X       Isn's used
		i = 0;
		boolean leave = false;
		int color,red,green,blue,count=0;
		Color color2;
		int xPixel = 0,yPixel = 0;
		while(xPixel < img.getWidth() && !leave){
			while(yPixel < img.getHeight() && !leave){
				color = img.getRGB(xPixel, yPixel);			
				
				red   = (color >> 16) & 0xFF;
				green = (color >> 8)  & 0xFF;
				blue  = color         & 0xFF;
				
				if(i<bits.length){
					red = red | (((bits[i]) ? 1 : 0) & 0x1<<0);
					count++;
					i++;
					green = green | (((bits[i]) ? 1 : 0) & 0x1<<0);
					i++;
					count++;
					if(count<8){
						blue = blue | (((bits[i]) ? 1 : 0) & 0x1<<0);
						i++;
						count++;
					}else{
						count = 0;
					}
					color2 =  new Color(red, green,blue);
					img.setRGB(xPixel, yPixel, color2.getRGB());
				}else{
					leave = true;
				}	
				yPixel++;
			}
			xPixel++;
		}
	    return img;
	}
	
	@Override
	public Pair<byte[], Character[]> decode(BufferedImage secretImg) {	
		int color,red,green,blue;
		
		// Here we get the size of the hidden file (boolean bits[])
		int pos=0,count = 0;
		boolean[] bits = new boolean[24];
		int xPixel=0,yPixel=0;
		while(xPixel < secretImg.getWidth() && pos < 24){
			while(yPixel < secretImg.getHeight() && pos < 24){
				color     = secretImg.getRGB(xPixel, yPixel);			
				
				red       = (color >> 16) & 0xFF;
				green     = (color >> 8)  & 0xFF;
				blue      = color         & 0xFF;
				
				bits[pos] = (red & 0x1<<0) != 0;
				pos++;
				count++;
				
				bits[pos] = (green & 0x1<<0) != 0;
				pos++;
				count++;
				
				if(count<8){
					bits[pos] = (blue & 0x1<<0) != 0;
					pos++;
					count++;
				}else{
					count = 0;
				}
				////////
				yPixel++;
			}
			if(pos!=24){
				xPixel++;
			}
		}
		
		// Now we transform the boolean array into a integer
		int n = 0;
		int i = bits.length-1;
		while(i >= 0){
			n = (n << 1) + (bits[i] ? 1 : 0);
			i--;
		}	

		// And then we start to read from 0 to hiddenFile.size into a byte[] array
		count = 0;
		i = 0;
		bits = new boolean[8];
		byte[] byteExit = new byte[n];
		byte b;
		while(xPixel < secretImg.getWidth() && count < n){
			while(yPixel < secretImg.getHeight() && count < n){
				color         = secretImg.getRGB(xPixel, yPixel);			
				
				red           = (color >> 16) & 0xFF;					
				green         = (color >> 8)  & 0xFF;
				blue          = color         & 0xFF;		
				
				red           = red           & 0x1<<0;
				green         = green         & 0x1<<0;
				blue          = blue          & 0x1<<0;
				
				bits[i] = (red != 0);
				i++;
				
				bits[i] = (green != 0);
				i++;
				
				if(i<8){
					bits[i]=(blue != 0);
					i++;
				}else{
					b = (byte)((bits[0]?1<<7:0) + (bits[1]?1<<6:0) + (bits[2]?1<<5:0) + (bits[3]?1<<4:0)   + 
							   (bits[4]?1<<3:0) + (bits[5]?1<<2:0) + (bits[6]?1<<1:0) + (bits[7]?1<<0:0)   );	
					byteExit[count] = b;
					count++;
				}				
				i = i % 8;
				
				////////
				yPixel++;
			}
			xPixel++;
		}	

		// Here we get the mime of the hidden file
		Character[] salida = new Character[4];
		if(byteExit[byteExit.length-4]!=0)salida[0] = (char)byteExit[byteExit.length-4];
		if(byteExit[byteExit.length-3]!=0)salida[1] = (char)byteExit[byteExit.length-3];
		if(byteExit[byteExit.length-2]!=0)salida[2] = (char)byteExit[byteExit.length-2];
		if(byteExit[byteExit.length-1]!=0)salida[3] = (char)byteExit[byteExit.length-1];
		
		// And now we remove the 4 last positions of the byte[] array (the mime) 
		byte[] newArray = Arrays.copyOfRange(byteExit, 0, byteExit.length-4);		
		return new Pair<byte[], Character[]>(newArray, salida);
	}

	@Override
	public float getImgEncodeSpace(BufferedImage img) {
		// The space for the hidden file in the image is:
		// (Height * Width * 3) = Number of bits we can use
		// -56 = 24 bits for the file size + 32 bits for the mime (4 char)
		// / 8.0f    = Bytes
		// / 1024.0f = KBytes
		float availableSpace = ((((img.getHeight() * img.getWidth() * 3) - 56) / 8.0f) /1024.0f);
		return (availableSpace < 0.0)?0:availableSpace;
	}

}
