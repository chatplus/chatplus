package main.ch.hszt.mdp.chatplus.logic.contract.peer;

import main.ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;

/***
 * Simple interface for the communication towards the server
 */
public interface IClientPeer {
	public void send(IServerMessage message);
}
