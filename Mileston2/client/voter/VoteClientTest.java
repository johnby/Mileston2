package voter;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import junit.framework.Assert;

import messages.PollUpdate;

import org.junit.Test;

import admin.AdminClient;
import admin.PollUpdateListener;


public class VoteClientTest implements PollUpdateListener {

	private int votePort = 7778;
	private String pollId = "37520";
	private int options = 3;

	public static int defaultPort = 9999;
	private PollUpdate lastPollUpdate = null;
	
	@Test
	public void testVote() throws IOException
	{
		AdminClient client = new AdminClient("localhost", VoteClientTest.defaultPort);
		client.setVisible(true);
		client.addPollUpdateListener(this);
		ArrayList<String> answers = new ArrayList<String>();
		answers.add("Yes");
		answers.add("No");
		client.createPoll("Is this test okay?", answers, "");
		
		System.out.println("Waiting...");
		
		while(lastPollUpdate == null)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		pollId = lastPollUpdate.getPollId();
		
		for(int i=0; i <200; i++)
		{
			vote(Integer.toString(i), pollId, 1);
			try {
				Thread.sleep(45);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int i=200; i <250; i++)
		{
			vote(Integer.toString(i), pollId, 2);
			try {
				Thread.sleep(45);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertEquals((long)lastPollUpdate.getResults().get(0), 200L);
		Assert.assertEquals((long)lastPollUpdate.getResults().get(1), 50L);
	}
	
	@Test
	public void testMultipleRandomVotes() throws IOException
	{
		AdminClient client = new AdminClient("localhost", VoteClientTest.defaultPort);
		client.setVisible(true);
		client.addPollUpdateListener(this);
		ArrayList<String> answers = new ArrayList<String>();
		answers.add("Yes");
		answers.add("No");
		answers.add("Maybe");
		client.createPoll("Second: Is this test okay?", answers, "");
		
		options = answers.size();
		
		System.out.println("Waiting...");
		
		while(lastPollUpdate == null)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		pollId = lastPollUpdate.getPollId();
		
		Random r = new Random();
		
		Hashtable<Integer, Integer> checker = new Hashtable<Integer, Integer>();
		
		for(int i=0; i<1000; i++)
		{
			int option = r.nextInt(options) + 1;
			vote(Integer.toString(i), pollId, (long)option);
			
			int o = 0;
			if(checker.containsKey(option))
			{
				o = checker.get(option);
			}
			checker.put(option, o+1);
			
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertEquals((long)lastPollUpdate.getResults().get(0), (long)checker.get(1));
		Assert.assertEquals((long)lastPollUpdate.getResults().get(1), (long)checker.get(2));
		Assert.assertEquals((long)lastPollUpdate.getResults().get(2), (long)checker.get(3));
		

		

		
		
	}
	
	@Test
	public void testDuplicateVotes() throws IOException
	{
		AdminClient client = new AdminClient("localhost", VoteClientTest.defaultPort);
		client.setVisible(true);
		client.addPollUpdateListener(this);
		ArrayList<String> answers = new ArrayList<String>();
		answers.add("Yes");
		answers.add("No");
		answers.add("Maybe");
		client.createPoll("Second: Is this test okay?", answers, "");
		
		Hashtable<Integer, Integer> voters = new Hashtable<Integer, Integer>();
		
		options = answers.size();
		
		System.out.println("Waiting...");
		
		while(lastPollUpdate == null)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		pollId = lastPollUpdate.getPollId();
		
		Random r = new Random();
		
		for(int j=0; j<500; j++)
		{

			
			int i = r.nextInt(100);
			
			
			int option = r.nextInt(options) + 1;
			vote(Integer.toString(i), pollId, (long)option);
			
			//
			voters.put(i, option);
			
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Hashtable<Integer, Integer> checker = new Hashtable<Integer,Integer>();
		for(Integer key : voters.keySet())
		{
			int v = voters.get(key);
			
			if(checker.containsKey(v))
			{
				int value = checker.get(v);
				checker.put(v, value+1);
			}
			else
			{
				checker.put(v, 1);
			}
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertEquals((long)lastPollUpdate.getResults().get(0), (long)checker.get(1));
		Assert.assertEquals((long)lastPollUpdate.getResults().get(1), (long)checker.get(2));
		Assert.assertEquals((long)lastPollUpdate.getResults().get(2), (long)checker.get(3));
	}
	
	private void vote(String id, String pollId, long selection)
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
			sendPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getLocalHost() , this.votePort);
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		return sendPacket;
	}

	@Override
	public void updateReceived(PollUpdate update) {
		// TODO Auto-generated method stub
		lastPollUpdate = update;
	}

}
