package ch.hszt.mdp.chatplus.logic.concrete;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;

/***
 * A demo chatplus server to test the abstract communication behaviour
 * 
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

	private final Queue<IClientPeer> threadSafeMessageQueue = new LinkedList<IClientPeer>();
	private final Object lock = new Object();
	private boolean isInterrupted = false;

	private ServerSocket server;

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
			for(IClientPeer peer : threadSafeMessageQueue)
				peer.send(msg);
		}
	}

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

}
