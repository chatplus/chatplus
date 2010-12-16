package ch.hszt.mdp.chatplus.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import ch.hszt.mdp.chatplus.logic.concrete.SimpleMessage;
import ch.hszt.mdp.chatplus.logic.concrete.TcpServerPeer;
import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

/***
 * The GUI of the public chat room
 * 
 * @author ckaufman
 */

public class GuiPublicRoom implements IClientContext {

	JTextArea readMessage;
	JTextArea writeMessage;
	JPanel textBox;
	JPanel buttonPanel;
	private String[] userNames = { "Pascal", "Sven", "Patrik", "Pascal", "Corinne" };
	private JList userList = new JList(userNames);
	private TcpServerPeer serverPeer;
	private String serverIP;
	private Integer serverPort;
	private String username;
	private JFrame frame;

	public static void main(String[] args) {
		GuiPublicRoom gui = new GuiPublicRoom();
		gui.guiLayout();
	}

	public void guiLayout() {
		// frame
		this.frame = new JFrame("ChatPlus");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		
		// box with two text fields - upper to read text, lower to write text
		this.textBox = new JPanel();
		this.textBox.setLayout(new BorderLayout());
		
		// text field to read messages from other users
		JPanel textPanel = new JPanel();
		textPanel.setSize(600, 300);
		readMessage = new JTextArea(20, 50);
		readMessage.setLineWrap(true);
		readMessage.setEditable(false);

		JScrollPane scrollText = new JScrollPane(readMessage);
		scrollText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollText.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		textPanel.add(scrollText);

		// text field to write own messages
		JPanel editorPanel = new JPanel();
		writeMessage = new JTextArea(5, 50);
		writeMessage.setCursor(new Cursor(2));
		
		JScrollPane writeScroller = new JScrollPane(writeMessage);
		writeScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		writeScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		editorPanel.add(writeScroller);

		// list of user names
		JPanel panelUsers = new JPanel();
		panelUsers.setSize(200, 500);
		JScrollBar scrollUsers = new JScrollBar();
		panelUsers.add(scrollUsers);
		userList.setFixedCellWidth(200);

		// buttons
		this.buttonPanel = new JPanel(new FlowLayout());
		JButton send = new JButton("send");
		this.buttonPanel.add(send);
		send.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				sendMessage(username, writeMessage.getText());
			}
		});
		
		/*// unused buttons at the moment
 		JButton createRoom = new JButton("create room");
		createRoom.addActionListener(this);
		JButton logout = new JButton("logout");
		logout.addActionListener(this);
		JButton login = new JButton("login");
		login.addActionListener(this);*/

		this.textBox.add(BorderLayout.NORTH, textPanel);
		this.textBox.add(BorderLayout.SOUTH, editorPanel);
		this.frame.getContentPane().add(BorderLayout.WEST, this.textBox);
		this.frame.getContentPane().add(BorderLayout.EAST, userList);
		this.frame.getContentPane().add(BorderLayout.SOUTH, this.buttonPanel);
		
		this.frame.setSize(800, 500);
		this.frame.setVisible(true);
		
		this.getConnectionDetails();

		writeMessage.requestFocusInWindow();
	}
	
	
	/**
	 * Disable elements
	 * 
	 * Disables all interface elements
	 * @return void 
	 */
	
	private final void disableElements() {
		this.textBox.setEnabled(false);
		this.buttonPanel.setEnabled(false);
	}
	
	
	/**
	 * Enable elements
	 * 
	 * Enables all interface elements
	 * @return void 
	 */
	
	private final void enableElements() {
		this.textBox.setEnabled(true);
		this.buttonPanel.setEnabled(true);
	}
	
	
	/**
	 * Send message
	 * 
	 * Send a message to the server
	 * 
	 * @param	sender
	 * @param	message
	 * @return  void 
	 */

	private final void sendMessage(String sender, String message) {
		//add the message to the queue
		SimpleMessage msg = new SimpleMessage();
		msg.setMessage(message);
		msg.setSender(sender);
		this.serverPeer.send(msg);
		
		//already display the message in the chat box
		this.processBoardChatMessage(this.username, message);
	}

	
	/**
	 * Process board chat message
	 * 
	 * Display a message which was received
	 * @param	sender
	 * @param	message
	 * @return	void 
	 */
	
	@Override
	public void processBoardChatMessage(String sender, String message) {
		readMessage.append(sender + " says:\t" + message + "\n");
	}


	private void getConnectionDetails() {
		
		//ask for the server ip
		String serverIP = (String)JOptionPane.showInputDialog(
				this.frame,
                "Please enter the server IP:",
                "Server IP",
                JOptionPane.PLAIN_MESSAGE);
		
		//ask for the server port
		Integer serverPort = Integer.parseInt(JOptionPane.showInputDialog(
				this.frame,
                "Please enter the server Port:",
                "Server Port",
                JOptionPane.PLAIN_MESSAGE));
				
		//try to connect to the server	
		if(!this.connectToServer(serverIP, serverPort)) {
			
			//request the connection details until a connection can be established
			//must be cancellable in a future version
			this.getConnectionDetails();
			
		} else {
			
			//ask for the username
			this.username = (String)JOptionPane.showInputDialog(
					this.frame,
	                "Please enter your username:",
	                "Username",
	                JOptionPane.PLAIN_MESSAGE);

		}

	}
	
	private boolean connectToServer(String ip, Integer port) {

		//init the server peer
		this.serverPeer = new TcpServerPeer();
		this.serverPeer.setServerIP(ip);
		this.serverPeer.setServerPort(port);
		
		try {
			//init the server peer. will throw various exceptions if the connection failed
			this.serverPeer.Init();
			
			//store the server ip and port for later usage
			this.serverIP = ip;
			this.serverPort = port;
			
			//To not block the GUI we have to start a separate thread for the peer handling
			Thread t = new Thread(this.serverPeer);
			t.start();
			
			return true;
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(this.frame, "Unknown host");
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this.frame, "Could not connect to the server");
			return false;
		}
		
	}
	
	private void disconnectFromServer() {
		this.serverPeer.Stop();
	}

}
