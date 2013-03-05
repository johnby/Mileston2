package messages;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;

@XmlRootElement
public abstract class Message {

	private static Class[] SupportedClasses = {Connect.class, ConnectReply.class, CreatePoll.class, CreatePollReply.class,
		PausePoll.class, PausePollReply.class, StopPoll.class, StopPollReply.class, ResumePoll.class, ResumePollReply.class,
		Quit.class, QuitReply.class, Error.class, PollUpdate.class};
	
	@SuppressWarnings("rawtypes")
	protected Class contextClass = Message.class;

	public Message()
	{
		setContextClass();
	}
	
	public abstract void setContextClass();

	/*
	 * Converts this class to an xml document using
	 * the JAXB framework and local context.
	 */
	public String marshal() throws JAXBException
	{		
		JAXBContext context = JAXBContext.newInstance(this.contextClass);

	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	    StringWriter stringWriter = new StringWriter();
		m.marshal(this, stringWriter);
		
		return stringWriter.toString();
	}
	
	/*
	 * Return this object from an xml string.
	 */
	public static Object unmarshal(String xmlString) throws JAXBException
	{		
		JAXBContext context = JAXBContext.newInstance(Message.getClasses(), null);
		
		StringReader xmlReader = new StringReader(xmlString);
		StreamSource xmlSource = new StreamSource(xmlReader);
		
		Unmarshaller u = context.createUnmarshaller();
		
		Object o = u.unmarshal(xmlSource);

		return o;
	}
	
	public static Class[] getClasses()
	{
		return Message.SupportedClasses;
	}
	
	public final static String EndOfMessageLine = "<End of Message />";

}
