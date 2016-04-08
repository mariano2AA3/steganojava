
import java.awt.EventQueue;

import tmi.steganojava.mvc.view.MainWindow;
import tmi.steganojava.resoruces.Resources_en;

public class Main {

	public static void main(String[] args) {
		
		final MainWindow w = new MainWindow(new Resources_en());
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				w.setVisible(true);
			}
		});
		

	}

}