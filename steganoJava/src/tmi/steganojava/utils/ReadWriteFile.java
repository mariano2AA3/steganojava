package tmi.steganojava.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import tmi.steganojava.exceptions.ImgFormatException;

/**
 * This class handled read and write methods for being used by Controller.
 * 
 * @author Mariano Hernandez Garcia
 *
 */
public class ReadWriteFile {

	/**
	 * This function use ImageIO for reading an image and store it into a BufferedImage. Use only for reading IMAGES!
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws ImgFormatException
	 */
	public BufferedImage readImg(String path) throws IOException, ImgFormatException {
		
		BufferedImage img = null;
		
		img = ImageIO.read(new File(path));
		
		return img;
	}
	
	/**
	 * This function use Files.readAllBytes to create a byte array that contents all File bytes.
	 * IMPORTANT: Use only if File is small.
	 * @param path of file to read
	 * @return byte[] 
	 * @throws IOException
	 */
	public byte[] readSmallFile(String path) throws IOException{
		
		Path pPath = Paths.get(path);
		return Files.readAllBytes(pPath);
	}
	
	/**
	 * This function read a file and convert it to a byte array.
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public byte[] readFile(String path) throws IOException {
		
		File file					 	= new File(path);
		ByteArrayOutputStream baos		= new ByteArrayOutputStream();
		InputStream ios			        = new FileInputStream(file);
		byte[] buffer					= new byte[4096];
		int read						= 0;
		
		while ( (read = ios.read(buffer)) != -1 ) {
			baos.write(buffer, 0, read);
		}
		
		baos.close();
		ios.close();
		
		return baos.toByteArray();
		
	}
	
	/**
	 * This function creates a file on disk with the content of param bytes.
	 * @param path
	 * @param bytes
	 * @throws IOException 
	 */
	public void writeFile(String path, byte[] bytes) throws IOException {
		
		FileOutputStream out = new FileOutputStream(path);
		out.write(bytes);
		out.close();
		
	}


}
