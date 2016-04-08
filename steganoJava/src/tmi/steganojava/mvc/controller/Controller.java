package tmi.steganojava.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;


import tmi.steganojava.exceptions.ImgFormatException;

public interface Controller {
	
	public static ArrayList<String> imgFormatAdmitted = new ArrayList<String>();
		
	/**
	 * 
	 * @param path
	 * @return
	 * @throws ImgFormatException
	 */
	public boolean isImgFormatCorrect(String imgPath) throws ImgFormatException;
	
	
	/**
	 * 
	 * @param path
	 * @param alg
	 * @return
	 * @throws ImgFormatException
	 * @throws IOException
	 */
	public float calculateImgEncodeAvaiableSize(String imgPath, String alg) throws ImgFormatException, IOException;
	
	
	
	public void encode(String imgPath, String filePath, String alg, String password) throws IOException;
	
	
	
	public void encodeAndEncrypt(String imgPath, String filePath, String alg) throws IOException;
	
	
	
	public void decode(String imgPath, String alg) throws IOException;
	
	
	
	public void DecryptAndDecode(String imgPath, String alg, String password) throws IOException;
	
	

}