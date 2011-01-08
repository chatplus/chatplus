package ch.hszt.mdp.chatplus.logic.concrete.message;

import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;
import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

public class SimpleMessage implements IClientMessage, IServerMessage {

	String sender;
	String message;

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
	public void process(IServerContext context) {
		context.publishSimpleMessage(sender, message);
	}
	@Override
	public void process(IClientContext context) {
		context.displayChatMessage(sender, message);
	}
	
	private IServerPeer serverSource;
	private IClientPeer clientSource;
	
	@Override
	public IServerPeer getServerSource() {
		return serverSource;
	}
	@Override
	public void setServerSource(IServerPeer peer) {
		serverSource = peer;
	}
	@Override
	public IClientPeer getClientSource() {
		return clientSource;
	}
	@Override
	public void setClientSource(IClientPeer peer) {
		clientSource = peer;
	}
}
