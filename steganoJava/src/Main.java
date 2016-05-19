
import java.awt.EventQueue;
import tmi.steganojava.mvc.controller.Controller;
import tmi.steganojava.mvc.view.MainWindow;
import tmi.steganojava.resoruces.Resources_en;

public class Main {
	
	public static Controller controller;

	public static void main(String[] args) {
		
		// Instanciate GUI
		final MainWindow w = new MainWindow(new Resources_en());
		
		// Instanciate Controller
		controller = new Controller(w);
		
		w.registerController(controller);
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				w.setVisible(true);
			}
		});
		

	}

}
