package ch.hszt.mdp.chatplus.logic.concrete;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import ch.hszt.mdp.chatplus.logic.contract.message.IClientMessage;
import ch.hszt.mdp.chatplus.logic.contract.message.IServerMessage;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

public class TcpServerPeer implements IServerPeer {

	private InputStream stream;
	private IClientContext context;

	private class ServerRx implements Runnable {
		private InputStream stream;
		private IClientContext context;

		public ServerRx(IClientContext context, InputStream stream) {
			this.context = context;
			this.stream = stream;
		}

		@Override
		public void run() {
			while (!isInterrupted) {
				ObjectReceiver objRx = new ObjectReceiver(stream);
				try {
					((IServerMessage) objRx.receive()).process(context);
				} catch (IOException e) {

				}
			}
		}

	}

	private class ServerTx implements Runnable {
		private OutputStream stream;

		public ServerTx(OutputStream stream) {
			this.stream = stream;
		}

		@Override
		public void run() {

			System.out.println("Starting.");
			while (!isInterrupted) {
				System.out.println("Looping.");
				IClientMessage msg;

				synchronized (lock) {
					System.out.println("Locked.");
					msg = threadSafeMessageQueue.poll();

					while (msg != null) {
						System.out.println("Msg != null.");
						try {

							ObjectSender sender = new ObjectSender(stream);
							sender.send(msg);

							msg = threadSafeMessageQueue.poll();
						} catch (Exception ex) {
							System.out.println("Ex:" + ex.getMessage());
						}
					}

					try {
						System.out.println("Waiting");
						lock.wait();
					} catch (InterruptedException e) {
						System.out.println("Error");
					}
				}
			}
			System.out.println("Dying");
		}

	}

	private String serverIP = "";
	private int serverPort;

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	private Socket server;
	private final Queue<IClientMessage> threadSafeMessageQueue = new LinkedList<IClientMessage>();
	private final Object lock = new Object();

	private boolean isInterrupted = false;

	@Override
	public void send(IClientMessage message) {

		synchronized (lock) {
			threadSafeMessageQueue.add(message);
			lock.notify();
		}
	}

	public void Init() throws UnknownHostException, IOException {
		server = new Socket(serverIP, serverPort);
	}

	public void Start() throws IOException {
		Thread rx = new Thread(new ServerRx(context, server.getInputStream()));
		Thread tx = new Thread(new ServerTx(server.getOutputStream()));
		rx.start();
		tx.start();
	}

	public void Stop() {
		isInterrupted = true;
		// lock.notify();
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setContext(IClientContext context) {
		this.context = context;
	}

	public IClientContext getContext() {
		return context;
	}

}
