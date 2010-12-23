package ch.hszt.mdp.chatplus.logic.contract.context;

import java.util.List;

/***
 * This interface describes the functionality a client must provide. By now it's
 * far from being complete; it will be extended step by step when implementing
 * the user stories.
 * 
 * @author sfrick
 * 
 */
public interface IClientContext {
	public void displayChatMessage(String sender, String message);
	public void notifyUserStatusChange(String username, boolean isOnline);
	public void processLoginResponse(String username, boolean isAuthorised);	
}