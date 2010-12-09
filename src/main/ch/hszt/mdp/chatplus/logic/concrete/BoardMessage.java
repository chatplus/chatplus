package main.ch.hszt.mdp.chatplus.logic.concrete;

import main.ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import main.ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import main.ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;
import main.ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import main.ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;
import main.ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

/***
 * A demo implementation of a message
 * that implements communication towards server as well as towards client.
 * This message represents a simple text-message on a specific chatboard;
 * 
 * @author sfrick
 *
 */
public class BoardMessage implements IServerMessage, IClientMessage {
	
	public String message;
	public String sender;
	public String boardName;
	@Override
	
	public IServerPeer getServerSource() {
		return null;
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
