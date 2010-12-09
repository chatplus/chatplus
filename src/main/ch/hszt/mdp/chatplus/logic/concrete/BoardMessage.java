package ch.hszt.mdp.chatplus.logic.concrete;

import java.io.Serializable;

import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;
import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

/***
 * A demo implementation of a message
 * that implements communication towards server as well as towards client.
 * This message represents a simple text-message on a specific chatboard;
 * 
 * @author sfrick
 *
 */
public class BoardMessage implements IServerMessage, IClientMessage, Serializable {
	
	public String message;
	public String sender;
	public String boardName;
	
	@Override	
	public IServerPeer getServerSource() {
		return null;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getBoardName() {
		return boardName;
	}
	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}
	@Override
	public void process(IClientContext context) {
		context.processBoardChatMessage(boardName, sender, message);
	}
	@Override
	public void setServerSource(IServerPeer peer) {
	}
	
	
	@Override
	public IClientPeer getClientSource() {
		return null;
	}
	@Override
	public void getClientSource(IClientPeer peer) {
	}
	@Override
	public void process(IServerContext context) {
		context.publishBoardChatMessage(boardName, sender, message);
	}
	
}
