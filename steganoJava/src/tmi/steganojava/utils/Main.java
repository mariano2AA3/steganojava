package tmi.steganojava.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.org.apache.bcel.internal.util.ByteSequence;

import sun.text.normalizer.UTF16;
import tmi.steganojava.exceptions.ImgFormatException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ReadWriteFile rwFile = new ReadWriteFile();
		BufferedImage bimg   = null;
		
		//  Leemos la imagen image.png como BufferedImage
		try {
			bimg = rwFile.readImg("image.png");
		} catch (IOException | ImgFormatException e) {
			e.printStackTrace();
		}
		
		// Convertimos el BufferedImage a byte[] y lo escribimos como image2.png
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(bimg, "png", out);
			out.flush();
			rwFile.writeFile("image2.png", out.toByteArray());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

}
