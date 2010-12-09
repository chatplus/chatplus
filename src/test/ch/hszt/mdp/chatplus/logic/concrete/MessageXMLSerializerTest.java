package ch.hszt.mdp.chatplus.logic.concrete;


import org.custommonkey.xmlunit.Diff;
import org.junit.Before;
import org.junit.Test;
import  static org.junit.Assert.*;
import org.xml.sax.InputSource;

import ch.hszt.mdp.chatplus.logic.concrete.BoardMessage;
import ch.hszt.mdp.chatplus.logic.concrete.MessageXMLSerializer;



public class MessageXMLSerializerTest {
	
	private BoardMessage dummyMessage;
	private MessageXMLSerializer impl;
	private String expected;
	private String written;
	
	
	@Before
	public void init(){
		dummyMessage = new BoardMessage();
		dummyMessage.setBoardName("TechTalk");
		dummyMessage.setSender("client1");
		dummyMessage.setMessage("Hallo");
		
		impl = new MessageXMLSerializer();
		
		expected = "src/test/ch/hszt/mdp/chatplus/logic/concrete/expected.xml";
		written = "src/test/ch/hszt/mdp/chatplus/logic/concrete/written.xml";
	}
	
	@Test
	public void isSerialized() throws Exception{
		impl.write(dummyMessage, written);
		InputSource xmlExpected = new InputSource(expected);
		InputSource xmlWritten = new InputSource(written);
		Diff myDiff = new Diff(xmlExpected, xmlWritten);
		assertTrue(myDiff.similar());
	}
	
	@Test
	public void isDeserialized(){
		BoardMessage o = (BoardMessage)impl.read(written);
		assertTrue(o.getBoardName().equals(dummyMessage.getBoardName())
				&& o.getMessage().equals(dummyMessage.getMessage())
				&& o.getSender().equals(dummyMessage.getSender()));
	}

}
