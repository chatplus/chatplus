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
 * GUI of the chat application
 * 
 * @author ckaufman
 */

public class ChatGui implements IClientContext {

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
		ChatGui gui = new ChatGui();
		gui.guiLayout();
	}

	
	/**
	 * Gui Layout
	 * 
	 * Builds the GUI
	 * @return void
	 */
	
	public void guiLayout() {
		// frame
		frame = new JFrame("ChatPlus");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		// box with two text fields - upper to read text, lower to write text
		textBox = new JPanel();
		textBox.setLayout(new BorderLayout());
		
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
		buttonPanel = new JPanel(new FlowLayout());
		JButton send = new JButton("send");
		buttonPanel.add(send);
		send.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				sendMessage(username, writeMessage.getText());
			}
		});

		textBox.add(BorderLayout.NORTH, textPanel);
		textBox.add(BorderLayout.SOUTH, editorPanel);
		frame.getContentPane().add(BorderLayout.WEST, textBox);
		frame.getContentPane().add(BorderLayout.EAST, userList);
		frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
		
		frame.setSize(800, 500);
		
		//disable all elements .. we will enable them again after logging in to a server
		disableElements();
		
		frame.setVisible(true);
		
		//get the server ip, port and username
		getConnectionDetails();

		writeMessage.requestFocusInWindow();
	}
	
	
	/**
	 * Disable elements
	 * 
	 * Disables all interface elements
	 * @return void 
	 */
	
	private final void disableElements() {
		textBox.setEnabled(false);
		buttonPanel.setEnabled(false);
	}
	
	
	/**
	 * Enable elements
	 * 
	 * Enables all interface elements
	 * @return void 
	 */
	
	private final void enableElements() {
		textBox.setEnabled(true);
		buttonPanel.setEnabled(true);
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
		serverPeer.send(msg);
		
		//already display the message in the chat box
		displayChatMessage(username, message);
		writeMessage.setText("");
	}

	
	/**
	 * Display chat message
	 * 
	 * Display a message which was sent by the server
	 * @return	void 
	 */
	
	@Override
	public void displayChatMessage(String sender, String message) {
		readMessage.append(sender + " says:\t" + message + "\n");
	}
	
	
	/**
	 * Get all the connection details from the user
	 * 
	 * Display a message which was received
	 * @return	void 
	 */
	
	private void getConnectionDetails() {
		
		//ask for the server ip
		String serverIP = (String)JOptionPane.showInputDialog(
				frame,
                "Please enter the server IP:",
                "Server IP",
                JOptionPane.PLAIN_MESSAGE);
		
		//ask for the server port
		Integer serverPort = Integer.parseInt(JOptionPane.showInputDialog(
				frame,
                "Please enter the server Port:",
                "Server Port",
                JOptionPane.PLAIN_MESSAGE));
				
		//try to connect to the server	
		if(!connectToServer(serverIP, serverPort)) {
			
			//request the connection details until a connection can be established
			//must be cancellable in a future version
			getConnectionDetails();
			
		} else {
			
			//ask for the username
			username = (String)JOptionPane.showInputDialog(
					frame,
	                "Please enter your username:",
	                "Username",
	                JOptionPane.PLAIN_MESSAGE);

		}

	}
	
	
	/**
	 * Connect to the server
	 * 
	 * Establish the connection between the client and the server
	 * @param	ip
	 * @param	port
	 * @return	void 
	 */
	
	private boolean connectToServer(String ip, Integer port) {

		//init the server peer
		serverPeer = new TcpServerPeer();
		serverPeer.setServerIP(ip);
		serverPeer.setServerPort(port);
		serverPeer.setContext(this);
		
		try {
			//init the server peer. will throw various exceptions if the connection failed
			serverPeer.Init();
			serverPeer.Start();
			
			//store the server ip and port for later usage
			serverIP = ip;
			serverPort = port;
			
			return true;
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(frame, "Unknown host");
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Could not connect to the server");
			return false;
		}
		
	}
	
	
	/**
	 * Disconnect from the server
	 * 
	 * Close the connection to the server
	 * @return	void 
	 */
	
	private void disconnectFromServer() {
		serverPeer.Stop();
	}
	
}
