package messages;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.bind.JAXBException;

public class tester {

	/**
	 * @param args
	 * @throws JAXBException 
	 */
	public static void main(String[] args) throws JAXBException {

		CreatePoll p = new CreatePoll();
		p.setQuestion("Hello there?");
		ArrayList<String> a = new ArrayList<String>();
		a.add("1.dsfsdff");
		a.add("2.sdfsdfds");
		p.setAnswers(a);
		
		System.out.println(p.marshal());
		
		CreatePoll p2 = (CreatePoll) Message.unmarshal(p.marshal());
		System.out.println(p2.getQuestion());
		
		
		PollUpdate pu = new PollUpdate();
		pu.setPollId("testing");
		ArrayList<Long> h = new ArrayList<Long>();
		h.add(2L);
		h.add(5L);
		pu.setResults(h);
		
		PollUpdate npu = (PollUpdate) Message.unmarshal(pu.marshal());
		System.out.println(npu.getPollId());
		
	}

}
