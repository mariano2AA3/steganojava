package tmi.steganojava.mvc.view;

import tmi.steganojava.mvc.controller.Controller;

public interface View {
	
	public void registerController(Controller c);
	
	public void updateUI();
	
	public void showInfoMsg(String msg);
	
	public void showErrorMsg(String msg);
	
	public void showImgReadErrorMsg();
	
	public void showFileReadErrorMsg();
	

}
