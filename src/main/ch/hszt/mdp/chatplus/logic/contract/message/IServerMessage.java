package main.ch.hszt.mdp.chatplus.logic.contract.message;

import main.ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import main.ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

/***
 * Describes a message that must be processed in an IClientContext.
 * This kind of message is issued by the server towards a client.
 * @author sfrick
 *
 */
public interface IServerMessage {
	public void process(IClientContext context);
	
	public IServerPeer getServerSource();
	public void setServerSource(IServerPeer peer);
}
