package email;



import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.*;
import java.util.Properties;
import javax.mail.*;

import voter.VoteClient;
import voter.VoteClientTest;

public class ReadMail extends Thread{
	private int vPort;
	
	public ReadMail(int vrt) throws IOException, InterruptedException
	{
		vPort = vrt;
		generateVotePacket("5rolm",1);
		
		VoteClient v = new VoteClient("localhost", 7778);
		this.sleep(10000);
		v.vote("a", "5rolm",1);
		//this.sleep(10000);
	}
	
  public static void main(String args[]) throws IOException, InterruptedException {
	  		
            ReadMail m = new ReadMail(7778);
  }
  
  public void run()
  {
	//VoteClientTest vot = new VoteClientTest(); // TO SEND THE VOTE
		
		
      Properties properties = System.getProperties();
      properties.setProperty("mail.store.protocol", "imaps");
          try {
              Session session = Session.getDefaultInstance(properties, null);
              //create session instance
              Store store = session.getStore("imaps");//create store instance
              store.connect("pop.gmail.com", "pollserverv2020@gmail.com", "pollsrv2020");
              //set your user_name and password
              System.out.println(store); 
              Folder inbox = store.getFolder("inbox");
              //set folder from where u wants to read mails
              inbox.open(Folder.READ_ONLY);//set access type of Inbox 
              
              Message messages[] = inbox.getMessages();// gets inbox messages
              System.out.println(messages.length);
              int j =0;
              for (int i = messages.length-1; i>0 ; i--) {
             	 if(messages[i].getFrom()[0].toString().equals("pollserverv2020@gmail.com"))
             	 {
             		 System.out.print(messages[i].getSubject().split("-")[0].toString());
             		 System.out.print("PeekaBooo");
             		 System.out.println(j);
             		 System.out.println(messages[i].getContent().toString());
             		 if(messages[i].getSubject().split("-")[0].toString().equals("5rolm"))
             		 {
             			long p = Integer.parseInt(messages[i].getContent().toString());
                		 vote(messages[i].getSubject().split("-")[1].toString(),messages[i].getSubject().split("-")[0].toString(),p,vPort);
      
             		 }
             		                         
             	 }
             	 /*
             System.out.println("------------ Message " + (i + 1) + " ------------");
             System.out.println("SentDate : " + messages[i].getSentDate()); //print sent date
             System.out.println("From : " + messages[i].getFrom()[0]); //print email id of sender
             System.out.println("Sub : " + messages[i].getSubject()); //print subject of email
             
             try
             {
                   Multipart mulpart = (Multipart) messages[i].getContent();
                   int count = mulpart.getCount();
                   for (int j = 0; j+1 < count; j++)
                  {
                       storePart(mulpart.getBodyPart(j));
                  }
             }
             catch (Exception ex)
             {
                  System.out.println("Exception arise at get Content");
                  ex.printStackTrace();
             }
             */
        }
        store.close();
   }
catch (Exception e) {
System.out.println(e);  
}  
  }
  public static void storePart(Part part) throws Exception
     {    
          InputStream input = part.getInputStream();
          if (!(input instanceof BufferedInputStream))
         {
              input = new BufferedInputStream(input);
          }
          int i;
         System.out.println("msg : ");
          while ((i = input.read()) != -1)
         {
         System.out.write(i);
    
         }
     }
  private void vote(String id, String pollId, long selection, int port)
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
			sendPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getLocalHost() , vPort);
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		return sendPacket;
	}
} 