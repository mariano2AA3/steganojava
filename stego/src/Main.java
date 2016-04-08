import java.io.IOException;

public class Main {
	static bmp bmp;
	
	public static void main(String[] args) throws IOException {
		String textoAOcultar = "Hello world, stegojava is here! Be ready for me!";
		bmp = new bmp(textoAOcultar,"foto.bmp","saved.bmp");
	}
}
