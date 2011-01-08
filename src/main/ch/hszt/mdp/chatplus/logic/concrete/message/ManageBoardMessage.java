package ch.hszt.mdp.chatplus.logic.concrete.message;

import ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;

public class ManageBoardMessage implements IClientMessage{

	private IClientPeer peer;
	
	private String username, boardName;
	private boolean join;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public boolean isJoin() {
		return join;
	}

	public void setJoin(boolean join) {
		this.join = join;
	}

	@Override
	public IClientPeer getClientSource() {
		return peer;
	}

	@Override
	public void process(IServerContext context) {
		context.manageBoardSubscription(username, boardName, join, peer);
	}

	@Override
	public void setClientSource(IClientPeer peer) {
		this.peer = peer;
	}

}
