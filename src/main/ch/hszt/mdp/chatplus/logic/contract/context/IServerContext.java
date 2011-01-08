package ch.hszt.mdp.chatplus.logic.contract.context;

import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;

/***
 * This interface describes the functionality a server must provide. By now it's
 * far from being complete; it will be extended step by step when implementing
 * the user stories.
 * 
 * @author sfrick
 * 
 */
public interface IServerContext {

	/***
	 * Publishes a board chat message (e.g to other chatclients on the board
	 */

	public void publishBoardMessage(String sender, String message, String boardName);	
	public void publishSimpleMessage(String sender, String message);

	public void manageBoardSubscription(String username, String boardName, boolean join, IClientPeer clientSource);
	
	public void requestLoginAuthorisation(String username, IClientPeer clientSource);
}
