package admin;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
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

import messages.Connect;
import messages.ConnectReply;
import messages.CreatePoll;
import messages.CreatePollReply;
import messages.Message;
import messages.PausePoll;
import messages.PausePollReply;
import messages.PollUpdate;
import messages.ResumePoll;
import messages.ResumePollReply;
import messages.StopPoll;
import messages.StopPollReply;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;




public class AdminPoll extends JPanel implements ActionListener, Runnable {

	private boolean auto = false;
	private CreatePoll autoPoll = null;
	private String autoEmail = null;
	
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
	private JPanel answerPanel = null;

	private String pollName = "";
	private ChartPanel panel = null;
	private JFreeChart chart = null;
	
	private ArrayList<Answer> answers = null;
	
	protected BufferedReader reader = null;
	protected PrintWriter writer = null;
	
	private AdminClient adminClient = null;
	
	private String pollId = "";
	//private String email = null;
	
	private ArrayList<PollUpdateListener> listeners = null;
	
	public void addPollUpdateListener(PollUpdateListener listener)
	{
		listeners.add(listener);
	}
	
	public AdminPoll(AdminClient adminClient, Socket socket) throws IOException
	{
		this.adminClient = adminClient;
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
		
        answers = new ArrayList<Answer>();
        
        listeners = new ArrayList<PollUpdateListener>();
        
		setupPanel();
		updateGUI();
	}

