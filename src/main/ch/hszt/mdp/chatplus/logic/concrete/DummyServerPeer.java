package ch.hszt.mdp.chatplus.logic.concrete;

import ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

/***
 * A demo implementation of the IServerPeer behaviour
 * A concrete IServerPeer will have to implement the network i/o towards the client
 * @author sfrick
 *
 *//*
public class DummyServerPeer implements IServerPeer{

	public ChatPlusServer server;
	@Override
	public void send(IClientMessage message) {
		System.out.println("[TCP\\IP]\t[S <- C]\t[DummyServerPeer]\tAdding IClientMessage to ChatPlusServer.threadSafeDummyMessageQueue");
		
		// That's where all the serializing and TCP/IP/Socket stuff must happen later
		// socket.open(ip);
		// socket.send(message.toByteArray());
		
		server.threadSafeDummyMessageQueue.add(message);
	}

}
*/