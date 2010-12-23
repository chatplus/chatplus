package ch.hszt.mdp.chatplus.logic.concrete;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;
import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;

public class TcpClientPeer implements IClientPeer {

	private final Queue<IServerMessage> threadSafeMessageQueue = new LinkedList<IServerMessage>();
	private final Object lock = new Object();
	private boolean isInterrupted = false;
	private IServerContext context;
	private Socket clientSocket;

	
	private class ClientRx implements Runnable {
		
		private InputStream stream;
		private IServerContext context;
		private IClientPeer parent;

		
		/**
		 * Constructor
		 * 
		 * @param context
		 * @param stream
		 * @param parent
		 */
		
		public ClientRx(IServerContext context, InputStream stream, IClientPeer parent) {
			this.stream = stream;
			this.context = context;
			this.parent = parent;
		}

		
		/**
		 * Run
		 * 
		 * Run method of Runnable implementation. 
		 * Receives new objects from the Rx stream.
		 */

		@Override
		public void run() {
			while (!isInterrupted) {
				ObjectReceiver objRX = new ObjectReceiver(stream);
				try {
					IClientMessage msg = ((IClientMessage) objRX.receive());
					msg.setClientSource(parent);
					msg.process(context);
				} catch (IOException e) {
					isInterrupted = true;
				}
			}
		}
		
	}

	private class ClientTx implements Runnable {

		private OutputStream stream;

		
		/**
		 * Constructor
		 * 
		 * @param stream
		 */
		
		public ClientTx(OutputStream stream) {
			this.stream = stream;
		}

		
		/**
		 * Run
		 * 
		 * Run method of Runnable implementation. 
		 * Sends new messages
		 */
		
		public void run() {
			System.out.println("[ClientTX]\tStarting.");

			while (!isInterrupted) {
				System.out.println("[ClientTX]\tLooping.");
				IServerMessage msg;

				synchronized (lock) {
					System.out.println("[ClientTX]\tLocked.");
					msg = threadSafeMessageQueue.poll();

					while (msg != null) {
						System.out.println("[ClientTX]\tMsg != null.");
						try {
							ObjectSender sender = new ObjectSender(stream);
							sender.send(msg);
							msg = threadSafeMessageQueue.poll();
						} catch (Exception ex) {
							System.out.println("Ex:" + ex.getMessage());
						}
					}

					try {
						System.out.println("[ClientTX]\tWaiting");
						lock.wait();
					} catch (InterruptedException e) {
					}
				}
			}
			System.out.println("[ClientTX]\tDying");
		}
		
	}

	
	/**
	 * Send
	 * 
	 * Adds a new message to the message queue.
	 * 
	 * @param	message
	 */
	
	@Override
	public void send(IServerMessage message) {
		
		synchronized (lock) {
			threadSafeMessageQueue.add(message);
			lock.notify();
		}

	}

	
	/**
	 * Stop
	 * 
	 * Shut the client peer down
	 */
	
	public void Stop() {
		isInterrupted = true;
		lock.notify();
	}


	/**
	 * Start
	 * 
	 * Start a new client peer
	 * 
	 * @throws IOException
	 */
	
	public void Start() throws IOException {
		
		Thread rx = new Thread(new ClientRx(context, clientSocket
				.getInputStream(), this));
		Thread tx = new Thread(new ClientTx(clientSocket.getOutputStream()));
		rx.start();
		tx.start();
		
	}
	
	
	/*
	 *	Getters and setters 
	 */

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}
	
	public IServerContext getContext() {
		return context;
	}

	public void setContext(IServerContext context) {
		this.context = context;
	}
	
	public boolean isAlive()
	{
		return !isInterrupted && clientSocket.isConnected();
	}

}
