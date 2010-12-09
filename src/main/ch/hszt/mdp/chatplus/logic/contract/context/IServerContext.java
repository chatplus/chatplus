package ch.hszt.mdp.chatplus.logic.contract.context;

/***
 * This interface describes the functionality a server must provide.
 * By now it's far from being complete; it will be extended step by step when implementing the user stories.
 * @author sfrick
 *
 */
public interface IServerContext {
	
	/***
	 * Publishes a board chat message (e.g to other chatclients on the board 
	 */
	public void publishBoardChatMessage(String boardName, String sender, String message);
	// More functionality can be added here
}
