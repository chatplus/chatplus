package ch.hszt.mdp.chatplus.logic.concrete;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import ch.hszt.mdp.chatplus.logic.contract.context.IServerContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;
import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IClientPeer;

public class TcpClientPeer implements IClientPeer {

	private UUID peerUUID;
	private final Queue<IServerMessage> threadSafeMessageQueue = new LinkedList<IServerMessage>();
	private final Object lock = new Object();
	private boolean isInterrupted = false;

	private class ClientRx implements Runnable {
		private InputStream stream;
		private IServerContext context;
		private IClientPeer parent;

		public ClientRx(IServerContext context, InputStream stream,
				IClientPeer parent) {
			this.stream = stream;
			this.context = context;
			this.parent = parent;
		}

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

		public ClientTx(OutputStream stream) {
			this.stream = stream;
		}

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

	private IServerContext context;

	public IServerContext getContext() {
		return context;
	}

	public void setContext(IServerContext context) {
		this.context = context;
	}

	private Socket clientSocket;

	@Override
	public void send(IServerMessage message) {
		synchronized (lock) {
			threadSafeMessageQueue.add(message);
			lock.notify();
		}
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void Stop() {
		isInterrupted = true;
		lock.notify();
	}

	public void Start() throws IOException {
		Thread rx = new Thread(new ClientRx(context, clientSocket
				.getInputStream(), this));
		Thread tx = new Thread(new ClientTx(clientSocket.getOutputStream()));
		rx.start();
		tx.start();
	}

	public boolean isAlive() {
		return !isInterrupted && clientSocket.isConnected();
	}

	@Override
	public UUID getUUID() {
		return peerUUID;
	}

	public TcpClientPeer() {
		peerUUID = UUID.randomUUID();
		System.out.println(peerUUID);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TcpClientPeer))
			return false;
		TcpClientPeer otherPeer = (TcpClientPeer) o;
		return otherPeer.getUUID().equals(peerUUID);
	}

	@Override
	public int hashCode() {
		return peerUUID.hashCode();
	}

}
