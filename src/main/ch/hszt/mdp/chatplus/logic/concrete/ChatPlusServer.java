package main.ch.hszt.mdp.chatplus.logic.concrete;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import main.ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import main.ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;
import main.ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;

/***
 * A demo chatplus server to test the abstract communication behaviour
 * @author sfrick
 */
public class ChatPlusServer implements IServerContext{
		
	public Dictionary<String, ArrayList<IClientPeer>> clientBoards = new Hashtable<String, ArrayList<IClientPeer>>();
	
	public ArrayList<IClientMessage> threadSafeDummyMessageQueue = new ArrayList<IClientMessage>();
	
	public boolean isInterrupted = false;
	
	public void Run()
	{
		while(!isInterrupted)
		{
			for (IClientMessage message : threadSafeDummyMessageQueue) {
				System.out.println("[Application]\t[      ]\t[ChatPlusServer]\tProcessing IClientMessage");				
				message.process(this);
			}
			break;
		}
	}
	
	@Override
	public void publishBoardChatMessage(String boardName, String sender, String message) {
		BoardMessage msg = new BoardMessage();
		msg.boardName = boardName;
		msg.sender = sender;
		msg.message = message;
		
		for(IClientPeer peer : clientBoards.get(boardName))
		{
			System.out.println("[Application]\t[      ]\t[ChatPlusServer]\tPassing IClientMessage to IClientPeer");	
			peer.send(msg);
		}
	}
}
