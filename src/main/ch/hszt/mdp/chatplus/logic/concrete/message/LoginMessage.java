package ch.hszt.mdp.chatplus.logic.concrete.message;

import java.util.LinkedList;

import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;
import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

public class LoginMessage implements IServerMessage, IClientMessage {
	
	private String username;
	private LinkedList<String> usernames = new LinkedList<String>();
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isAuthorised() {
		return isAuthorised;
	}
	public void setAuthorised(boolean isAuthorised) {
		this.isAuthorised = isAuthorised;
	}
	
	public LinkedList<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(LinkedList<String> usernames) {
		this.usernames = usernames;
	}

	private boolean isAuthorised;
	
	@Override
	public void process(IServerContext context) {
		context.requestLoginAuthorisation(username, clientSource);
	}
	@Override
	public void process(IClientContext context) {
		context.processLoginResponse(username, isAuthorised);
		for (String username : usernames) {
			context.notifyUserStatusChange(username, true);
		}
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
