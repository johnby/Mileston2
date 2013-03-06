package admin;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.bind.JAXBException;

import messages.*;

public class AdminPoll extends JPanel implements ActionListener, Runnable {

	public enum State { Connect, Create, Running, Closed };
	public enum PollState { None, Open, Paused, Closed };

	private boolean active = true;
	private Socket socket = null;
	
	private State state = State.Connect;
	private PollState pState = PollState.None;
	
	private JTextField txtQuestion = null;
	private JButton btnCreate = null;
	private JButton btnPause = null;
	private JButton btnResume = null;
	private JButton btnStop = null;
	private JButton btnAdd = null;
	private JLabel lblPollId = null;
	private JTextArea pollStatus = null;
	
	protected BufferedReader reader = null;
	protected PrintWriter writer = null;
	
	private String pollId = "";
	//private String email = null;
	
	public AdminPoll(Socket socket) throws IOException
	{
		//this.email = email;
		this.socket = socket;
		
		// setup reader
		InputStream inputstream = this.socket.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        reader = new BufferedReader(inputstreamreader);
        
        // setup writer
        OutputStream outputStream = this.socket.getOutputStream();
        OutputStreamWriter osr = new OutputStreamWriter(outputStream);
        writer = new PrintWriter(socket.getOutputStream(), true);
		
		setupPanel();
	}

	private void setupPanel() {

		// question comp
		JPanel questionPanel = new JPanel();
		questionPanel.add(new JLabel("Question:"));
		txtQuestion = new JTextField();
		txtQuestion.setPreferredSize(new Dimension(300,25));
		questionPanel.add(txtQuestion);
		this.add(questionPanel);
		
		txtQuestion.addActionListener(this);
		
		// answer comp
		JPanel answerPanel = new JPanel();
		answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.PAGE_AXIS));
		lblPollId = new JLabel("");
		answerPanel.add(lblPollId);
		answerPanel.add(new JLabel("Answers:"));
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(this);
		answerPanel.add(btnAdd);
		
		answerPanel.add(createAnswerPanel(1));
		answerPanel.add(createAnswerPanel(2));
		
		this.add(answerPanel);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		btnCreate = new JButton("Create");
		btnCreate.addActionListener(this);
		controlPanel.add(btnCreate);
		btnPause = new JButton("Pause");
		btnPause.addActionListener(this);
		controlPanel.add(btnPause);
		btnResume = new JButton("Resume");
		btnResume.addActionListener(this);
		controlPanel.add(btnResume);
		btnStop = new JButton("Stop");
		btnStop.addActionListener(this);
		controlPanel.add(btnStop);
		
		this.add(controlPanel);
		
		pollStatus = new JTextArea();
		pollStatus.setPreferredSize(new Dimension(100,300));
		this.add(pollStatus);
	}
	
	private static JPanel createAnswerPanel(int id)
	{
		JPanel a = new JPanel();
		a.add(new JLabel("Answer " + id));
		JTextField answer = new JTextField();
		answer.setPreferredSize(new Dimension(300,25));
		a.add(answer);
		
		return a;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		
		if(source instanceof JButton)
		{
			JButton button = (JButton)source;
			System.out.println("Button pressed: " + button.getText());
			
			if(source.equals(btnCreate))
			{
				if(this.state == State.Connect)
				{
					Connect c = new Connect();
					c.setEmailAddress("test email");
					sendMessage(c);
				}
			}
		}
	}
	
	public void run()
	{
		while(active)
		{
			try {
				Object message = waitForMessage();
				
				messageReceived(message);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void messageReceived(Object message)
	{
		if(this.state == State.Connect)
		{
			if(message instanceof ConnectReply)
			{
				this.state = State.Create;
				
				CreatePoll cp = new CreatePoll();
				cp.setQuestion(txtQuestion.getText());
				
				// Add answers here!!!!!!
				// TO-DO: !!!
				
				sendMessage(cp);
			}
		}
		else if(this.state == State.Create)
		{
			if(message instanceof CreatePollReply)
			{
				CreatePollReply cpr = (CreatePollReply)message;
				lblPollId.setText(cpr.getPollId());
				
				this.state = State.Running;
			}
		}
		else if(this.state == State.Running)
		{
			if(message instanceof PausePollReply)
			{
				this.pState = PollState.Paused;
			}
			else if(message instanceof ResumePollReply)
			{
				this.pState = PollState.Open;
			}
			else if(message instanceof StopPollReply)
			{
				this.state = State.Closed;
				this.pState = PollState.Closed;
			}
			else if(message instanceof PollUpdate)
			{
				PollUpdate pu = (PollUpdate)message;
				String id = pu.getPollId();
				ArrayList<Long> results = pu.getResults();
				
				String status = "Id = " + id;

				int i = 0;
				for(Long s: results)
				{
					
					status += i + " " + s.toString() + "\n";
					i++;
				}
				
				pollStatus.setText(status);
			}
		}
		else if(this.state == State.Closed)
		{
			
		}
	}

	public void quit() throws IOException
	{
		this.socket.close();
	}
	
	private Object waitForMessage() throws IOException, JAXBException
	{
		Object o = null;
		String line = null;
		String message = "";

		while((line = this.reader.readLine())!=null)
		{
			if(line.equals(Message.EndOfMessageLine))
			{
				o = Message.unmarshal(message);
				break;
			}
			else
			{
				message += line;
			}
		}

		
		return o;
	}
	
	private void sendMessage(Message m)
	{
		synchronized (this.writer) {
			try {
				this.writer.println(m.marshal() + "\n" + Message.EndOfMessageLine);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}
	
}
