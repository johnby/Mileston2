package server;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import messages.*;
import messages.Error;

import poll.Poll;

public class AdminListener extends ListenerThread {

	private static SecureRandom random = new SecureRandom();

	public String generatePollId(){
		return new BigInteger(128, random).toString(32).substring(0, 5);
	}
	
	public AdminListener(Socket socket) throws IOException {
		super(socket);
	}
	
	private Poll poll = null;
	public enum State { Connect, CreatePoll, PollRunning, PollClosed, Exit };
	private State state = State.Connect;
	private String email = null;
	private VoteObserver voteObserver = null;
	
	public void run()
	{
		while(this.state != State.Exit)
		{
			// listen for incoming data
			String xmlMessage = "";
			
			try {
				String line = null;
				while((line = this.reader.readLine())!=null)
				{
					System.out.println("State=" + this.state);
					System.out.println("lione: " + line);
					
					if(line.equals(Message.EndOfMessageLine))
					{
						System.out.println("Message rec.");
						System.out.println("To be marshal =" + xmlMessage);
						
						Object o = null;
						try {
							o = Message.unmarshal(new String(xmlMessage));						
						} catch (JAXBException e) {
							e.printStackTrace();
						}
						
						if(o != null)
						{
							System.out.println("Rec valid message.");
							
							// quit poll
							if(o instanceof Quit)
							{
								sendMessage(new QuitReply());
								sendResults();
								this.state = State.Exit;
							}

							if(this.state == State.Connect)
							{
								if(o instanceof Connect)
								{
									System.out.println("Rec connect message.");
									Connect c = (Connect)o;
									this.email = c.getEmailAddress();
									ConnectReply cr = new ConnectReply();
									cr.setSuccessful(true);
									cr.setEmailAddress(this.email);
									sendMessage(cr);
									this.state = State.CreatePoll;
								}
							}
							else if(this.state == State.CreatePoll)
							{
								if(o instanceof CreatePoll)
								{
									CreatePoll cp = (CreatePoll)o;
									String question = cp.getQuestion();
									ArrayList<String> answers = (ArrayList<String>) cp.getAnswers();
									this.poll = new Poll(generatePollId(), question, answers);
									this.state = State.PollRunning;
									
									CreatePollReply cpr = new CreatePollReply();
									cpr.setPollId(this.poll.getId());
									cpr.setQuestion(this.poll.getQuestion());
									cpr.setAnswers(this.poll.getAnswers());
									cpr.setSuccessful(true);
									sendMessage(cpr);
									
									this.voteObserver = new VoteObserver(this.poll, this);
									this.voteObserver.start();
									
									System.out.println("Created poll id=" + this.poll.getId());
								}
							}
							else if(this.state == State.PollRunning)
							{
								if(o instanceof PausePoll)
								{
									// pause poll
									this.poll.pause();
									sendMessage(new PausePollReply());
								}
								else if(o instanceof StopPoll)
								{
									// stop poll
									this.poll.stop();
									this.state = State.PollClosed;
									sendMessage(new StopPollReply());
								}
								else if(o instanceof ResumePoll)
								{
									// resume poll
									this.poll.resume();
									sendMessage(new ResumePollReply());
								}
		
								// if poll is closed (stopped) then send results to
								// email address and move on
								if(this.state == State.PollClosed)
								{
									sendResults();
									this.state = State.Exit;
								}
							}
							else
							{
								Error err = new Error();
								err.setErrorMessage("Invalid state.");
								sendMessage(err);
							}
						}
						
						xmlMessage = "";
					}
					else
					{
						System.out.println("l: " + line);
						xmlMessage += line + "\n";
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				sendResults();
				quit();
			}
		}
		
		if(this.voteObserver != null)
			this.voteObserver.quit();
	}

	private void sendResults() {

		String results = "vote failed";
		if(this.poll != null)
		{
			results = this.poll.getResultString();
		}
		
		System.out.println(this.email + " " + results);
	}
	
	public String getPollId()
	{
		if(this.poll != null)
		{
			return this.poll.getId();
		}
		else
		{
			return "XxXxX";
		}
	}

	@Override
	protected void quit() {
		this.state = State.Exit;
		if(this.voteObserver != null)
			this.voteObserver.quit();
	}

	public void vote(String userId, String pollId, long selection) {
		System.out.println("A.Listener rec vote.");
		
		if(this.poll != null)
			System.out.println("Poll State=" + this.poll.getState());

		if(this.poll != null)
		{
			if(this.poll.state == Poll.State.Open)
			{
				System.out.println("voting");
				this.poll.vote(userId, selection);
			}
		}
	}

	public void pollChanged() {
		System.out.println("Poll changed.");
		
		PollUpdate pu = new PollUpdate();
		pu.setPollId(this.poll.getId());
		pu.setResults(this.poll.getResults());
		sendMessage(pu);		
	}
}
