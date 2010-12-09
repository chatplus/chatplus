package main.ch.hszt.mdp.chatplus.logic.contract.message;

import main.ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import main.ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;

/***
 * Describes a message that must be processed in an IServerContext
 * This kind of message is issued by a client towards the server.
 * @author sfrick
 *
 */
public interface IClientMessage {
	public void process(IServerContext context);
	
	public IClientPeer getClientSource();
	public void getClientSource(IClientPeer peer);
}
