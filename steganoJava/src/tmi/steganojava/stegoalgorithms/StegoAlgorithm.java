package tmi.steganojava.stegoalgorithms;

import java.awt.image.BufferedImage;

public interface StegoAlgorithm {
	
	public void encode(BufferedImage img, char[] bytesFile);
	
	public char[] decode(BufferedImage secretImg);
	
	public float getImgEncodeSpace(BufferedImage img);

}