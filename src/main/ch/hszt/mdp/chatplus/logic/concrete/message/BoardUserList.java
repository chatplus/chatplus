package ch.hszt.mdp.chatplus.logic.concrete.message;

import java.util.LinkedList;

import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

public class BoardUserList implements IServerMessage {

	LinkedList<String> usernames = new LinkedList<String>();
	String boardName;

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public LinkedList<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(LinkedList<String> usernames) {
		this.usernames = usernames;
	}

	@Override
	public IServerPeer getServerSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void process(IClientContext context) {
		for (String username : usernames) {
			context.notifyUserStatusChange(boardName, username, true);
		}
	}

	@Override
	public void setServerSource(IServerPeer peer) {
		// TODO Auto-generated method stub

	}

}
