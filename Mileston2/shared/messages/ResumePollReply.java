package messages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResumePollReply extends Message {

	@Override
	public void setContextClass() {
		this.contextClass = ResumePollReply.class;
	}

}
