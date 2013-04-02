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
	JTextField txtEmail = null;
	private String pid;
	
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
		AdminPoll poll = new AdminPoll(this, socket);
		polls.add(poll);
		setPid(poll); //CHANGED 1
		Thread pollThread = new Thread(poll);
		pollThread.start();
		
		
		addGuiComponents();
	}

	private void addGuiComponents() {
	
		// email component
		JPanel emailPanel = new JPanel();
		emailPanel.add(new JLabel("Email:"));
		txtEmail = new JTextField();
		txtEmail.setPreferredSize(new Dimension(300,25));
		emailPanel.add(txtEmail);
		panel.add(emailPanel);
		

		panel.add(polls.get(0));
		
	}

	//CHANGED 2
	public void setPid(AdminPoll a){
		this.pid = a.getPollID();
		System.out.println("POLLLLLLLLLLLL ID -> "+ getPid());
	}
	
	//CHANGED 3
	public String getPid()
	{
		if(this.pid!=null)
		{
			return this.pid;
		}
		else
		{
			return "NO_ID_FOUND";
		}
	}
	public String getEmailAddress() {
		if(txtEmail != null)
		{
			return this.txtEmail.getText();
		}
		else
		{
			return "";
		}
	}
	//



}
