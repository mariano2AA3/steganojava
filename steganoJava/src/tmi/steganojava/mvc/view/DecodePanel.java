package tmi.steganojava.mvc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
	private JButton bDecode;
	private ButtonGroup groupAlg;
	
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
		selectFilePanel.setPreferredSize(new Dimension(635, 60));
		selectFilePanel.setBorder(BorderFactory.createTitledBorder(res.getString("title3")));
		
		this.txtFile = new JTextField(res.getString("select3"));
		this.txtFile.setPreferredSize(new Dimension(520, 27));
		this.txtFile.setEditable(false);
		
		JButton bExamine = new JButton(res.getString("examine"));
		bExamine.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if ( fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
					txtFile.setText(fileChooser.getSelectedFile().toString());
					bDecode.setEnabled(true);
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
			JPanel pLeft = new JPanel(new GridLayout(5, 1));//(new FlowLayout(FlowLayout.LEFT));
			JPanel pInf = new JPanel(new FlowLayout(FlowLayout.CENTER));
			
			JLabel l1 = new JLabel(res.getString("selectAlg"));
			optionsPanel.setPreferredSize(new Dimension(635, 150));
			JLabel l2 = new JLabel(res.getString("pass"));
			this.pass1 = new JPasswordField(35);
			
			JRadioButton alg1 = new JRadioButton(res.getString("alg1"), true);
			alg1.setActionCommand(res.getString("alg1"));
			JRadioButton alg2 = new JRadioButton(res.getString("alg2"), false);
			alg2.setActionCommand(res.getString("alg2"));
			groupAlg = new ButtonGroup();
			groupAlg.add(alg1);
			groupAlg.add(alg2);
			
			
		/* Config components */
			optionsPanel.setBorder(BorderFactory.createTitledBorder(res.getString("label3")));
					
			
		/* Add components to relative JPanels and JFrame */
			pLeft.add(l1);
			pLeft.add(alg1);
			pLeft.add(alg2);
			optionsPanel.add(pLeft, BorderLayout.WEST);
			pInf.add(l2);
			pInf.add(this.pass1);
			optionsPanel.add(pInf);
			this.add(optionsPanel);
		}

	
	private void buttonsPanel() {
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		bDecode = new JButton(res.getString("tabDecode")); 
		JButton bClean= new JButton(res.getString("clean"));
		
		bDecode.setEnabled(false);
		
		bDecode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( pass1.getPassword().length == 0 ) {	
					MainWindow.controller.decode(txtFile.getText(), groupAlg.getSelection().getActionCommand());
				}
				else {
					MainWindow.controller.DecryptAndDecode(txtFile.getText(), groupAlg.getSelection().getActionCommand(), String.valueOf(pass1.getPassword()));
				}
				
				
			}
		});
		
		bClean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txtFile.setText(res.getString("select3"));
				pass1.setText("");
				bDecode.setEnabled(false);
			}
		});
		
		bottomPanel.add(bDecode);
		bottomPanel.add(bClean);
		
		this.add(bottomPanel);
	}

}
