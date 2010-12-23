package ch.hszt.mdp.chatplus.logic.concrete;

import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;
import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

public class SimpleMessage implements IClientMessage, IServerMessage {

	String sender;
	String message;

	
	/**
	 * Process
	 * 
	 * Execute a message in a server context
	 * 
	 * @param 	context
	 */
	
	@Override
	public void process(IServerContext context) {
		context.publishSimpleMessage(sender, message);

	}

	
	/**
	 * Process
	 * 
	 * Execute a message in a client context
	 * 
	 * @param 	context
	 */
	
	@Override
	public void process(IClientContext context) {
		context.displayChatMessage(sender, message);
	}

	
	/**
	 * Serialize
	 * 
	 * not yet implemented
	 */
	
	@Override
	public Object serialize() {
		return null;
	}
	
	
	/*
	 * Getters and setters 
	 */
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public IClientPeer getClientSource() {
		return null;
	}

	@Override
	public void getClientSource(IClientPeer peer) {
	}
	
	@Override
	public IServerPeer getServerSource() {
		return null;
	}
	
	@Override
	public void setServerSource(IServerPeer peer) {
	}

	
	/*
	 * public void write(Object o, String filename){ try{ XMLEncoder encoder =
	 * new XMLEncoder( new BufferedOutputStream( new
	 * FileOutputStream(filename))); encoder.writeObject(o); encoder.close();
	 * }catch(IOException e){ e.printStackTrace(); } }
	 * 
	 * 
	 * public Object read(String filename){ try{ XMLDecoder decoder = new
	 * XMLDecoder( new BufferedInputStream( new FileInputStream(filename)));
	 * Object o = decoder.readObject(); decoder.close(); return o; }catch
	 * (FileNotFoundException e){ e.printStackTrace(); } return null; }
	 */

}
