package ch.hszt.mdp.chatplus.logic.concrete;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import ch.hszt.mdp.chatplus.logic.concrete.message.BoardUserList;
import ch.hszt.mdp.chatplus.logic.concrete.message.LoginMessage;
import ch.hszt.mdp.chatplus.logic.concrete.message.SimpleMessage;
import ch.hszt.mdp.chatplus.logic.concrete.message.UserStatusMessage;
import ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;


/***
 * @author sfrick
 */

public class ChatPlusServer implements IServerContext, Runnable {

	private int serverPort;

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	private final Dictionary<String,UUID> usernames = new Hashtable<String,UUID>();	
	private final Dictionary<String,LinkedList<UUID>> boardRegistration = new Hashtable<String,LinkedList<UUID>>();	
	private final Dictionary<UUID,ClientInformation> clientPeerInformationTable = new Hashtable<UUID,ClientInformation>();
	private final Queue<IClientPeer> threadSafeClientPeerQueue = new LinkedList<IClientPeer>();
	
	private final Object lock = new Object();
	private boolean isInterrupted = false;

	private ServerSocket server;

	private class ClientPeerQueueManager implements Runnable {
		@Override
		public void run() {
			while (!isInterrupted) {
				System.out.println("watcher running");
				LinkedList<IClientPeer> list = new LinkedList<IClientPeer>();
				LinkedList<UserStatusMessage> logOfMsgList = new LinkedList<UserStatusMessage>();
				synchronized (lock) {
					for (IClientPeer client : threadSafeClientPeerQueue)
					{
						if(!client.isAlive())		
							list.add(client);		
					}
					for (IClientPeer client : list)
					{
						ClientInformation info = clientPeerInformationTable.get(client.getUUID());					
						if(info.isLoggedIn)
						{
							UserStatusMessage statusMsg = new UserStatusMessage();
							statusMsg.setUsername(info.userName);
							statusMsg.setIsLoggedIn(false);
							logOfMsgList.add(statusMsg);
						}
						System.out.println("watcher removing dead client");
						usernames.remove(info.userName);
						for(String boardName : info.currentBoards)
						{
							if(boardRegistration.get(boardName) != null)
								boardRegistration.get(boardName).remove(client.getUUID());
						}
						clientPeerInformationTable.remove(client.getUUID());
						threadSafeClientPeerQueue.remove(client);							
					}
					for (IClientPeer client : threadSafeClientPeerQueue)
					{
						for(UserStatusMessage msg : logOfMsgList)
							client.send(msg);
					}
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	public ChatPlusServer(int serverPort) throws IOException {
		this.serverPort = serverPort;
		server = new ServerSocket(serverPort);
	}

	@Override
	public void publishSimpleMessage(String sender, String message) {		
		SimpleMessage msg = new SimpleMessage();
		msg.setSender(sender);
		msg.setMessage(message);
		synchronized (lock) {
			for (IClientPeer client : threadSafeClientPeerQueue)
				client.send(msg);
		}
	}

	@Override
	public void run() {
		Thread watcher = new Thread(new ClientPeerQueueManager());
		watcher.start();
		while (!isInterrupted) {
			try {
				Socket client = null;
				try {
					client = server.accept();
				} catch (IOException e) {
					System.out.println("Ex.");
				}

				if (client != null) {
					System.out.println("Client != null");
					TcpClientPeer clientPeer = new TcpClientPeer();
					clientPeer.setClientSocket(client);
					clientPeer.setContext(this);
					synchronized (lock) {
						System.out.println("start info");
						System.out.println("uuid " + clientPeer.getUUID());
						clientPeerInformationTable.put(clientPeer.getUUID(), new ClientInformation(clientPeer));
						System.out.println("added info");
						threadSafeClientPeerQueue.add(clientPeer);
						System.out.println("Added client.");
					}
					clientPeer.Start();
				}
			} catch (Exception ex) {
			}
		}
	}

	public static void main(String[] args) throws IOException {
		ChatPlusServer server = new ChatPlusServer(9999);
		Thread serverThread = new Thread(server);
		serverThread.start();
		Scanner in = new Scanner(System.in);
		in.nextLine();
	}

	private String getValidUsername(String username, UUID uuid)
	{
		synchronized (lock) {
			if(usernames.get(username) != null)
				return getValidUsername(username + "_" + (new Random()).nextInt(100),uuid);
			else
			{
				usernames.put(username,uuid);
				return username;
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void requestLoginAuthorisation(String username, IClientPeer clientSource) {
		LoginMessage msg = new LoginMessage();
		msg.setAuthorised(true);
		String newUsername = getValidUsername(username,clientSource.getUUID());
		msg.setUsername(newUsername);		
		synchronized (lock) {
			System.out.println("Getting info.");
			ClientInformation info = clientPeerInformationTable.get(clientSource.getUUID());
			info.isLoggedIn = true;
			info.userName = newUsername;
			System.out.println("Looping usernames info.");
			LinkedList<String> names = new LinkedList<String>();
			for (Enumeration<String> e = usernames.keys() ; e.hasMoreElements() ;) {
				names.add(e.nextElement());
			}
			msg.setUsernames(names);
		}
		System.out.println("Sending msg to new client.");
		clientSource.send(msg);

		UserStatusMessage statusMsg = new UserStatusMessage();
		statusMsg.setUsername(msg.getUsername());
		statusMsg.setIsLoggedIn(true);
		System.out.println("Sending user status (login) to all clients.");
		synchronized (lock) {
			for (IClientPeer client : threadSafeClientPeerQueue)
				client.send(statusMsg);
		}
	}


	public void publishBoardMessage(String sender, String message, String boardName) {
		SimpleMessage msg = new SimpleMessage();
		msg.setSender(sender);
		msg.setMessage(message);
		msg.setBoard(boardName);
		synchronized (lock) {
			LinkedList<UUID> list = null;
			if((list = boardRegistration.get(boardName)) != null)
			{
				for (UUID uuid : list)
				{
					ClientInformation info = clientPeerInformationTable.get(uuid);
					if(info != null && info.peer != null)
					{ 
						info.peer.send(msg);					
					}
				}
			}
		}
	}
	public void manageBoardSubscription(String username, String boardName, boolean join, IClientPeer clientSource) {
		synchronized (lock) {
			LinkedList<UUID> list = boardRegistration.get(boardName);
			if(join)
			{
				if(list == null)
				{
					list = new LinkedList<UUID>();
					boardRegistration.put(boardName, list);
				}
				list.add(clientSource.getUUID());
				clientPeerInformationTable.get(clientSource.getUUID()).currentBoards.add(boardName);
				
				UserStatusMessage msg = new UserStatusMessage();
				msg.setBoard(boardName);
				msg.setUsername(username);
				msg.setIsLoggedIn(join);
				
				LinkedList<String> usernames = new LinkedList<String>();
				
				for(UUID uuid : list){
					if(uuid != clientSource.getUUID()){
						ClientInformation info = clientPeerInformationTable.get(uuid);
						if(info != null)
						{
							info.peer.send(msg);
							usernames.add(info.userName);
						}
					}
				}				

				BoardUserList boardListMsg = new BoardUserList();
				boardListMsg.setBoardName(boardName);
				boardListMsg.setUsernames(usernames);
			}
			else
			{
				if(list != null)
				{					
					list.remove(clientSource.getUUID());
					UserStatusMessage status = new UserStatusMessage();
					status.setBoard(boardName);
					status.setIsLoggedIn(join);
					status.setUsername(username);
					
					for(UUID uuid : list){
						if(uuid != clientSource.getUUID()){
							ClientInformation info = clientPeerInformationTable.get(uuid);
							if(info != null)
								info.peer.send(status);
						}
					}
				}
				clientPeerInformationTable.get(clientSource.getUUID()).currentBoards.remove(boardName);			
			}
		}
	}
}
