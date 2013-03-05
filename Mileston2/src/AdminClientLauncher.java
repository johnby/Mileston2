import admin.AdminClient;


public class AdminClientLauncher {

	public static void main(String[] args)
	{
		AdminClient client = new AdminClient("localhost", ServerLauncher.defaultPort);
		client.setVisible(true);
	}
	
}
