package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ConnectionListener implements Runnable {

	private ServerSocket serverSocket = null;
	private int listeningPort = 7777;
	private boolean active = true;
	private ArrayList<AdminListener> threads = null;
	
	public static ConnectionListener activeConnectionListener = null;
	
	public static ConnectionListener getConnectionListener()
	{
		return ConnectionListener.activeConnectionListener;
	}
	
	public ConnectionListener(int port) throws IOException
	{
		this.listeningPort = port;
		serverSocket = new ServerSocket(this.listeningPort);
		threads = new ArrayList<AdminListener>();
		ConnectionListener.activeConnectionListener = this;
	}

	public void run()
	{
		while(active)
		{
			try {
				Socket newSocket = serverSocket.accept();
				
				AdminListener newAdminListener = new AdminListener(newSocket);
				threads.add(newAdminListener);
				newAdminListener.start();

				} catch (SocketException e2)
			{
				active = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void quit()
	{
		for(ListenerThread t : threads)
		{
			t.disconnect();
		}
		
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.active = false;
	}

	public void vote(String userId, String pollId, long selection)
	{
		System.out.println("C.Listener rec vote: " + userId + " , " + pollId + " " + selection);
		for(AdminListener a: threads)
		{
			if(a.getPollId().equals(pollId))
			{
				System.out.println("C.Listener found poll for vote.");
				a.vote(userId, pollId, selection);
			}
		}
	}
}
