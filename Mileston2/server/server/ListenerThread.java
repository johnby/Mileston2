package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.xml.bind.JAXBException;


public abstract class ListenerThread extends Thread {
	
	protected Socket socket = null;
	protected BufferedReader reader = null;
	protected PrintWriter writer = null;
	
	public ListenerThread(Socket socket) throws IOException
	{
		this.socket = socket;
		
		// setup reader
		InputStream inputstream = this.socket.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        reader = new BufferedReader(inputstreamreader);
        
        // setup writer
        OutputStream outputStream = this.socket.getOutputStream();
        OutputStreamWriter osr = new OutputStreamWriter(outputStream);
        writer = new PrintWriter(socket.getOutputStream(), true);
	}
	
	protected void sendMessage(messages.Message m)
	{
		synchronized (this.writer) {
			try {
				this.writer.println(m.marshal() + "\n" + messages.Message.EndOfMessageLine);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected abstract void quit();
	
	public void disconnect()
	{
		quit();
		
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
