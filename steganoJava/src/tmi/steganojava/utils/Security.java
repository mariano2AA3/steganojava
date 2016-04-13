package tmi.steganojava.utils;

import java.io.File;
import java.io.IOException;

public interface Security {
	
	public void encrypt(File file, String password);
	
	public void decrypt(File file, String password) throws IOException;

}
