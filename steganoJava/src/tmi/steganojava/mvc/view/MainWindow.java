package tmi.steganojava.mvc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import tmi.steganojava.mvc.controller.Controller;
import tmi.steganojava.resoruces.Resources_en;

public class MainWindow extends JFrame implements View {
	
	public static Controller controller;
	
	@Override
	public void registerController(Controller c) {
		MainWindow.controller = c;
	}

	@Override
	public void updateUI() {}

	@Override
	public void showInfoMsg(String msg) {
		JOptionPane.showMessageDialog(this, msg, this.resources.getString("info"), JOptionPane.OK_OPTION);
	}

	@Override
	public void showErrorMsg(String msg) {
		JOptionPane.showMessageDialog(this, msg, this.resources.getString("error"), JOptionPane.ERROR_MESSAGE);
	}
	
	private static final long serialVersionUID = -7952723121471145730L;
	private Dimension dimension;
	private Resources_en resources;
	
	public MainWindow(Resources_en res) {
		super();
		
	/* Init variables */
		this.dimension       = new Dimension(600, 490);
		this.resources 		 = res;
		JLabel label         = new JLabel(res.getString("title"));
		JTabbedPane pane     = new JTabbedPane();
		EncodePanel encode   = new EncodePanel(res);
		DecodePanel decode   = new DecodePanel(res);
		CreditsPanel credits = new CreditsPanel(res);
		
	/* Set variables */
		setResizable(false);
		setTitle(res.getString("title"));
		setSize(dimension);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);				// Center window
		setExtendedState(MAXIMIZED_BOTH);
		
		label.setPreferredSize(new Dimension(200, 100));
		label.setFont(new Font("Arial", Font.BOLD , 18));
		
		pane.setPreferredSize(new Dimension(600, 370));
		pane.add(res.getString("tabEncode"), encode);
		pane.add(res.getString("tabDecode"), decode);
		pane.add(res.getString("tabCredits"), credits);
		
		
	/* Add to panel */
		getContentPane().add(label, BorderLayout.NORTH);
		getContentPane().add(pane);
	}



}
