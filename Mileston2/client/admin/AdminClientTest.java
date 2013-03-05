package admin;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import messages.*;

import org.junit.Test;

public class AdminClientTest {

	private BufferedReader reader = null;
	private PrintWriter writer = null;
	private Socket socket = null;
	
	@Test
	public void connectToServer() throws IOException, JAXBException
	{
		try {
			setupConnection();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertNotNull(this.socket);
		
		Connect c = new Connect();
		c.setEmailAddress("email.test.ca");
		
		sendMessage(c);
		
		Object o = waitForMessage();
		
		assert(o instanceof ConnectReply);
	}
	
	@Test
	public void createPoll() throws IOException, JAXBException
	{
		try {
			setupConnection();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertNotNull(this.socket);
		
		Connect c = new Connect();
		c.setEmailAddress("email.test.ca");
		
		sendMessage(c);
		
		Object o = waitForMessage();
		
		assert(o instanceof ConnectReply);
		
		ConnectReply cr = (ConnectReply)o;
		assertTrue(cr.isSuccessful());
		
		CreatePoll cp = new CreatePoll();
		cp.setQuestion("How old is the oldest monkey?");
		cp.addAnswer("10");
		cp.addAnswer("a million!");
		sendMessage(cp);
		
		o = null;
		try {
			o = waitForMessage();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		assertTrue(o instanceof CreatePollReply);
	}
	
	@Test
	public void createPollPauseResumeStop() throws IOException, JAXBException
	{
		try {
			setupConnection();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertNotNull(this.socket);
		
		Connect c = new Connect();
		c.setEmailAddress("email.test.ca");
		
		sendMessage(c);
		
		Object o = waitForMessage();
		
		assert(o instanceof ConnectReply);
		
		ConnectReply cr = (ConnectReply)o;
		assertTrue(cr.isSuccessful());
		
		CreatePoll cp = new CreatePoll();
		cp.setQuestion("How old is the oldest monkey?");
		cp.addAnswer("10");
		cp.addAnswer("a million!");
		sendMessage(cp);
		
		o = null;
		try {
			o = waitForMessage();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		assertTrue(o instanceof CreatePollReply);
		
		
		PausePoll pause = new PausePoll();
		ResumePoll resume = new ResumePoll();
		StopPoll stop = new StopPoll();
		
		sendMessage(pause);
		o = waitForMessage();
		assertTrue(o instanceof PausePollReply);
		
		sendMessage(resume);
		o = waitForMessage();
		assertTrue(o instanceof ResumePollReply);
		
		sendMessage(stop);
		o = waitForMessage();
		assertTrue(o instanceof StopPollReply);
		
		sendMessage(new Quit());
		o = waitForMessage();
		assertTrue(o instanceof QuitReply);
		
	}
	
	
	@Test
	public void createPollAndLeaveOpen() throws IOException, JAXBException
	{
		try {
			setupConnection();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertNotNull(this.socket);
		
		Connect c = new Connect();
		c.setEmailAddress("email.test.ca");
		
		sendMessage(c);
		
		Object o = waitForMessage();
		
		assert(o instanceof ConnectReply);
		
		ConnectReply cr = (ConnectReply)o;
		assertTrue(cr.isSuccessful());
		
		CreatePoll cp = new CreatePoll();
		cp.setQuestion("How old is the oldest monkey?");
		cp.addAnswer("10");
		cp.addAnswer("a million!");
		sendMessage(cp);
		
		o = null;
		try {
			o = waitForMessage();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		assertTrue(o instanceof CreatePollReply);
		
		while(true)
		{
			o = waitForMessage();
			if(o instanceof PollUpdate)
			{
				PollUpdate pu = (PollUpdate)o;
				String id = pu.getPollId();
				ArrayList<Long> results = pu.getResults();
				
				System.out.println("Id = " + id);
				int i = 0;
				for(Long s: results)
				{
					System.out.println(i + " " + s.toString());
					i++;
				}
			}
		}
	}
	
	private Object waitForMessage() throws IOException, JAXBException
	{
		System.out.println("waiting");
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
	
	private void setupConnection() throws UnknownHostException, IOException
	{
		this.socket = new Socket("localhost", 9999);
		
		// setup reader
		InputStream inputstream = this.socket.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        reader = new BufferedReader(inputstreamreader);
        
        // setup writer
        OutputStream outputStream = this.socket.getOutputStream();
        OutputStreamWriter osr = new OutputStreamWriter(outputStream);
        writer = new PrintWriter(socket.getOutputStream(), true);
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
