import java.io.IOException;

import admin.AdminClient;


public class AdminClientLauncher {

	public static void main(String[] args) throws IOException
	{
		AdminClient client = new AdminClient("localhost", ServerLauncher.defaultPort);
		client.setVisible(true);
	}
	
}
