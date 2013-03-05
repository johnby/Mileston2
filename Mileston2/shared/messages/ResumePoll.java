package messages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResumePoll extends Message {

	@Override
	public void setContextClass() {
		this.contextClass = ResumePoll.class;
	}

}
