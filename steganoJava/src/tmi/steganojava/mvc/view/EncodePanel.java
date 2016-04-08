package tmi.steganojava.mvc.view;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class EncodePanel extends JPanel{
	
	private static final long serialVersionUID = 8470874018217488156L;
	private ResourceBundle res;
	private JTextField txtImage, txtFile;
	private JCheckBox checkPass;
	private JPasswordField pass1, pass2;
	
	public EncodePanel(ResourceBundle resources) {
		super();
		
	/* Set variables */
		this.res = resources;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		

	/* Add panels */
		this.selectFile1Panel();
		this.selectFile2Panel();
		this.optionsPanel();
		this.buttonsPanel();
	}
	
	private void selectFile1Panel() {
		JPanel selectFile1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectFile1Panel.setBorder(BorderFactory.createTitledBorder(res.getString("title1")));
		
		this.txtImage = new JTextField(res.getString("select1"));
		this.txtImage.setPreferredSize(new Dimension(480, 27));
		this.txtImage.setEditable(false);
		
		JButton bExamine = new JButton(res.getString("examine"));
		bExamine.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if ( fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
					txtImage.setText(fileChooser.getSelectedFile().toString());
					// ...
				} 
			}
		});
		
	
		selectFile1Panel.add(this.txtImage);
		selectFile1Panel.add(bExamine);
		this.add(selectFile1Panel);
	}
	
	private void selectFile2Panel() {
		JPanel selectFile2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectFile2Panel.setBorder(BorderFactory.createTitledBorder(res.getString("title2")));
		
		this.txtFile = new JTextField(res.getString("select2"));
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
		
	
		selectFile2Panel.add(this.txtFile);
		selectFile2Panel.add(bExamine);
		this.add(selectFile2Panel);
	}
	
	private void optionsPanel() {
		
	/* Init components */
		JPanel optionsPanel = new JPanel(new GridLayout(1, 2));
		optionsPanel.setPreferredSize(new Dimension(400, 160));
		
		JLabel l1 = new JLabel(res.getString("pass"));
		JLabel l2 = new JLabel(res.getString("repPass"));
		JLabel l3 = new JLabel(res.getString("selectAlg"));
		
		this.checkPass = new JCheckBox(res.getString("usePass"));
		
		this.pass1 = new JPasswordField(15);
		this.pass2 = new JPasswordField(15);
		
		JPanel pLeft = new JPanel(new GridLayout(5, 1));
		JPanel pRight = new JPanel(new GridLayout(5, 1));
		
		JRadioButton alg1 = new JRadioButton(res.getString("alg1"), true);
		JRadioButton alg2 = new JRadioButton(res.getString("alg2"), false);
		
		ButtonGroup group = new ButtonGroup();
		group.add(alg1);
		group.add(alg2);
		
	/* Config components */
		optionsPanel.setBorder(BorderFactory.createTitledBorder(res.getString("label3")));
		
		this.pass1.setEnabled(false);
		this.pass1.setOpaque(false);
		this.pass2.setEnabled(false);
		this.pass2.setOpaque(false);

		
	/* Add listeners */
		this.checkPass.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if ( checkPass.isSelected() ) {
					pass1.setEnabled(true);
					pass1.setOpaque(true);
					pass2.setEnabled(true);
					pass2.setOpaque(true);
				}
				else {
					pass1.setEnabled(false);
					pass1.setOpaque(false);
					pass2.setEnabled(false);
					pass2.setOpaque(false);
				}							
			}
		});
		
		
	/* Add components to relative JPanels and JFrame */
		pLeft.add(this.checkPass);
		pLeft.add(l1);		
		pLeft.add(this.pass1);
		pLeft.add(l2);
		pLeft.add(this.pass2);
		
		pRight.add(l3);
		pRight.add(alg1);
		pRight.add(alg2);
		
		optionsPanel.add(pLeft);
		optionsPanel.add(pRight);
	
		this.add(optionsPanel);
	}
	
	private void buttonsPanel() {
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JButton bAccept = new JButton(res.getString("tabEncode")); 
		JButton bClean= new JButton(res.getString("clean"));
		
		bAccept.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Do something...
			}
		});
		
		bClean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txtImage.setText(res.getString("select1"));
				txtFile.setText(res.getString("select2"));
				checkPass.setSelected(false);
				pass1.setText("");
				pass2.setText("");
			}
		});
		
		bottomPanel.add(bAccept);
		bottomPanel.add(bClean);
		
		this.add(bottomPanel);
	}

}