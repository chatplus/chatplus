package ch.hszt.mdp.chatplus.logic.concrete.message;

import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

public class UserStatusMessage implements IServerMessage {

	private String username;
	private String board = null;
	private boolean isLoggedIn;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}	
	
	public String getBoard() {
		return board;
	}
	public void setBoard(String board) {
		this.board = board;
	}
	
	public boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	@Override
	public IServerPeer getServerSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void process(IClientContext context) {
		if(board == null)
			context.notifyUserStatusChange(username, isLoggedIn);
		else
			context.notifyUserStatusChange(board, username, isLoggedIn);			
	}

	@Override
	public void setServerSource(IServerPeer peer) {
		// TODO Auto-generated method stub
		
	}

}
