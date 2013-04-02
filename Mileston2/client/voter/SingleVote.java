package voter;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.Test;

public class SingleVote {

	private int votePort = 7778;
	
	@Test
	public void test() {
		String pollId = "45550";
		
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

}
