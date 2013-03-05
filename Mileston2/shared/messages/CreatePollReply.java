package messages;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreatePollReply extends Message implements isSuccessfulInterface {

	private boolean successful = false;
	
	private String question = null;
	private ArrayList<String> answers = null;
	private String pollId = null;
	
	public CreatePollReply()
	{
	}

	public String getQuestion()
	{
		return this.question;
	}
	
	public void setQuestion(String question)
	{
		this.question = question;
	}
	
	@XmlElementWrapper(name="answers")
	@XmlElement(name = "answer")
	public List<String> getAnswers()
	{
		return this.answers;
	}
	
	public void setAnswers(List<String> answers)
	{
		this.answers = (ArrayList<String>) answers;
	}
	
	public void addAnswer(String answer)
	{
		if(this.answers == null)
		{
			this.answers = new ArrayList<String>();
		}

		this.answers.add(answer);
	}
	
	@Override
	public boolean isSuccessful() {
		return this.successful;
	}

	public void setSuccessful(boolean successful)
	{
		this.successful = successful;
	}
	
	@Override
	public void setContextClass() {
		this.contextClass = CreatePollReply.class;
	}

	public void setPollId(String id) {
		this.pollId = id;
	}
	
	public String getPollId()
	{
		return this.pollId;
	}

}
