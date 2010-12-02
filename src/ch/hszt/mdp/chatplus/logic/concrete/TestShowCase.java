package ch.hszt.mdp.chatplus.logic.concrete;

import java.util.ArrayList;

import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;

/***
 * A simple showcase to demonstrate base functionality of this application
 * @author sfrick
 *
 */
public class TestShowCase {

	public static void main(String[] args) {
		ChatPlusServer server = new ChatPlusServer();
		DummyServerPeer serverPeer = new DummyServerPeer();
		serverPeer.server = server;
		
		ChatPlusClient client1 = new ChatPlusClient();
		client1.name = "client1";
		DummyClientPeer clientPeer1 = new DummyClientPeer();
		clientPeer1.client = client1;
		client1.serverPeer = serverPeer;
		
		ChatPlusClient client2 = new ChatPlusClient();
		client2.name = "client2";
		DummyClientPeer clientPeer2 = new DummyClientPeer();
		clientPeer2.client = client2;
		client2.serverPeer = serverPeer;
		
		ChatPlusClient client3 = new ChatPlusClient();
		client3.name = "client3";
		DummyClientPeer clientPeer3 = new DummyClientPeer();
		clientPeer3.client = client3;
		client3.serverPeer = serverPeer;
		
		ArrayList<IClientPeer> currentClients = new ArrayList<IClientPeer>();
		currentClients.add(clientPeer1);
		currentClients.add(clientPeer2);
		currentClients.add(clientPeer3);
		server.clientBoards.put("TechTalk", currentClients);
		
		client1.sendNewBoardMessage("TechTalk", "client1", "Hello World!");
		
		server.Run();
		client1.Run();
		client2.Run();
		client3.Run();
		
	}

}
