import java.io.IOException;

import server.ConnectionListener;
import server.VoteListener;


public class ServerLauncher {

	public static int defaultPort = 9999;
	public static int defaultVotePort = 7778;
	private static ConnectionListener l = null;
	
	public static void main(String[] args)
	{
		System.out.println("Server started...");
		
		try {
			l = new ConnectionListener(defaultPort);
			Thread t = new Thread(l);
			
			VoteListener vListener = new VoteListener(ServerLauncher.defaultVotePort);
			
			t.start();			
			vListener.start();
			
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				l.quit();
			}
			
			try {
				vListener.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				vListener.quit();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ConnectionListener getConnectionListener()
	{
		return ServerLauncher.l;
	}

}
