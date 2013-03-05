package messages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PausePollReply extends Message {

	@Override
	public void setContextClass() {
		this.contextClass = PausePollReply.class;
	}

}
