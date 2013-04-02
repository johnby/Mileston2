package voter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VoteClient extends JFrame implements ActionListener {

	JPanel panel = null;
	JButton btnVote = null;
	JTextField txtPollId = null;
	JTextField txtSelection = null;
	int port = 0;
	String host = null;
	
	public VoteClient(String host, int port) throws IOException
	{	
		super("Voter Client");

		this.host = host;
		this.port = port;
		
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setPreferredSize(new Dimension(800,600));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-panel.getPreferredSize().width/2, dim.height/2-panel.getPreferredSize().height/2);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		
		
		addGuiComponents();
	}

	private void addGuiComponents() {
		
		JPanel idPanel = new JPanel();
		idPanel.add(new JLabel("Poll ID:"));
		txtPollId = new JTextField();
		txtPollId.setPreferredSize(new Dimension(75,25));
		idPanel.add(txtPollId);
		panel.add(idPanel);
		
		JPanel selectionPanel = new JPanel();
		selectionPanel.add(new JLabel("Slection:"));
		txtSelection = new JTextField();
		txtSelection.setPreferredSize(new Dimension(75,25));
		selectionPanel.add(txtSelection);
		panel.add(selectionPanel);
		
		btnVote = new JButton("Vote");

		panel.add(btnVote);

		
		btnVote.addActionListener(this);
		
	}
	
	public void vote(String id, String pollId, long selection)
	{
		String newId = "";
		if(id != null)
		{
			newId += id + " " + pollId;
		}
		else
		{
			newId = pollId;
		}
		
		DatagramSocket sendSocket = null;
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		try {
			sendSocket.send(generateVotePacket(newId, selection));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		sendSocket.close();
	}
	
	private DatagramPacket generateVotePacket(String pollId, long selection)
	{
		String message = pollId + " " + Long.toString(selection);
		
		DatagramPacket sendPacket = null;
		try {
			sendPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getLocalHost(), this.port);
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		return sendPacket;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();

		if(source instanceof JButton)
		{
			JButton button = (JButton)source;
			
			if(button.equals(btnVote))
			{
				System.out.println("Vote button pressed.");
				
				vote(null, txtPollId.getText(), Long.parseLong(txtSelection.getText()));
				txtPollId.setText("");
				txtSelection.setText("");
			}
		}
	}
	
}
