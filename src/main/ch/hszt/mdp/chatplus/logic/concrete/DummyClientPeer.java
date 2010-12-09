package ch.hszt.mdp.chatplus.logic.concrete;

import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;

/***
 * A demo implementation of the IClientPeer behaviour
 * A concrete ClientPeer will have to implement the network i/o towards the server
 * @author sfrick
 *
 */
public class DummyClientPeer implements IClientPeer {

	public ChatPlusClient client;
	
	@Override
	public void send(IServerMessage message) {
		System.out.println("[TCP\\IP]\t[S -> C]\t[DummyClientPeer]\tPassing message to ChatPlusClient: " + client.name);
		
		// That's where all the serializing and TCP/IP/Socket stuff must happen later
		// socket.open(ip);
		// socket.send(message.toByteArray());
		
		client.threadSafeDummyMessageQueue.add(message);
	}

}
