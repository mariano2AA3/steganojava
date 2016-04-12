package tmi.steganojava.stegoalgorithms;

import java.awt.image.BufferedImage;

public interface StegoAlgorithm {
	
	public BufferedImage encode(BufferedImage img, byte[] bytesFile);
	
	public char[] decode(BufferedImage secretImg);
	
	public float getImgEncodeSpace(BufferedImage img);

}
