package tmi.mvc.View;

import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreditsPanel extends JPanel {
	
	private static final long serialVersionUID = -2819945989880845264L;
	
	public CreditsPanel(ResourceBundle res) {
		super();
		
	/* Init Variables */
		JLabel l1 = new JLabel(res.getString("credits"));
		JLabel l2 = new JLabel(res.getString("autor1"));
		JLabel l3 = new JLabel(res.getString("autor2"));
		JLabel l4 = new JLabel(res.getString("autor3"));
		Font font = new Font("Arial", Font.BOLD, 22);
		Font font2 = new Font("Arial", Font.BOLD, 18);
		
	/* Set variables */
		setLayout(new FlowLayout(FlowLayout.CENTER, 100, 25));
	
		l1.setFont(font);
		l2.setFont(font2);
		l3.setFont(font2);
		l4.setFont(font2);
		
		this.add(l1);
		this.add(l2);
		this.add(l3);
		this.add(l4);
	}

}