	private void setupPanel() {

		// chart panel
		DefaultPieDataset data = new DefaultPieDataset();
		chart = ChartFactory.createPieChart("Sample Pie Chart", data,true,true,false);
		panel = new ChartPanel(chart);
		panel.setVisible(false);
		this.add(panel);
		
		// question comp
		JPanel questionPanel = new JPanel();
		questionPanel.add(new JLabel("Question:"));
		txtQuestion = new JTextField();
		txtQuestion.setPreferredSize(new Dimension(300,25));
		questionPanel.add(txtQuestion);
		this.add(questionPanel);
		
		txtQuestion.addActionListener(this);
		
		// answer comp
		answerPanel = new JPanel();
		answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.PAGE_AXIS));
		lblPollId = new JLabel("");
		answerPanel.add(lblPollId);
		answerPanel.add(new JLabel("Answers:"));
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(this);
		answerPanel.add(btnAdd);
		
		answers.add(new Answer(1));
		answers.add(new Answer(2));
		
		answerPanel.add(answers.get(0));
		answerPanel.add(answers.get(1));
		
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
		pollStatus.setPreferredSize(new Dimension(300,200));
		this.add(pollStatus);
		
		
		
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
					c.setEmailAddress(adminClient.getEmailAddress());
					sendMessage(c);
				}
			}
			else if(source.equals(btnPause))
			{
				if(this.state == State.Running && this.pState == PollState.Open)
				{
					System.out.println("Sending pause");
					PausePoll p = new PausePoll();
					sendMessage(p);
				}
			}
			else if(source.equals(btnResume))
			{
				if(this.state == State.Running && this.pState == PollState.Paused)
				{
					System.out.println("Sending resume");
					ResumePoll r = new ResumePoll();
					sendMessage(r);
				}
			}
			else if(source.equals(btnStop))
			{
				if(this.state == State.Running && this.pState != PollState.Closed)
				{
					System.out.println("Sending stop");
					StopPoll s = new StopPoll();
					sendMessage(s);
				}
			}
			else if(source.equals(btnAdd))
			{
				if(this.state == State.Connect && this.pState == PollState.None)
				{
					if(answers.size() <= 6)
					{
						Answer a = new Answer(answers.size() + 1);
						answers.add(a);
						answerPanel.add(a);
					}
				}
			}
			
			updateGUI();
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
	
	public void CreatePoll(String question, ArrayList<String> answers, String email)
	{
		auto = true;
		autoPoll = new CreatePoll();
		autoPoll.setQuestion(question);
		autoPoll.setAnswers(answers);
		autoEmail = email;
		
		Connect c = new Connect();
		c.setEmailAddress(autoEmail);
		sendMessage(c);
	}
	
	private void messageReceived(Object message)
	{
		if(this.state == State.Connect)
		{
			if(message instanceof ConnectReply)
			{
				this.state = State.Create;
				
				if(auto)
				{
					sendMessage(autoPoll);
				}
				else
				{
					CreatePoll cp = new CreatePoll();
					cp.setQuestion(txtQuestion.getText());
					
					// Add answers here!!!!!!
					// TO-DO: !!!
					
					ArrayList<String> answerList = new ArrayList<String>();
					for(Answer a : answers)
					{
						answerList.add(a.getTextValue());
					}
					
					cp.setAnswers(answerList);
					
					sendMessage(cp);
				}
			}
		}
		else if(this.state == State.Create)
		{
			if(message instanceof CreatePollReply)
			{
				CreatePollReply cpr = (CreatePollReply)message;
				lblPollId.setText(cpr.getPollId());
				
			    DefaultPieDataset data = new DefaultPieDataset();
			    ArrayList<String> answers = (ArrayList<String>) cpr.getAnswers();
			    for(int i=0; i<answers.size(); i++)
			    {
			    	data.setValue(answers.get(i), 0);
			    }
			    
			    pollName = cpr.getPollId() + " - " + cpr.getQuestion();
			    
				chart = ChartFactory.createPieChart(pollName,data,true,true,false);
				
				panel.setChart(chart);
				
				panel.setVisible(true);
				
				PollUpdate pu = new PollUpdate();
				pu.setResults(new ArrayList<Long>());
				pu.setPollId(((CreatePollReply) message).getPollId());
				updatePollFrame(pu);
				
				this.state = State.Running;
				this.pState = PollState.Open;
			}
		}
		else if(this.state == State.Running)
		{
			if(message instanceof PausePollReply)
			{
				this.pState = PollState.Paused;
				updateGUI();
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
				
				updatePollFrame(pu);
				
				String status = "Id = " + id + "\n";
				status += "Results" + "\n";
				status += "____________________" + "\n";
				status += "Option\tVotes" + "\n";
				int i = 1;
				for(Long s: results)
				{
					
					status += i + "\t" + s.toString() + "\n";
					i++;
				}
				
				pollStatus.setText(status);
			}
		}
		else if(this.state == State.Closed)
		{
			
		}
		
		updateGUI();
	}

	private void updatePollFrame(PollUpdate pu) {
		
		for(PollUpdateListener l : listeners)
		{
			l.updateReceived(pu);
		}
		
		System.out.println("updating poll");
		
	    DefaultPieDataset data = new DefaultPieDataset();

	    if(!auto)
	    {
		    for(int i=0; i<answers.size(); i++)
		    {
		    	data.setValue(answers.get(i).getTextValue(), pu.getResults().get(i));
		    	System.out.println("f:" + answers.get(i).getTextValue() + ", " + pu.getResults().get(i));
		    }
	    }
	    else
	    {
	    	if(pu.getResults().size() > 0)
	    	{
			    for(int i=0; i<autoPoll.getAnswers().size(); i++)
			    {
			    	data.setValue(autoPoll.getAnswers().get(i), pu.getResults().get(i));
			    	System.out.println("f:" + autoPoll.getAnswers().get(i) + ", " + pu.getResults().get(i));
			    }
	    	}
	    }
	    


		chart = ChartFactory.createPieChart(pollName,data,true,true,false);
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSimpleLabels(true); 
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));
		panel.setChart(chart);
	}

	private void updateGUI() {
		btnAdd.setEnabled(false);
		btnCreate.setEnabled(false);
		btnResume.setEnabled(false);
		btnPause.setEnabled(false);
		btnStop.setEnabled(false);
		pollStatus.setEditable(false);
		
		if(state == State.Connect)
		{
			btnCreate.setEnabled(true);
			
			if(answers.size() <= 6)
			{
				btnAdd.setEnabled(true);
			}
		}
		else if(state == State.Running)
		{
			btnStop.setEnabled(true);
		}
		
		if(pState == PollState.Open)
		{
			btnPause.setEnabled(true);
		}
		else if(pState == PollState.Closed)
		{
			
		}
		else if(pState == PollState.Paused)
		{
			btnResume.setEnabled(true);
		}
		else if(pState == PollState.None)
		{
			
		}
		
		answerPanel.revalidate();
		answerPanel.repaint();
		
		validate();
		repaint();
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

class Answer extends JPanel
{
	private JTextField answer = null;
	
	public Answer(int id)
	{
		this.add(new JLabel("Answer " + id));
		answer = new JTextField();
		answer.setPreferredSize(new Dimension(300,25));
		this.add(answer);
	}
	
	public String getTextValue()
	{
		return answer.getText();
	}
}
