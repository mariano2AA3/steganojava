package tmi.steganojava.resoruces;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class Resources_en extends ResourceBundle {

	public Object handleGetObject(String key) {
		if (key.equals("title")) return "TMI - SteganoJava";
		if (key.equals("tabEncode")) return "Encode";
		if (key.equals("tabDecode")) return "Decode";
		if (key.equals("tabCredits")) return "Credits";
		if (key.equals("title1")) return "Select a cover image";			// image, audio...
		if (key.equals("select1")) return "Select image";
		if (key.equals("title2")) return "Select file to hide";
		if (key.equals("select2")) return "Select file to hide";
		if (key.equals("examine")) return "Examine";
		if (key.equals("label3")) return "Options";
		if (key.equals("pass")) return "Password";
		if (key.equals("repPass")) return "Repeat password";
		if (key.equals("usePass")) return "Use password";
		if (key.equals("selectAlg")) return "Select algorithm";
		if (key.equals("alg1")) return "LSB";
		if (key.equals("alg2")) return "FilterFirst";
        if (key.equals("accept")) return "Accept";
        if (key.equals("cancel")) return "Cancel";
        if (key.equals("clean")) return "Clean";
        if (key.equals("title3")) return "Select file with hidden content";
        if (key.equals("select3")) return "Select file";
        if (key.equals("credits")) return "This application has been made by:";
        if (key.equals("autor1")) return "Jorge Casas Hernan";
        if (key.equals("autor2")) return "Mariano Hernandez Garcia";
        if (key.equals("autor3")) return "Alberto Lorente Sanchez";
        if (key.equals("info")) return "Info";
        if (key.equals("error")) return "Error";
        if (key.equals("errorImgFormat")) return "Error, now only are supported the following formats:";
        if (key.equals("imgSpaceAvaiable")) return "Space avaiable:";
        if (key.equals("errorPassword")) return "Error, password does not match!";
        
        
        return null;
    }

    public Enumeration<String> getKeys() {
        return Collections.enumeration(keySet());
    }

    protected Set<String> handleKeySet() {
        return new HashSet<String>(Arrays.asList(
        		"title", 
        		"tabEncode",
        		"tabDecode",
        		"tabCredits",
        		"title1",
        		"select1",
        		"title2",
        		"select2",
        		"label3", 
        		"pass",
        		"repPass",
        		"usePass",
        		"selectAlg",
        		"alg1",
        		"alg2",
        		"accept",
        		"cancel",
        		"clean",
        		"title3",
        		"select3",
        		"credits",
        		"autor1",
        		"autor2",
        		"autor3",
        		"info",
        		"error",
        		"errorImgFormat",
        		"imgSpaceAvaiable",
        		"errorPassword"
        		));
    }
}
