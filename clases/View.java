import java.util.Vector;

public interface View {

    public Vector  myController;
    
  public void update();

  public void showInfoMsg(String msg);

  public void showErrorMsg(String msg);

}