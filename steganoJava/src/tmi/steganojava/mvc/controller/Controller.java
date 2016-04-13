package tmi.steganojava.mvc.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import tmi.steganojava.exceptions.ImgFormatException;
import tmi.steganojava.mvc.view.View;
import tmi.steganojava.stegoalgorithms.Lsb;
import tmi.steganojava.utils.ReadWriteFile;
import tmi.steganojava.utils.Security;

public class Controller {
	
	private ReadWriteFile rwFile;
	 
	private static Lsb lsbInstanceLsb = null;	
	private ArrayList<String> imgFormatAdmitted;
	private View view;
	
	
	public Controller(View w) {
	
		this.rwFile 	       = new ReadWriteFile();
		this.imgFormatAdmitted = new ArrayList<String>();
		this.view 			   = w;
		
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
		
		String extention = getFileExtention(imgPath);

		return this.imgFormatAdmitted.contains(extention);
	}
	
	
	/**
	 * This functions estimates the avaiable image space to encode files, using specified algorithm.
	 *   
	 * @param imgPath: the path of the image.
	 * @param alg: the algorithm using
	 * @return a float that represents the image size if MB.
	 * @throws ImgFormatException
	 * @throws IOException
	 */
	public float calculateImgEncodeAvaiableSize(String imgPath, String alg) {
		switch (alg) {
			case "LSb":
				// 
			break;

		default:
			break;
		}
		return 10;
	}
	

	public void encode(String imgPath, String filePath, String alg) {
		
		this.encodeAux(imgPath, filePath, alg, null);
	}
	
	
	
	public void encodeAndEncrypt(String imgPath, String filePath, String alg, String password) {
		
		this.encodeAux(imgPath, filePath, alg, password);
	}
	
	
	
	public void decode(String imgPath, String alg) throws IOException {
	}
	
	
	
	public void DecryptAndDecode(String imgPath, String alg, String password) {
	}

	public String getSupportedFormatString() {
		
		return this.imgFormatAdmitted.toString();
	}
	
	
	private String getFileExtention(String filePath) {
		
		return filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
	}
	
	private void encodeAux(String imgPath, String filePath, String alg, String password) {
		
		byte[] fileBytes;
		BufferedImage bufferedImg        = null;
		BufferedImage bufferedimgSecret  = null;
		ByteArrayOutputStream out        = new ByteArrayOutputStream();
		
		try {
			System.out.println("INFO:");
			System.out.println(" |- reading img... ");
			bufferedImg = this.rwFile.readImg(imgPath);
			System.out.println(" |- reading file... ");
			fileBytes   = this.rwFile.readFile(filePath);
			
			// Checks if file fits into image...
			if ( this.calculateImgEncodeAvaiableSize(imgPath, alg) * 1024 >= fileBytes.length ) {
				System.out.println(" |- Encoding file: " + filePath + " in image: " + imgPath);
				System.out.println(" |- encoding using algotithm: " + alg);
				
				// If user check PasswordChechBox, then file byte array will be encrypted...
				if ( password != null ) {
					System.out.println("|- encrypting file using password");
					// Encrypt file...
					// fileBytes = ...
				}
				
				switch (alg) {
		
					case "LSB":						
						bufferedimgSecret = getLsbInstance().encode(bufferedImg, fileBytes);
					break;
		
					default:
						this.view.showErrorMsg("Error: Unknown Encoding algorithm!");
					break;
					
				}
				
				
				// Writes cover image to disk...
				ImageIO.write(bufferedimgSecret, this.getFileExtention(imgPath), out);
				out.flush();
				this.rwFile.writeFile(imgPath + "_secret_." + this.getFileExtention(imgPath), out.toByteArray());
				out.close();
				this.view.showInfoMsg("Great!! Your secret image has been created");
			}
			else {
				this.view.showErrorMsg("Error: file size is bigger than image size. Please use larger image or change the algorithm.");
			}
			

		} catch (IOException e) {
			this.view.showImgReadErrorMsg();
		} catch (ImgFormatException e) {
			this.view.showErrorMsg("Error: image format not supported");
		}
	}
	
	private Lsb getLsbInstance() {
		
		if (Controller.lsbInstanceLsb == null) {
			return new Lsb();
		}
		else return Controller.lsbInstanceLsb;
	}
	

}
