package tmi.steganojava.stegoalgorithms;

import java.awt.image.BufferedImage;

import javafx.util.Pair;

public interface StegoAlgorithm {
	
	public BufferedImage encode(BufferedImage img, byte[] bytesFile);
	
	public Pair<byte[], Character[]> decode(BufferedImage secretImg);
	
	public float getImgEncodeSpace(BufferedImage img);

}
