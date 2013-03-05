package messages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QuitReply extends Message {

	@Override
	public void setContextClass() {
		this.contextClass = QuitReply.class;
	}

}
