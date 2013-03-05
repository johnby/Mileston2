package messages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Quit extends Message {

	@Override
	public void setContextClass() {
		this.contextClass = Quit.class;
	}

}
