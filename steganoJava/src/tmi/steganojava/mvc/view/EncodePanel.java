package tmi.steganojava.mvc.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/**
 * TMI - StegoJava: application designed for encode and decode files into images
 * 
 * @author Mariano Hernandez Garcia
 */
public class EncodePanel extends JPanel{
	
	private static final long serialVersionUID = 8470874018217488156L;
	private ResourceBundle res;
	private JTextField txtImage, txtFile;
	private JCheckBox checkPass;
	private JPasswordField pass1, pass2;
	private JLabel imageLabel;
	private ButtonGroup groupAlg;
	private JButton bEncode;
	private int bEncodeReady;
	
	private String imgPath, filePath;
	
	public EncodePanel(ResourceBundle resources) {
		super();
		
	/* Set variables */
		this.res = resources;
		bEncodeReady = 0;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		

	/* Add panels */
		this.selectFile1Panel();
		this.selectFile2Panel();
		this.optionsPanel();
		this.buttonsPanel();
	}
	
	private void selectFile1Panel() {
		
		imgPath = "";
		filePath = "";
		
		JPanel selectFile1Panel = new JPanel(new BorderLayout());
		selectFile1Panel.setPreferredSize(new Dimension(635, 80));
		selectFile1Panel.setBorder(BorderFactory.createTitledBorder(res.getString("title1")));
		JPanel aux = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		this.txtImage = new JTextField(res.getString("select1"));
		this.txtImage.setPreferredSize(new Dimension(520, 27));
		this.txtImage.setEditable(false);
		
		this.imageLabel = new JLabel();
		
		JButton bExamine = new JButton(res.getString("examine"));
		bExamine.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if ( fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
					imgPath = fileChooser.getSelectedFile().toString();
					txtImage.setText(imgPath);
					
					if (!MainWindow.controller.isImgFormatCorrect(imgPath)) {
						imageLabel.setForeground(Color.RED);
						imageLabel.setText(res.getString("errorImgFormat") +  MainWindow.controller.getSupportedFormatString());
					}
					else { 
						/*
						bEncodeReady++;
						if ( bEncodeReady == 2 ) {
							bEncode.setEnabled(true);
						}
						else {
							bEncode.setEnabled(false);
						}*/
						imageLabel.setText(
							res.getString("imgSpaceAvaiable") + " " + 
							MainWindow.controller.calculateImgEncodeAvaiableSize(
									imgPath, groupAlg.getSelection().getActionCommand()
							) 
							+ " KB"
						);
					}
				} 
			}
		});
		
		aux.add(this.txtImage);
		aux.add(bExamine);
		selectFile1Panel.add(aux, BorderLayout.CENTER);
		selectFile1Panel.add(imageLabel, BorderLayout.SOUTH);
		this.add(selectFile1Panel);
	}
	
	private void selectFile2Panel() {
		JPanel selectFile2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectFile2Panel.setBorder(BorderFactory.createTitledBorder(res.getString("title2")));
		selectFile2Panel.setPreferredSize(new Dimension(635, 60));
		
		this.txtFile = new JTextField(res.getString("select2"));
		this.txtFile.setPreferredSize(new Dimension(520, 27));
		this.txtFile.setEditable(false);
		
		JButton bExamine = new JButton(res.getString("examine"));
		bExamine.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if ( fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
					filePath = fileChooser.getSelectedFile().toString();
					txtFile.setText(fileChooser.getSelectedFile().toString());
					
					/*bEncodeReady++;
					if ( bEncodeReady == 2 ) {
						bEncode.setEnabled(true);
					}
					else {
						bEncode.setEnabled(false);
					}
					*/
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
		optionsPanel.setPreferredSize(new Dimension(400, 170));
		
		JLabel l1 = new JLabel(res.getString("pass"));
		JLabel l2 = new JLabel(res.getString("repPass"));
		JLabel l3 = new JLabel(res.getString("selectAlg"));
		final JLabel lPasswordError = new JLabel();
		lPasswordError.setForeground(Color.RED);
		
		
		this.checkPass = new JCheckBox(res.getString("usePass"));
		
		this.pass1 = new JPasswordField(15);
		this.pass2 = new JPasswordField(15);
		
		JPanel pLeft = new JPanel(new GridLayout(6, 1));
		JPanel pRight = new JPanel(new GridLayout(5, 1));
		
		final JRadioButton alg1 = new JRadioButton(res.getString("alg1"), true);
		alg1.setActionCommand(res.getString("alg1"));
		JRadioButton alg2 = new JRadioButton(res.getString("alg2"), false);
		alg2.setActionCommand(res.getString("alg2"));
		groupAlg = new ButtonGroup();
		groupAlg.add(alg1);
		groupAlg.add(alg2);
		
		
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
		
		pass1.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) { 
				if (pass2.getPassword().length != 0 && !Arrays.equals(pass1.getPassword(), pass2.getPassword())) {
					lPasswordError.setText(res.getString("errorPassword"));
				}
				else {
					lPasswordError.setText("");
					//bEncodeReady++;
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) { }
		});
		
		pass2.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				if (pass1.getPassword().length != 0 && !Arrays.equals(pass1.getPassword(), pass2.getPassword())) {
					lPasswordError.setText(res.getString("errorPassword"));
				}
				else {
					lPasswordError.setText("");
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent arg0) { }
		});
		
		
	/* Add components to relative JPanels and JFrame */
		pLeft.add(this.checkPass);
		pLeft.add(l1);		
		pLeft.add(this.pass1);
		pLeft.add(l2);
		pLeft.add(this.pass2);
		pLeft.add(lPasswordError);
		
		pRight.add(l3);
		pRight.add(alg1);
		pRight.add(alg2);
		
		optionsPanel.add(pLeft);
		optionsPanel.add(pRight);
	
		this.add(optionsPanel);
	}
	
	private void buttonsPanel() {
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		bEncode = new JButton(res.getString("tabEncode")); 
		JButton bClean= new JButton(res.getString("clean"));
		
		/////////////
		bEncode.setEnabled(true);
		
		final Component component = this;
		
		bEncode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if ( imgPath.length() != 0 && filePath.length() != 0 ) {
					
					if ( !checkPass.isSelected() ) {
						MainWindow.controller.encode(imgPath, filePath, groupAlg.getSelection().getActionCommand());
					}
					else {
						MainWindow.controller.encodeAndEncrypt(imgPath, filePath, groupAlg.getSelection().getActionCommand(), String.valueOf(pass1.getPassword()));
					}
				}
				else {
					JOptionPane.showMessageDialog(component, "Please, fill fields!", "Error", JOptionPane.ERROR_MESSAGE);					
				}
				
				
				
				
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
				imageLabel.setText("");
				//bEncode.setEnabled(false);
			}
		});
		
		bottomPanel.add(bEncode);
		bottomPanel.add(bClean);
		
		this.add(bottomPanel);
	}

}
