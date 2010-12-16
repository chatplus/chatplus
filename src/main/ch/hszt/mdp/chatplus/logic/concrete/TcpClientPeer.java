package ch.hszt.mdp.chatplus.logic.concrete;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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

public class TcpClientPeer implements IClientPeer{

	private final Queue<IServerMessage> threadSafeMessageQueue = new LinkedList<IServerMessage>();
	private final Object lock = new Object();
	private boolean isInterrupted = false;
		
	private class ClientRx implements Runnable
	{
		private InputStream stream;
		private IServerContext context;
		
		public ClientRx(IServerContext context, InputStream stream) {
			this.stream = stream;
			this.context = context;
		}
		@Override
		public void run() {
			while(!isInterrupted)
			{
				XMLDecoder decoder = new XMLDecoder(
					    new BufferedInputStream(stream));
					  
				Object obj = decoder.readObject();					  
				((IClientMessage)obj).process(context);
			}
		}
	}
	private class ClientTx implements Runnable
	{
		private OutputStream stream;

		public ClientTx(OutputStream stream) {
			this.stream = stream;
		}
		public void run() {
			System.out.println("Starting.");
			
			while (!isInterrupted) {
				System.out.println("Looping.");
				IServerMessage msg;

				synchronized (lock) {
					System.out.println("Locked.");
					msg = threadSafeMessageQueue.poll();

					while (msg != null) {
						System.out.println("Msg != null.");
						try {
							XMLEncoder encoder = new XMLEncoder(
									new BufferedOutputStream(stream));
							encoder.writeObject(msg);
							encoder.close();

							msg = threadSafeMessageQueue.poll();
						} catch (Exception ex) {
							System.out.println("Ex:" + ex.getMessage());
						}
					}

					try {
						System.out.println("Waiting");
						lock.wait();
					} catch (InterruptedException e) {
					}
				}
			}
			System.out.println("Dying");
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
	public void Start() throws IOException
	{
		Thread rx = new Thread(new ClientRx(context,clientSocket.getInputStream()));
		Thread tx = new Thread(new ClientTx(clientSocket.getOutputStream()));
		rx.start();
		tx.start();
	}
	
}
