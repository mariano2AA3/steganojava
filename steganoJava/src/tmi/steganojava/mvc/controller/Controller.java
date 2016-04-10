package tmi.steganojava.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;


import tmi.steganojava.exceptions.ImgFormatException;

public class Controller {
	
	private ArrayList<String> imgFormatAdmitted;
	
	public Controller() {
		this.imgFormatAdmitted = new ArrayList<String>();
		
		// Add supported image format extentions
		this.imgFormatAdmitted.add("bmp");
	}
		
	/**
	 * This function checks if the image path file contains a extension recognize by the application.
	 * Now, there are recognized the following formats: BMP 
	 * @param imgPath
	 * @return true if imgPath contains a recognize format
	 */
	public boolean isImgFormatCorrect(String imgPath) {
		String extention = imgPath.substring(imgPath.indexOf(".") + 1, imgPath.length());
		return this.imgFormatAdmitted.contains(extention);
	}
	
	
	/**
	 * 
	 * @param path
	 * @param alg
	 * @return
	 * @throws ImgFormatException
	 * @throws IOException
	 */
	public float calculateImgEncodeAvaiableSize(String imgPath, String alg) { //throws ImgFormatException, IOException {
		return 0;
	}
	
	
	
	public void encode(String imgPath, String filePath, String alg, String password) {
		System.out.println("INFO: Encoding file: " + filePath + " in image: " + imgPath + " with algotithm: " + alg);
	}
	
	
	
	public void encodeAndEncrypt(String imgPath, String filePath, String alg) throws IOException {
	}
	
	
	
	public void decode(String imgPath, String alg) throws IOException {
	}
	
	
	
	public void DecryptAndDecode(String imgPath, String alg, String password) throws IOException {
	}

	public String getSupportedFormatString() {
		return this.imgFormatAdmitted.toString();
	}
	
	

}
