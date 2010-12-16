package ch.hszt.mdp.chatplus.logic.concrete;

import java.util.ArrayList;

import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

/***
 * A demo chatplus client to test the abstract communication behaviour
 * @author sfrick
 */
/*
public class ChatPlusClient implements IClientContext {

	public IServerPeer serverPeer;
	public String name;
	
	public ArrayList<IServerMessage> threadSafeDummyMessageQueue = new ArrayList<IServerMessage>();
	
	public boolean isInterrupted = false;
	
	public void Run()
	{
		while(!isInterrupted)
		{
			for (IServerMessage message : threadSafeDummyMessageQueue) {
				System.out.println("[Application]\t[      ]\t[ChatPlusClient]\tProcessing IServerMessage");				
				message.process(this);
			}
			break;
		}
	}
	
	@Override
	public void processBoardChatMessage(String sender, String message) {
		System.out.println("[Application]\t[      ]\t[ChatPlusClient]\tClient " + name + " got message \"" + message + "\" from sender " + sender);
	}
	
	public void sendNewBoardMessage(String sender, String message) {

		System.out.println("[Application]\t[      ]\t[ChatPlusClient]\t" + name + " passing NewBoardMessage to IServerPeer");
	
		BoardMessage msg = new BoardMessage();
		msg.sender = sender;
		msg.message = message;
		
		serverPeer.send(msg);
	}

	@Override
	public void displayChatMessage(String sender, String message) {
		// TODO Auto-generated method stub
		
	}
}
*/
