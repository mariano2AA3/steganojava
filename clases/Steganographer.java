import java.util.Vector;

public interface Steganographer {

    public Vector  myController;

  public void putIn(String img, String secret);

  public void getOut(String img);

  public void getSpace(String img);

}