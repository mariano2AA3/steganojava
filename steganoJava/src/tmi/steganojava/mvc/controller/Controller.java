package tmi.steganojava.mvc.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;


import tmi.steganojava.exceptions.ImgFormatException;
import tmi.steganojava.mvc.view.MainWindow;
import tmi.steganojava.mvc.view.View;
import tmi.steganojava.stegoalgorithms.Lsb;
import tmi.steganojava.utils.ReadWriteFile;

public class Controller {
	
	private ReadWriteFile rwFile;
	private static Lsb lsbInstanceLsb = null;
	
	private ArrayList<String> imgFormatAdmitted;
	private View view;
	
	
	
	public Controller(View w) {
		this.rwFile = new ReadWriteFile();
		this.imgFormatAdmitted = new ArrayList<String>();
		this.view = w;
		
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
		String extention = imgPath.substring(imgPath.lastIndexOf(".") + 1, imgPath.length());
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
	public float calculateImgEncodeAvaiableSize(String imgPath, String alg) {
		return 0;
	}
	
	
	
	public void encode(String imgPath, String filePath, String alg) {
		
		byte[] fileBytes;
		BufferedImage bufferedImg;
		
		System.out.println("INFO: Encoding file: " + filePath + " in image: " + imgPath);
		
		try {
			System.out.println(" |- reading img... ");
			bufferedImg = this.rwFile.readImg(imgPath);
			System.out.println(" |- reading file... ");
			fileBytes   = this.rwFile.readFile(filePath);
			
			switch (alg) {
			case "LSB":
				System.out.println(" |- encoding using algotithm: " + alg);
				//getLsbInstance().encode(bufferedImg, fileBytes);
				this.view.showInfoMsg("Great!! Your secret image has been created");
			break;

			default:
				this.view.showErrorMsg("Error: Unknown Encoding algorithm!");
			break;
			}
			
			

		} catch (IOException e) {
			this.view.showImgReadErrorMsg();
		} catch (ImgFormatException e) {
			this.view.showErrorMsg("Error: image format not supported");
		}
		

	}
	
	
	
	public void encodeAndEncrypt(String imgPath, String filePath, String alg, String password) {
		
	}
	
	
	
	public void decode(String imgPath, String alg) throws IOException {
	}
	
	
	
	public void DecryptAndDecode(String imgPath, String alg, String password) {
	}

	public String getSupportedFormatString() {
		return this.imgFormatAdmitted.toString();
	}
	
	private Lsb getLsbInstance() {
		if (Controller.lsbInstanceLsb == null) {
			return new Lsb();
		}
		else return Controller.lsbInstanceLsb;
	}
	

}
