package ch.hszt.mdp.chatplus.logic.concrete;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import java.util.Random;
import java.util.Scanner;

import ch.hszt.mdp.chatplus.logic.concrete.message.LoginMessage;
import ch.hszt.mdp.chatplus.logic.concrete.message.SimpleMessage;
import ch.hszt.mdp.chatplus.logic.concrete.message.UserStatusMessage;
import ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;


/***
 * A demo chatplus server to test the abstract communication behaviour
 * 
 * @author sfrick
 */

public class ChatPlusServer implements IServerContext, Runnable {

	private int serverPort;
	private final Queue<IClientPeer> threadSafeMessageQueue = new LinkedList<IClientPeer>();
	private final LinkedList<String> usernames = new LinkedList<String>();
	private final Queue<ClientPeerWrapper> threadSafeClientPeerQueue = new LinkedList<ClientPeerWrapper>();
	private final Object lock = new Object();
	private boolean isInterrupted = false;
	private ServerSocket server;

	
	/**
	 * Constructor
	 * 
	 * @param serverPort
	 * @throws IOException
	 */

	public ChatPlusServer(int serverPort) throws IOException {
		this.serverPort = serverPort;
		server = new ServerSocket(serverPort);
	}


	private class ClientPeerQueueManager implements Runnable {

		@Override
		public void run() {
			while (!isInterrupted) {
				System.out.println("watcher running");
				LinkedList<ClientPeerWrapper> list = new LinkedList<ClientPeerWrapper>();
				synchronized (lock) {
					for (ClientPeerWrapper client : threadSafeClientPeerQueue)
					{
						if(!client.peer.isAlive())		
							list.add(client);		
					}
					for (ClientPeerWrapper client : list)
					{
						if(client.isLoggedIn)
						{
							//new logout msg
						}
						System.out.println("watcher removing dead client");
						threadSafeClientPeerQueue.remove(client);								
					}
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
			}
		}

	}
	
	
	/**
	 * Publish simple message
	 * 
	 * @param	sender		origin of the message
	 * @param	message		the message body
	 */
	
	@Override
	public void publishSimpleMessage(String sender, String message) {		
		SimpleMessage msg = new SimpleMessage();
		msg.setSender(sender);
		msg.setMessage(message);
		synchronized (lock) {
			for (ClientPeerWrapper client : threadSafeClientPeerQueue)
				client.peer.send(msg);
		}
	}

	
	/**
	 * Run
	 * 
	 * Run method of Runnable implementation. 
	 * Accepts new connections
	 */
	
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
				}

				if (client != null) {
					TcpClientPeer clientPeer = new TcpClientPeer();
					clientPeer.setClientSocket(client);
					clientPeer.setContext(this);
					synchronized (lock) {
						ClientPeerWrapper clientWrapper = new ClientPeerWrapper();
						clientWrapper.peer = clientPeer;						
						threadSafeClientPeerQueue.add(clientWrapper);
					}
					clientPeer.Start();
				}
			} catch (Exception ex) {
			}
		}
	}
	
	
	/**
	 * Main method to start a ChatPlus server instance
	 * Currently runs on port 9999 by default
	 * 
	 * @param args
	 * @throws IOException
	 */

	public static void main(String[] args) throws IOException {
		ChatPlusServer server = new ChatPlusServer(9999);
		Thread serverThread = new Thread(server);
		serverThread.start();
		Scanner in = new Scanner(System.in);
		in.nextLine();
	}

	
	/**
	 * Get Valid Username
	 * 
	 * Avoid duplicate usernames
	 * 
	 * @param username
	 * @return unique username
	 */

	private String getValidUsername(String username)
	{
		synchronized (lock) {
			if(usernames.contains(username))
				return getValidUsername(username + "_" + (new Random()).nextInt(100));
			else
			{
				usernames.add(username);
				return username;
			}
		}
	}
	
	
	/**
	 * Request Login Authorisation
	 * 
	 * @param	username
	 * @param	clientSource
	 */
	
	@Override
	public void requestLoginAuthorisation(String username, IClientPeer clientSource) {
		LoginMessage msg = new LoginMessage();
		msg.setAuthorised(true);
		msg.setUsername(getValidUsername(username));
		synchronized (lock) {
			msg.setUsernames((LinkedList<String>)usernames.clone());
		}
		clientSource.send(msg);

		UserStatusMessage statusMsg = new UserStatusMessage();
		statusMsg.setUsername(msg.getUsername());
		synchronized (lock) {
			for (ClientPeerWrapper client : threadSafeClientPeerQueue)
				client.peer.send(statusMsg);
		}
	}	
	
	
	/*
	 * Getters and setters
	 */
	
	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

}
