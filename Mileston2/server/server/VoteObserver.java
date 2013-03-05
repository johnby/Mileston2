package server;

import poll.Poll;

public class VoteObserver extends Thread {

	private Poll observingPoll = null;
	private AdminListener adminListener = null;
	private boolean active = false;
	
	public VoteObserver(Poll poll, AdminListener adminListener)
	{
		this.observingPoll = poll;
		this.adminListener = adminListener;
		this.active = true;
	}
	
	public void run()
	{
		while(this.active)
		{
			if(this.observingPoll.hasChanged())
			{
				adminListener.pollChanged();
			}
			
			try {
				this.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void quit()
	{
		this.active = false;
	}
	
}
