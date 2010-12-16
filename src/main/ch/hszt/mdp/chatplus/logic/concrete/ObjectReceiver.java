package ch.hszt.mdp.chatplus.logic.concrete;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ObjectReceiver {

	InputStream input;
	
	public ObjectReceiver(InputStream input) {
		this.input = input;
	}

	public Object receive() throws IOException
	{
		DataInputStream stream = new DataInputStream(input);
		int dataLength = stream.readInt();
		byte[] data = new byte[dataLength];
		stream.read(data);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		
		XMLDecoder decoder = new XMLDecoder(bais);
		Object obj = decoder.readObject();		
		bais.close();
		return obj;
	}
}
