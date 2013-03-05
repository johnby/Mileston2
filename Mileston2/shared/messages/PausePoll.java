package messages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PausePoll extends Message {

	@Override
	public void setContextClass() {
		this.contextClass = PausePoll.class;
	}

}
