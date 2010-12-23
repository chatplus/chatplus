package ch.hszt.mdp.chatplus.logic.concrete.message;

import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

public class UserStatusMessage implements IServerMessage {

	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public IServerPeer getServerSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void process(IClientContext context) {
		context.notifyUserStatusChange(username, true);	
	}

	@Override
	public void setServerSource(IServerPeer peer) {
		// TODO Auto-generated method stub
		
	}

}
