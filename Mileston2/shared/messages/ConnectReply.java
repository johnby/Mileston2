package messages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConnectReply extends Message implements isSuccessfulInterface {
	
	private String emailAddress = null;
	private boolean successful = false;
	
	public ConnectReply()
	{
	}
	
	@Override
	public void setContextClass() {
		this.contextClass = ConnectReply.class;
	}

	public void setEmailAddress(String email)
	{
		this.emailAddress = email;
	}
	
	public String getEmailAddress()
	{
		return this.emailAddress;
	}

	public void setSuccessful(boolean successful)
	{
		this.successful = successful;
	}
	
	@Override
	public boolean isSuccessful() {
		return this.successful;
	}
}
