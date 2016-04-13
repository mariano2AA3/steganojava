package tmi.steganojava.stegoalgorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Lsb implements StegoAlgorithm {
	
	
	@Override
	// Encode no escribe en disco!!
	// Recibe una imagen (BufferedImage) y unos datos a ocultar (byte[]) y devuelve un BufferedImage con esos datos dentro
	public BufferedImage encode(BufferedImage img, byte[] fileBytes) {
		int[] arrayText = new int[(fileBytes.length*8)];
		for(int i = 0; i < fileBytes.length; i++){
			arrayText[i*7]     = (((int)(fileBytes[i])) & 0x1<<7)>>7;
			arrayText[(i*7)+1] = (((int)(fileBytes[i])) & 0x1<<6)>>6;
			arrayText[(i*7)+2] = (((int)(fileBytes[i])) & 0x1<<5)>>5;
			arrayText[(i*7)+3] = (((int)(fileBytes[i])) & 0x1<<4)>>4;
			arrayText[(i*7)+4] = (((int)(fileBytes[i])) & 0x1<<3)>>3;
			arrayText[(i*7)+5] = (((int)(fileBytes[i])) & 0x1<<2)>>2;
			arrayText[(i*7)+6] = (((int)(fileBytes[i])) & 0x1<<1)>>1;
			arrayText[(i*7)+7] = (((int)(fileBytes[i])) & 0x1<<0)>>0;
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
				
				if(pos<arrayText.length){
					red = red | (arrayText[pos] & 0x1<<0);
					color2 =  new Color(red, green,blue);
					img.setRGB(xPixel, yPixel, color2.getRGB());
				}else{
					leave = true;
				}	
			}
		}
		
		File outputfile = new File("salida.bmp");
	    try {
			ImageIO.write(img, "bmp", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    // Cambia esto por lo que sea!!
	    return img;
	}

	@Override
	public char[] decode(BufferedImage secretImg) {
		char[] salidaString = new char[secretImg.getHeight()*secretImg.getWidth()/8];
		int relleno = 0;
		int avance = 0;
		int color,red,green,blue,pos,x;//mantener para posible codificacion en los otros colores
		int[] charSeven = new int[8];
		for (int xPixel = 0; xPixel < secretImg.getWidth(); xPixel++) {
			for (int yPixel = 0; yPixel < secretImg.getHeight(); yPixel++) {
				color = secretImg.getRGB(xPixel, yPixel);			
				red = (color >> 16) & 0xFF;
				//System.out.println(red);
				
				
				green = (color >> 8) & 0xFF;
				blue = color & 0xFF;		
				
				red  = red & 0x1<<0;
				
				
				x = 0;
				charSeven[relleno]=red;
				relleno++;
				if(relleno == 8){
					x = charSeven[0]<<7 | charSeven[1]<<6 | charSeven[2]<<5 | charSeven[3]<<4 | charSeven[4]<<3 | charSeven[5]<<2 | charSeven[6]<<1 | charSeven[7]<<0;
					salidaString[avance]=(char)x;
					avance++;
				}
				
				if(x==126){
					return salidaString;
				}
				
				relleno = (relleno) % 7;				
					
			}
		}
		return salidaString;
	}

	@Override
	public float getImgEncodeSpace(BufferedImage img) {
		return img.getHeight() * img.getWidth() / 8.0f;
	}

}
