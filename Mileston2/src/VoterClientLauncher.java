import java.io.IOException;

import voter.VoteClient;


public class VoterClientLauncher {

	public static void main(String[] args) throws IOException
	{
		VoteClient vClient = new VoteClient("localhost", ServerLauncher.defaultVotePort);
	}
	
}
