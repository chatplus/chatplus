package ch.hszt.mdp.chatplus.logic.concrete;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

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
			for (IClientPeer peer : threadSafeMessageQueue)
				peer.send(msg);
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
						threadSafeMessageQueue.add(clientPeer);
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
