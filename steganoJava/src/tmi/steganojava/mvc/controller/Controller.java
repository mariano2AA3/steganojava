package tmi.steganojava.mvc.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import tmi.steganojava.utils.Pair;

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
	 * This functions estimates the available image space to encode files, using specified algorithm.
	 *   
	 * @param imgPath: the path of the image.
	 * @param alg: the algorithm using
	 * @return a float that represents the image size if MB.
	 * @throws ImgFormatException
	 * @throws IOException
	 */
	public float calculateImgEncodeAvaiableSize(String imgPath, String alg) {
		float cal = 10.0f;
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(imgPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch (alg) {
			case "LSB":
				// 
				cal = getLsbInstance().getImgEncodeSpace(img);
				
			break;

		default:
			break;
		}
		return cal;
	}
	

	public void encode(String imgPath, String filePath, String alg) {
		this.encodeAux(imgPath, filePath, alg, null);		
	}
	
	
	
	public void encodeAndEncrypt(String imgPath, String filePath, String alg, String password) {	
		this.encodeAux(imgPath, filePath, alg, password);
	}
	
	
	
	public void decode(String imgPath, String alg){
		this.decodeAux(imgPath, alg, null);
	}
	
	
	
	public void DecryptAndDecode(String imgPath, String alg, String password) {
		this.decodeAux(imgPath, alg, password);
	}

	public String getSupportedFormatString() {	
		return this.imgFormatAdmitted.toString();
	}
	
	
	private String getFileExtention(String filePath) {	
		return filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
	}
	
	private void decodeAux(String imgPath, String alg, String password){
		BufferedImage bufferedImg = null;
		byte[] fileBytes = null;
		try {
			System.out.println("INFO:");
			System.out.println(" |- Reading encode file... ");
			bufferedImg = this.rwFile.readImg(imgPath);	
			System.out.println(" |- Decoding image: " + imgPath);
			System.out.println(" |- Decoding using algotithm: " + alg);
			Pair<byte[], Character[]> pair;
			switch (alg) {	
				case "LSB":						
					pair = getLsbInstance().decode(bufferedImg);
				break;
	
				default:
					this.view.showErrorMsg("Error: Unknown Encoding algorithm!");
					return;
				
			}
			Character[] chares = pair.getRight();
			String decodedFileString = "DecodedFile";
			String decodedFileMime   = "";
			for(int i = 0; i < chares.length; i++){
				if(chares[i] != ' '){
					decodedFileMime = decodedFileMime.concat("" + chares[i]);
				}
			}
			decodedFileString = decodedFileString.concat(".").concat(decodedFileMime);
			
			if (password != null) {
				System.out.println("|- decrypting file...");
				try {
					fileBytes = Security.decrypt(pair.getLeft(), password);
				}catch(Exception e){
					this.view.showErrorMsg("Error: Invalid password.");
					return;
				}
			}
			else {
				fileBytes = pair.getLeft();
			}
	        try {	             	
	        	System.out.println(" |- Writting hidden object into file: \""+ decodedFileString +"\"");
	        	this.rwFile.writeFile(decodedFileString, fileBytes);
	        	this.view.showInfoMsg("Great!! Your hidden file has been rebuilt");
	        }catch(Exception e){
	        	this.view.showErrorMsg("Error: can't write file");
	        }
		} catch (IOException e) {
			this.view.showImgReadErrorMsg();
		} catch (ImgFormatException e) {
			this.view.showErrorMsg("Error: image format not supported");
		}	
	}
	
	private void encodeAux(String imgPath, String filePath, String alg, String password) {
		
		byte[] fileBytes;
		char[] mimeArray                 = new char[4];
		BufferedImage bufferedImg        = null;
		BufferedImage bufferedimgSecret  = null;
		ByteArrayOutputStream out        = new ByteArrayOutputStream();
		String mime;
		
		try {
			System.out.println("INFO:");
			System.out.println(" |- reading img... ");
			bufferedImg = this.rwFile.readImg(imgPath);
			System.out.println(" |- reading file... ");
			fileBytes   = this.rwFile.readFile(filePath);
			
			// If user check PasswordChechBox, then file byte array will be encrypted...
			if ( password != null ) {
				System.out.println("|- encrypting file...");
				fileBytes = Security.encrypt(fileBytes, password);
			}
			
			mime = this.getFileExtention(filePath);			
			for(int i = 0; i < 4;i++){
				if(i<mime.length()){
					mimeArray[i] = mime.charAt(i);
				}else{
					mimeArray[i] = ' ';
				}
			}
			
			// Checks if file fits into image...
			if ( this.calculateImgEncodeAvaiableSize(imgPath, alg) * 1024 >= fileBytes.length ) {
				System.out.println(" |- Encoding file: " + filePath + " in image: " + imgPath);
				System.out.println(" |- encoding using algotithm: " + alg);
				
				switch (alg) {
		
					case "LSB":						
						bufferedimgSecret = getLsbInstance().encode(bufferedImg, fileBytes,mimeArray);
					break;
		
					default:
						this.view.showErrorMsg("Error: Unknown Encoding algorithm!");
					break;
					
				}
				

				// Writes cover image to disk...
				//File outputfile = new File("salida_oculta.bmp");
			    //ImageIO.write(bufferedimgSecret, "bmp", outputfile);
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
