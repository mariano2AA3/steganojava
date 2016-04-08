import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class bmp {
	String text;
	BufferedImage image;
	int[] arrayTexto;
	
	public bmp(String text,String imgEntrada,String imgSalida) {
		this.text = text;
		this.text.concat("~");
		try {
			//seeBMPImage("foto.bmp");
			this.image = ImageIO.read(new File(imgEntrada));
			
			int numChars = this.image.getWidth() * this.image.getHeight() / 7;
			
			if(this.text.length()>numChars){
				System.out.println("Img pequeña");
				return;
			}
			
			this.arrayTexto = new int[(this.text.length()*7)];
			for(int i = 0; i < this.text.length(); i++){
				this.arrayTexto[i*7]     = (((int)(this.text.charAt(i))) & 0x1<<6)>>6;
				this.arrayTexto[(i*7)+1] = (((int)(this.text.charAt(i))) & 0x1<<5)>>5;
				this.arrayTexto[(i*7)+2] = (((int)(this.text.charAt(i))) & 0x1<<4)>>4;
				this.arrayTexto[(i*7)+3] = (((int)(this.text.charAt(i))) & 0x1<<3)>>3;
				this.arrayTexto[(i*7)+4] = (((int)(this.text.charAt(i))) & 0x1<<2)>>2;
				this.arrayTexto[(i*7)+5] = (((int)(this.text.charAt(i))) & 0x1<<1)>>1;
				this.arrayTexto[(i*7)+6] = (((int)(this.text.charAt(i))) & 0x1<<0)>>0;
			}
			
			
			writeIMG(imgSalida);
			
			System.out.println(readBMP(imgSalida));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeIMG(String imgSalida) throws IOException {
		boolean leave = false;
		int color,red,green,blue,pos;
		Color color2;
		for (int xPixel = 0; xPixel < this.image.getWidth() && !leave; xPixel++) {
			for (int yPixel = 0; yPixel < this.image.getHeight() && !leave; yPixel++) {
				color = this.image.getRGB(xPixel, yPixel);			
				red = (color >> 16) & 0xFF;
				green = (color >> 8) & 0xFF;
				blue = color & 0xFF;		
				pos = getPosition(xPixel,yPixel);
				
				if(pos<this.arrayTexto.length){
					red = red | (this.arrayTexto[pos] & 0x1<<0);
					color2 =  new Color(red, green,blue);
					this.image.setRGB(xPixel, yPixel, color2.getRGB());
				}else{
					leave = true;
				}	
			}
		}
		
		File outputfile = new File(imgSalida);
	    ImageIO.write(image, "bmp", outputfile);
	} 
	
	private String readBMP(String file) throws IOException{
		this.image = ImageIO.read(new File(file));
		String salidaString = "";
		int relleno = 0;
		int color,red,green,blue,pos,x;//mantener para posible codificacion en los otros colores
		String std;
		int[] charSeven = new int[7];
		for (int xPixel = 0; xPixel < this.image.getWidth(); xPixel++) {
			for (int yPixel = 0; yPixel < this.image.getHeight(); yPixel++) {
				color = this.image.getRGB(xPixel, yPixel);			
				red = (color >> 16) & 0xFF;
				//System.out.println(red);
				
				
				green = (color >> 8) & 0xFF;
				blue = color & 0xFF;		
				std = "";
				
				red  = red & 0x1<<0;
				
				
				x = 0;
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
		return x * this.image.getWidth() + y;
	}
}
