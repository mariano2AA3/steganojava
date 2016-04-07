import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class bmp {
	String texto = "Alegria y alboroto, perrito piloto~";
	int[][] array2D;
	BufferedImage image;
	int[] arrayTexto;
	public bmp() {
		try {
			//seeBMPImage("foto.bmp");
			image = ImageIO.read(new File("foto.bmp"));
			
			int numChars = image.getWidth() * image.getHeight() / 7;
			
			if(texto.length()>numChars){
				System.out.println("Img pequeña");
				return;
			}
			
			arrayTexto = new int[(texto.length()*7)];
			for(int i = 0; i < texto.length(); i++){
				arrayTexto[i*7]     = (((int)(texto.charAt(i))) & 0x1<<6)>>6;
				arrayTexto[(i*7)+1] = (((int)(texto.charAt(i))) & 0x1<<5)>>5;
				arrayTexto[(i*7)+2] = (((int)(texto.charAt(i))) & 0x1<<4)>>4;
				arrayTexto[(i*7)+3] = (((int)(texto.charAt(i))) & 0x1<<3)>>3;
				arrayTexto[(i*7)+4] = (((int)(texto.charAt(i))) & 0x1<<2)>>2;
				arrayTexto[(i*7)+5] = (((int)(texto.charAt(i))) & 0x1<<1)>>1;
				arrayTexto[(i*7)+6] = (((int)(texto.charAt(i))) & 0x1<<0)>>0;
			}
			
			
			writeIMG();
			
			System.out.println(readBMP("saved.bmp"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeIMG() throws IOException {
		boolean leave = false;
		//array2D = new int[image.getWidth()][image.getHeight()];
		for (int xPixel = 0; xPixel < image.getWidth() && !leave; xPixel++) {
			for (int yPixel = 0; yPixel < image.getHeight() && !leave; yPixel++) {
				int color = image.getRGB(xPixel, yPixel);			
				int red = (color >> 16) & 0xFF;
				int green = (color >> 8) & 0xFF;
				int blue = color & 0xFF;		
				int pos = getPosition(xPixel,yPixel);
				
				if(pos<(texto.length()*7)){
					red = red | (arrayTexto[pos] & 0x1<<0);
					Color color2 =  new Color(red, green,blue);
					image.setRGB(xPixel, yPixel, color2.getRGB());
				}else{
					leave = true;
				}	
			}
		}
		
		File outputfile = new File("saved.bmp");
	    ImageIO.write(image, "bmp", outputfile);
	} 
	
	private String readBMP(String file) throws IOException{
		image = ImageIO.read(new File(file));
		String salidaString = "";
		int relleno = 0;
		int[] charSeven = new int[7];
		for (int xPixel = 0; xPixel < image.getWidth(); xPixel++) {
			for (int yPixel = 0; yPixel < image.getHeight(); yPixel++) {
				int color = image.getRGB(xPixel, yPixel);			
				int red = (color >> 16) & 0xFF;
				int green = (color >> 8) & 0xFF;
				int blue = color & 0xFF;		
				String std = "";
				red  = red & 0x1<<0;
				int x = 0;
				charSeven[relleno]=red;
				relleno++;
				if(relleno == 7){
					x = charSeven[0]<<6 | charSeven[1]<<5 | charSeven[2]<<4 | charSeven[3]<<3 | charSeven[4]<<2 | charSeven[5]<<1 | charSeven[6]<<0;
					std = "" + (char)x;
				}
				
				if(x==126){
					return salidaString;
				}
				
				relleno = (relleno) % 7;
				
				
				
				salidaString = salidaString.concat(std);
					
			}
		}
		return salidaString;
	}
	
	private int getPosition(int x, int y){
		return x * image.getWidth() + y;
	}
}
