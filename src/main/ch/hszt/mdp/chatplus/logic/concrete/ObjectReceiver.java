package ch.hszt.mdp.chatplus.logic.concrete;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ObjectReceiver {

	InputStream input;
	
	public ObjectReceiver(InputStream input) {
		this.input = input;
	}

	public Object receive() throws IOException
	{
		int dataLength = input.read();
		byte[] data = new byte[dataLength];
		input.read(data,0,dataLength);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		
		XMLDecoder decoder = new XMLDecoder(bais);
		Object obj = decoder.readObject();		
		return obj;
	}
}
