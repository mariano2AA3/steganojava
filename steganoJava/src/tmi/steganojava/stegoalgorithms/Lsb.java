package tmi.steganojava.stegoalgorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import tmi.steganojava.utils.Pair;

public class Lsb implements StegoAlgorithm {
	private int fileSize = 24;								// 24 bits for encode the size of the fileBytes
	private int byteSize = 8;								// 1 Byte = 8 bits
	private int mimeSize = 4;								// Mime size = 4 chars, 4 bytes
	
	@Override
	public BufferedImage encode(BufferedImage img, byte[] fileBytes,char[] extension) {	
		
		int input = fileBytes.length+mimeSize;				// File size + Mime (for example: jpg, txt, docx...)
		int bitsSize = fileSize+(input*byteSize);			// For encode: 	1 - The file size   = 24 bits		
		boolean[] bits = new boolean[bitsSize];				//              2 - The file + mime = input * 8 bits	
																					
		// Now we represent the file size of the fileBytes  + mime size in 24 bits
		for(int i = fileSize-1; i >= 0; i--){
			bits[i] = (input & (1 << i)) != 0;
		}
		
		// Then we transcript the fileBytes into bits
		for(int i = 0; i < fileBytes.length; i++){
			for(int j = 0; j < byteSize; j++){
		    	bits[(i*byteSize)+j+fileSize] = (((fileBytes[i])) & 0x1<<((byteSize-1)-j))>>((byteSize-1)-j) != 0;
			}
		}

		// After that we do the same with the mime type of the file
		for(int i = 0; i < mimeSize; i++){					// We only encode mimes of 4 chars (.xxxx)
			for(int j = 0; j < byteSize; j++){
		    	bits[((fileBytes.length+i)*byteSize)+fileSize+j] = (((extension[i])) & 0x1<<((byteSize-1)-j))>>((byteSize-1)-j) != 0;
			}
		}
		
		// Finally we encode all the bits array into the image. For that we change the least significant bit
		// of the 3 components (Red, Green and blue) of the pixel.
		// For encode a byte (8bits) we use 3 pixels (3 * 3 = 9 bits). The final bit in the third pixel isn't used.
		// P1.Red    P1.Green    P1.Blue    P2.Red    P2.Green    P2.Blue    P3.Red    P3.Green    P3.Blue
		//   X          X          X          X          X          X          X          X       Isn's used
		int color,red,green,blue;
		int bitsIterator = 0;
		for(int xPixel = 0; xPixel < img.getWidth() && bitsIterator < bitsSize; xPixel++){
			for(int yPixel = 0; yPixel < img.getHeight() && bitsIterator < bitsSize; yPixel++){
				color = img.getRGB(xPixel, yPixel);			
				
				red   = (color >> 16) & 0xFF;
				green = (color >> 8)  & 0xFF;
				blue  = color         & 0xFF;
							
				red =      (red   & ~(0x1<<0)) | (((bits[bitsIterator]) ? 1 : 0) & 0x1<<0);
				bitsIterator++;

				green =    (green & ~(0x1<<0)) | (((bits[bitsIterator]) ? 1 : 0) & 0x1<<0);
				bitsIterator++;

				if((bitsIterator%byteSize)!=0){				// The Blue component of the third pixel isn't used.
					blue = (blue  & ~(0x1<<0)) | (((bits[bitsIterator]) ? 1 : 0) & 0x1<<0);
					bitsIterator++;
				}
				
				img.setRGB(xPixel, yPixel, new Color(red, green, blue).getRGB());	
			}
		}
		
	    return img;
	}
	
