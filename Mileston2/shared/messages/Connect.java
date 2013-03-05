package messages;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Connect extends Message {

	private String emailAddress = null;
	
	public Connect()
	{
	}
	
	@Override
	public void setContextClass() {
		this.contextClass = Connect.class;
	}

	@XmlElement
	public void setEmailAddress(String email)
	{
		this.emailAddress = email;
	}
	
	
	public String getEmailAddress()
	{
		return this.emailAddress;
	}
}
