package messages;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PollUpdate extends Message {

	private String pollId = null;
	private ArrayList<Long> results = null;
	
	public void setPollId(String id) {
		this.pollId = id;
	}
	
	public String getPollId()
	{
		return this.pollId;
	}
	
	public void setResults(ArrayList<Long> results)
	{
		this.results = results;
	}
	
	@XmlElementWrapper(name="results")
	@XmlElement(name = "result")
	public ArrayList<Long> getResults()
	{
		return this.results;
	}
	
	@Override
	public void setContextClass() {
		this.contextClass = PollUpdate.class;
	}

}
