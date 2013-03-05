package messages;

public class MessageReply extends Message {

	protected boolean successful = false;
	
	public MessageReply()
	{

	}

	@Override
	public void setContextClass() {
		this.contextClass = MessageReply.class;
	}
	
	protected boolean isSuccessful()
	{
		return this.successful;
	}
	
	protected void setSuccessful(boolean successful)
	{
		this.successful = successful;
	}
	
}
