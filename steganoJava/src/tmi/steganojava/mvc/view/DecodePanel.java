package tmi.steganojava.mvc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class DecodePanel extends JPanel {
	
	private static final long serialVersionUID = 262764945702533763L;
	private ResourceBundle res;
	private JTextField txtFile;
	private JPasswordField pass1;
	
	public DecodePanel(ResourceBundle res) {
		super();
		
	/* Init variables */
		this.res = res;
		
		
	/* Set variables */
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
		
	/* Add panels to JPanel */
		this.selectFilePanel();
		this.optionsPanel();
		this.buttonsPanel();
		
	}
	
	private void selectFilePanel() {
		JPanel selectFilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectFilePanel.setBorder(BorderFactory.createTitledBorder(res.getString("title3")));
		
		this.txtFile = new JTextField(res.getString("select3"));
		this.txtFile.setPreferredSize(new Dimension(480, 27));
		this.txtFile.setEditable(false);
		
		JButton bExamine = new JButton(res.getString("examine"));
		bExamine.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if ( fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
					txtFile.setText(fileChooser.getSelectedFile().toString());
					// ...
				} 
			}
		});
		
		selectFilePanel.add(this.txtFile);
		selectFilePanel.add(bExamine);
		this.add(selectFilePanel);
	}
	
	private void optionsPanel() {
		
		/* Init components */
			JPanel optionsPanel = new JPanel(new BorderLayout());
			JPanel pSup = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel pInf = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			JLabel l1 = new JLabel(res.getString("selectAlg"));
			optionsPanel.setPreferredSize(new Dimension(480, 100));
			JLabel l2 = new JLabel(res.getString("pass"));
			this.pass1 = new JPasswordField(35);
			
			JRadioButton alg1 = new JRadioButton(res.getString("alg1"), true);
			alg1.setActionCommand(res.getString("alg1"));
			JRadioButton alg2 = new JRadioButton(res.getString("alg2"), false);
			alg2.setActionCommand(res.getString("alg2"));
			ButtonGroup groupAlg = new ButtonGroup();
			groupAlg.add(alg1);
			groupAlg.add(alg2);
			
		/* Config components */
			optionsPanel.setBorder(BorderFactory.createTitledBorder(res.getString("label3")));
					
			
		/* Add components to relative JPanels and JFrame */
			
			pSup.add(l1);
			pSup.add(alg1);
			pSup.add(alg2);
			optionsPanel.add(pSup, BorderLayout.NORTH);
			pInf.add(l2);
			pInf.add(this.pass1);
			optionsPanel.add(pInf);
			this.add(optionsPanel);
		}

	
	private void buttonsPanel() {
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JButton bAccept = new JButton(res.getString("tabDecode")); 
		JButton bClean= new JButton(res.getString("clean"));
		
		bAccept.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pass1.setText("");
			}
		});
		
		bClean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txtFile.setText(res.getString("select3"));
				pass1.setText("");
			}
		});
		
		bottomPanel.add(bAccept);
		bottomPanel.add(bClean);
		
		this.add(bottomPanel);
	}

}
