package admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class AdminClient extends JFrame {

	JPanel panel = null;
	ArrayList<AdminPoll> polls = null;
	Socket socket = null;
	
	public AdminClient(String host, int port) throws IOException
	{	
		super("Admin Client");
	
		socket = new Socket(host, port);
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setPreferredSize(new Dimension(800,600));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-panel.getPreferredSize().width/2, dim.height/2-panel.getPreferredSize().height/2);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		
		polls = new ArrayList<AdminPoll>();
		AdminPoll poll = new AdminPoll(socket);
		polls.add(poll);
		Thread pollThread = new Thread(poll);
		pollThread.start();
		
		
		addGuiComponents();
	}

	private void addGuiComponents() {
	
		// email component
		JPanel emailPanel = new JPanel();
		emailPanel.add(new JLabel("Email:"));
		JTextField email = new JTextField();
		email.setPreferredSize(new Dimension(300,25));
		emailPanel.add(email);
		panel.add(emailPanel);
		

		panel.add(polls.get(0));
		
	}
	



}
