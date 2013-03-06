package poll;

import java.util.ArrayList;
import java.util.Hashtable;

public class Poll {

	public enum State { Open, Paused, Closed };
	
	public State state = State.Open;
	private String question = null;
	private ArrayList<String> answers = null;
	private ArrayList<Long> voteCount = null;
	private Hashtable<String, Long> votes = null;
	private String pollId = null;
	private boolean hasChanged = false;
	
	public Poll(String id, String question, ArrayList<String> answers)
	{
		this.pollId = id;
		this.question = question;
		this.answers = answers;
		this.votes = new Hashtable<String, Long>();
		this.voteCount = new ArrayList<Long>();
		
		if(answers == null)
		{
			answers = new ArrayList<String>();
		}
		
		for(long i=0; i<answers.size(); i++)
			this.voteCount.add(0L);

		this.hasChanged = true;
	}
	
	public String getQuestion()
	{
		return this.question;
	}
	
	public ArrayList<String> getAnswers()
	{
		return this.answers;
	}
	
	public void vote(String user, long vote)
	{
		synchronized (this) {
			this.hasChanged = true;
		}
		
		vote = vote - 1;
		
		// Error vote selection out of range
		if(vote < 0 || vote >= voteCount.size())
		{
			return;
		}
		
		// user has already voted
		if(this.votes.containsKey(user))
		{
			long votedFor = this.votes.get(user);
			
			if(votedFor != vote)
			{
				// reduce votedFor and increase vote
				voteCount.set((int)votedFor, voteCount.get((int)votedFor) - 1);
				voteCount.set((int)vote, voteCount.get((int)vote)+1);
				
				// add user and vote
				votes.put(user, vote);
			}
		}
		else
		{
			// does not contain this user already
			votes.put(user, vote);
			
			// increment vote count
			voteCount.set((int)vote, voteCount.get((int)vote) + 1);
		}
	}
	
	public ArrayList<Long> getResults()
	{
		return this.voteCount;
	}

	public void pause() {
		if(this.state != State.Closed)
			this.state = State.Paused;
	}
	
	public void resume() {
		if(this.state != State.Closed)
			this.state = State.Open;
	}
	
	public void stop() {
		this.state = State.Closed;
	}
	
	public State getState()
	{
		return this.state;
	}

	public void setId(String id)
	{
		this.pollId = id;
	}
	
	public String getId() {
		return this.pollId;
	}
	
	public boolean hasChanged()
	{
		synchronized (this) {
			boolean old = this.hasChanged;
			this.hasChanged = false;
			return old;
		}
	}

}
