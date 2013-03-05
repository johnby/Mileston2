package messages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Error extends Message {

	private String errorMessage = "";
	
	@Override
	public void setContextClass() {
		this.contextClass = Error.class;
	}
	
	public void setErrorMessage(String err)
	{
		this.errorMessage = err;
	}
	
	public String getErrorMessage()
	{
		return this.errorMessage;
	}

}
