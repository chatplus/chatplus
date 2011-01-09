package ch.hszt.mdp.chatplus.logic.concrete;

import java.util.LinkedList;

import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;

public class ClientInformation {
	public boolean isLoggedIn;
	public String userName;
	public IClientPeer peer;
	public LinkedList<String> currentBoards = new LinkedList<String>();

	public ClientInformation(IClientPeer peer) {
		this.peer = peer;
	}
}
