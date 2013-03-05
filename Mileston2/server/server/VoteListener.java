package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import messages.Connect;

public class VoteListener extends Thread {

	private int port = 7778;
	private boolean active = true;
	
	private DatagramSocket recSocket = null;
	
	public VoteListener(int port) throws SocketException {
		this.port = port;
		recSocket = new DatagramSocket(this.port);
	}
	
	public void run()
	{
		while(this.active){
			System.out.println("listening");
			try {
				byte data[] = new byte[200];
			    DatagramPacket recPacket = new DatagramPacket(data, data.length);
			    
				recSocket.receive(recPacket);
				
				System.out.println("Server: VoteListener: message received.");
				
				String ip = recPacket.getAddress().toString();
				String contents = new String(recPacket.getData(),0,recPacket.getLength());
				
				String[] parser = contents.split(" ");
				if(parser.length == 2)
				{
					String pollId = parser[0];
					long selection = Long.parseLong(parser[1]);
					
					ConnectionListener.getConnectionListener().vote(ip, pollId, selection);
				}
				else if(parser.length == 3)
				{
					// Debug mode!
					String id = parser[0];
					String pollId = parser[1];
					long selection = Long.parseLong(parser[2]);
					
					ConnectionListener.getConnectionListener().vote(id, pollId, selection);
				}
				else
				{
					System.out.println("Parsing error in vote.");
				}
				
				
				
			} catch (IOException e) {
				
				System.out.println("error");
				
				if(this.recSocket.isClosed())
				{
					this.active = false;
				}
				else
				{
					e.printStackTrace();
				}
			}
		}
	}
		
	public void quit()
	{
		this.active = false;
		recSocket.close();
	}
}
