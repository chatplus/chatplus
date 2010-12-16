package ch.hszt.mdp.chatplus.logic.concrete;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ObjectSender {

	private OutputStream output;
	
	public ObjectSender(OutputStream output) {
		this.output = output;
	}
	
	public void send(Object obj) throws IOException
	{					
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(baos);		
		encoder.writeObject(obj);	
		encoder.close();
		
		byte[] data = baos.toByteArray();
		int dataLength = data.length;
		output.write(dataLength);
		output.write(data);
		output.flush();
	}
}
