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
	String board = null;

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

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	@Override
	public void process(IServerContext context) {
		if (board == null)
			context.publishSimpleMessage(sender, message);
		else
			context.publishBoardMessage(sender, message, board);
	}

	@Override
	public void process(IClientContext context) {
		if (board == null)
			context.displayChatMessage(sender, message);
		else
			context.displayChatMessage(sender, message, board);
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
