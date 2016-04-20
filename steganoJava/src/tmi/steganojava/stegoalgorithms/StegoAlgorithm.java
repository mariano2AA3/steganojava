package tmi.steganojava.stegoalgorithms;

import java.awt.image.BufferedImage;

import tmi.steganojava.utils.Pair;

public interface StegoAlgorithm {
	
	/**
	 * Hide a file into a image
	 * @param img: the image where we will encode the hidden file
	 * @param bytesFile: file that will be hide
	 * @param extension: the mime of the file 
	 * @return new image with the hidden file inside him
	 */
	public BufferedImage encode(BufferedImage img, byte[] bytesFile,char[] extension);
	
	/**
	 * Rebuild the hide file inside the image
	 * @param secretImg: the image with the hide file inside
	 * @return Pair<byte[],Character[]> = <The hide file, The mime of the file>
	 */
	public Pair<byte[], Character[]> decode(BufferedImage secretImg);
	
	/**
	 * Look into the image all the space we have for hide a file
	 * @param img: the image where we want to hide a file
	 * @return the space in MB for the file
	 */
	public float getImgEncodeSpace(BufferedImage img);

}
