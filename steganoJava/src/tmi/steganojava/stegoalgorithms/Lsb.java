package tmi.steganojava.stegoalgorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import sun.rmi.runtime.Log;
import sun.util.logging.resources.logging;
import tmi.steganojava.utils.Pair;

public class Lsb implements StegoAlgorithm {
	
	
	@Override
	public BufferedImage encode(BufferedImage img, byte[] fileBytes,char[] extension) {	
		
		/*/////
		String ext = "bmp ";	//4 de longitud siempre
		char[] extension = new char[ext.length()];
		for(int i = 0; i < ext.length(); i++){
			extension[i] = ext.charAt(i);
		}
		/////*/
		
		
		int input = fileBytes.length+4;
		boolean[] bits = new boolean[24+(input*8)];
	    for (int i = 23; i >= 0; i--) {
	        bits[i] = (input & (1 << i)) != 0;
	    }
	    
	    for(int i = 0; i < fileBytes.length; i++){
	    	bits[i*8+24]     = (((fileBytes[i])) & 0x1<<7)>>7 != 0;
	    	bits[(i*8)+1+24] = (((fileBytes[i])) & 0x1<<6)>>6 != 0;
	    	bits[(i*8)+2+24] = (((fileBytes[i])) & 0x1<<5)>>5 != 0;
	    	bits[(i*8)+3+24] = (((fileBytes[i])) & 0x1<<4)>>4 != 0;
	    	bits[(i*8)+4+24] = (((fileBytes[i])) & 0x1<<3)>>3 != 0;
	    	bits[(i*8)+5+24] = (((fileBytes[i])) & 0x1<<2)>>2 != 0;
	    	bits[(i*8)+6+24] = (((fileBytes[i])) & 0x1<<1)>>1 != 0;
			bits[(i*8)+7+24] = (((fileBytes[i])) & 0x1<<0)>>0 != 0;
		}
	    
	    for(int i = 0; i < 4; i++){
	    	bits[((fileBytes.length+i)*8)+24]   = (((extension[i])) & 0x1<<7)>>7 != 0;
	    	bits[((fileBytes.length+i)*8)+24+1] = (((extension[i])) & 0x1<<6)>>6 != 0;
	    	bits[((fileBytes.length+i)*8)+24+2] = (((extension[i])) & 0x1<<5)>>5 != 0;
	    	bits[((fileBytes.length+i)*8)+24+3] = (((extension[i])) & 0x1<<4)>>4 != 0;
	    	bits[((fileBytes.length+i)*8)+24+4] = (((extension[i])) & 0x1<<3)>>3 != 0;
	    	bits[((fileBytes.length+i)*8)+24+5] = (((extension[i])) & 0x1<<2)>>2 != 0;
	    	bits[((fileBytes.length+i)*8)+24+6] = (((extension[i])) & 0x1<<1)>>1 != 0;
	    	bits[((fileBytes.length+i)*8)+24+7] = (((extension[i])) & 0x1<<0)>>0 != 0;
	    }
	    
		boolean leave = false;
		int color,red,green,blue,pos;
		Color color2;
		for (int xPixel = 0; xPixel < img.getWidth() && !leave; xPixel++) {
			for (int yPixel = 0; yPixel < img.getHeight() && !leave; yPixel++) {
				color = img.getRGB(xPixel, yPixel);			
				red = (color >> 16) & 0xFF;
				green = (color >> 8) & 0xFF;
				blue = color & 0xFF;	
				pos = (xPixel * img.getWidth()) + yPixel;
				
				if(pos<bits.length){
					red = red | (((bits[pos]) ? 1 : 0) & 0x1<<0);
					color2 =  new Color(red, green,blue);
					img.setRGB(xPixel, yPixel, color2.getRGB());
				}else{
					leave = true;
				}	
			}
		}
	    return img;
	}
	
	@Override
	public Pair<byte[], Character[]> decode(BufferedImage secretImg) {	
		int color,red,green,blue;//mantener para posible codificacion en los otros colores	
		
		// Here we get the size of the hidden file (boolean bits[])
		int count = 0;
		boolean[] bits = new boolean[24];
		int xPixel=0,yPixel=0;
		while(xPixel < secretImg.getWidth() && count < 24){
			while(yPixel < secretImg.getHeight() && count < 24){
				color = secretImg.getRGB(xPixel, yPixel);			
				red = (color >> 16) & 0xFF;
				bits[count] = (red & 0x1<<0) != 0;
				count++;
				yPixel++;
			}
			if(count!=24){
				xPixel++;
			}
		}
		
		// Now we transform the boolean array into a integer
		int n = 0;
		for (int i = bits.length-1; i >= 0; --i) {
		    n = (n << 1) + (bits[i] ? 1 : 0);
		}	

		// And then we start to read from 0 to hiddenFile.size into a byte[] array
		count = 0;
		int top = n;
		int relleno = 0;
		bits = new boolean[8];
		byte[] byteExit = new byte[top];
		byte b;
		while(xPixel < secretImg.getWidth() && count < top){
			while(yPixel < secretImg.getHeight() && count < top){
				color = secretImg.getRGB(xPixel, yPixel);			
				red = (color >> 16) & 0xFF;
						
				green = (color >> 8) & 0xFF;
				blue = color & 0xFF;		
				
				red  = red & 0x1<<0;
				
				bits[relleno]=(red != 0);
				relleno++;
				if(relleno == 8){
					b = (byte)((bits[0]?1<<7:0) + (bits[1]?1<<6:0) + (bits[2]?1<<5:0) + 
			                   (bits[3]?1<<4:0) + (bits[4]?1<<3:0) + (bits[5]?1<<2:0) + 
			                   (bits[6]?1<<1:0) + (bits[7]?1:0));	
					byteExit[count] = b;
					count++;
				}				
				relleno = (relleno) % 8;
				
				yPixel++;
			}
			xPixel++;
		}	

		Character[] salida = new Character[4];
		if(byteExit[byteExit.length-4]!=0)salida[0] = (char)byteExit[byteExit.length-4];
		if(byteExit[byteExit.length-3]!=0)salida[1] = (char)byteExit[byteExit.length-3];
		if(byteExit[byteExit.length-2]!=0)salida[2] = (char)byteExit[byteExit.length-2];
		if(byteExit[byteExit.length-1]!=0)salida[3] = (char)byteExit[byteExit.length-1];
		byte[] newArray = Arrays.copyOfRange(byteExit, 0, byteExit.length-4);		
		return new Pair<byte[], Character[]>(newArray, salida);
	}

	@Override
	public float getImgEncodeSpace(BufferedImage img) {
		return ((((img.getHeight() * img.getWidth())-56 / 8.0f) /1024.0f) /1024.0f);
	}

}
