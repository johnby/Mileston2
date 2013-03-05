package messages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StopPoll extends Message {

	@Override
	public void setContextClass() {
		this.contextClass = StopPoll.class;
	}

}
