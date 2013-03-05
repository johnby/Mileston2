package messages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StopPollReply extends Message {

	@Override
	public void setContextClass() {
		this.contextClass = StopPollReply.class;
	}

}
