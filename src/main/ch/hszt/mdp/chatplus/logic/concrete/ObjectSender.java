package ch.hszt.mdp.chatplus.logic.concrete;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class ObjectSender {

	private OutputStream output;

	
	/**
	 * Constructor
	 * 
	 * @param output
	 */
	
	public ObjectSender(OutputStream output) {
		this.output = output;
	}

	
	/**
	 * Send
	 * 
	 * Serialize an object to an XML output stream
	 * 
	 * @param obj
	 * @throws IOException
	 */
	
	public void send(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(baos);
		encoder.writeObject(obj);
		encoder.close();

		byte[] data = baos.toByteArray();
		int dataLength = data.length;
		DataOutputStream stream = new DataOutputStream(output);
		System.out.println(dataLength);
		stream.writeInt(dataLength);
		stream.write(data);
		stream.flush();
	}
	
}