	@Override
	public Pair<byte[], Character[]> decode(BufferedImage secretImg) {	
		int color,red,green,blue;
		
		// Here we get the size of the hidden file (boolean bits[])		
		int xPixel=0,yPixel=0;
		int bitsIterator = 0;
		boolean[] bits = new boolean[fileSize];
		while(xPixel < secretImg.getWidth() && bitsIterator < fileSize){
			while(yPixel < secretImg.getHeight() && bitsIterator < fileSize){
				color     = secretImg.getRGB(xPixel, yPixel);			
				
				red       = (color >> 16) & 0xFF;
				green     = (color >> 8)  & 0xFF;
				blue      = color         & 0xFF;
				
				bits[bitsIterator] =     (red   & 0x1<<0) != 0;
				bitsIterator++;
				bits[bitsIterator] =     (green & 0x1<<0) != 0;
				bitsIterator++;
				
				if((bitsIterator%byteSize)!=0){				// The Blue component of the third pixel isn't used.
					bits[bitsIterator] = (blue  & 0x1<<0) != 0;
					bitsIterator++;
				}

				yPixel++;	
			}
			if(bitsIterator!=fileSize){
				xPixel++;
			}
		}
		
		
		// Now we transform the boolean array into a integer
		int fileBytesSize = 0;
		for(int i = fileSize-1; i >= 0; i--){
			fileBytesSize = (fileBytesSize << 1) + (bits[i] ? 1 : 0);
		}

		// And then we start to read from 0 to hiddenFile.size+mimeSize into a byte[] array
		byte b;	
		bitsIterator = 0;
		int readedBytes = 0;
		bits = new boolean[byteSize];
		byte[] byteExit = new byte[fileBytesSize];		
		while(xPixel < secretImg.getWidth() && readedBytes < fileBytesSize){
			while(yPixel < secretImg.getHeight() && readedBytes < fileBytesSize){
				color     = secretImg.getRGB(xPixel, yPixel);			
				
				red       = (color >> 16) & 0xFF;
				green     = (color >> 8)  & 0xFF;
				blue      = color         & 0xFF;
				
				bits[bitsIterator] =     (red   & 0x1<<0) != 0;
				bitsIterator++;			
				bits[bitsIterator] =     (green & 0x1<<0) != 0;
				bitsIterator++;
				
				if((bitsIterator%byteSize)!=0){				// The Blue component of the third pixel isn't used.
					bits[bitsIterator] = (blue  & 0x1<<0) != 0;
					bitsIterator++;
				}else{
					// When we have 8 bits we should create the byte:
					b = (byte)((bits[0]?1<<7:0) + (bits[1]?1<<6:0) + (bits[2]?1<<5:0) + (bits[3]?1<<4:0) + 
							   (bits[4]?1<<3:0) + (bits[5]?1<<2:0) + (bits[6]?1<<1:0) + (bits[7]?1<<0:0)   );		
					byteExit[readedBytes] = b;
					readedBytes++;
					bitsIterator = 0;
				}
				
				////////
				yPixel++;
			}
			xPixel++;
			yPixel = 0;
		}	

		// Here we get the mime of the hidden file
		Character[] exit = new Character[mimeSize];
		for(int i = 1; i <= mimeSize; i++){
			if(byteExit[fileBytesSize-i]!=0)exit[mimeSize-i] = (char)byteExit[fileBytesSize-i];
		}
		
		// And now we remove the 4 last positions of the byte[] array (the mime) 	
		return new Pair<byte[], Character[]>(Arrays.copyOfRange(byteExit, 0, fileBytesSize-mimeSize), exit);
	}

	@Override
	public float getImgEncodeSpace(BufferedImage img) {
		// The space for the hidden file in the image is:
		// (Height * Width * 2) = Number of bits we can use (with 3 bits)
		// + (Height * Width * 2/3) = Number of bits we can use (with 2 bits)
		// -56 = 24 bits for the file size + 32 bits for the mime (4 char)
		// / 8.0f    = Bytes
		// / 1024.0f = KBytes
		float availableSpace = ((((((img.getHeight() * img.getWidth()) * 2) + (((img.getHeight() * img.getWidth()) / 3)*2)) - 56) / 8.0f) /1024.0f);
		return (availableSpace < 0.0)?0:availableSpace;
	}

}
