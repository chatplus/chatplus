package ch.hszt.mdp.chatplus.logic.contract.peer;

import ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;

/***
 * Simple interface for the communication towards the server
 */
public interface IServerPeer {
	public void send(IClientMessage message);
}
