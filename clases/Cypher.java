import java.util.Vector;

public interface Cypher {

    public Vector  myController;

  public void encode(String plainText, String pass);

  public void decode(String encryptText, String pass);

}