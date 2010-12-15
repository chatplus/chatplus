package ch.hszt.mdp.chatplus.logic.concrete;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;
import ch.hszt.mdp.chatplus.logic.contract.peer.IServerPeer;

/***
 * The GUI of the public chat room
 * 
 * @author ckaufman
 */

public class GuiPublicRoom implements ActionListener, IClientContext {

	JTextArea readMessage;
	JTextArea writeMessage;
	String[] userNames = { "Pascal                        ", "Sven", "Patrik",
			"Pascal", "Corinne" };
	JList userList = new JList(userNames);
	IServerPeer server;

	public static void main(String[] args) {
		GuiPublicRoom gui = new GuiPublicRoom();
		gui.guiLayout();
	}

	public void guiLayout() {
		// frame
		JFrame frame = new JFrame("ChatPlus");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// box with two text fields - upper to read text, lower to write text
		JPanel textBox = new JPanel();
		textBox.setLayout(new BorderLayout());
		
		// text field to read messages from other users
		JPanel textPanel = new JPanel();
		readMessage = new JTextArea(20, 50);
		readMessage.setLineWrap(true);
		readMessage.setEditable(false);
		readMessage.setBackground(Color.orange);
		readMessage.setForeground(Color.BLUE);

		JScrollPane scrollText = new JScrollPane(readMessage);
		scrollText
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollText
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		textPanel.add(scrollText);

		// text field to write own messages
		JPanel editorPanel = new JPanel();
		writeMessage = new JTextArea(5, 50);
		writeMessage.setCursor(new Cursor(2));
		
		JScrollPane writeScroller = new JScrollPane(writeMessage);
		writeScroller
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		writeScroller
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		editorPanel.add(writeScroller);

		// list of user names
		JPanel panelUsers = new JPanel();
		JScrollBar scrollUsers = new JScrollBar();
		panelUsers.add(scrollUsers);
		userList.setVisibleRowCount(20);

		// buttons
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton send = new JButton("send");
		buttonPanel.add(send);
		send.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			sendMessage();
		}
		});
		// unused buttons at the moment
 		JButton createRoom = new JButton("create room");
		createRoom.addActionListener(this);
		JButton logout = new JButton("logout");
		logout.addActionListener(this);
		JButton login = new JButton("login");
		login.addActionListener(this);

		textBox.add(BorderLayout.NORTH, textPanel);
		textBox.add(BorderLayout.SOUTH, editorPanel);
		frame.getContentPane().add(BorderLayout.WEST, textBox);
		frame.getContentPane().add(BorderLayout.EAST, userList);
		frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
		
		frame.setSize(800, 500);
		frame.setVisible(true);

		writeMessage.requestFocusInWindow();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

	public void sendMessage() {
		SimpleMessage newMessage = new SimpleMessage();
		newMessage.setMessage("Wert von Textfeld...");
		newMessage.setSender("Name");// welcher zu Beginn eingegeben wurde ...
		// --> ohne check ob Name schon besteht oder sonstiges Login);
		// server.send(newMessage);
		writeMessage.setText(""); 
		writeMessage.requestFocusInWindow();
		processBoardChatMessage("Corinne", "Hello");
	}

	@Override
	public void processBoardChatMessage(String sender, String message) {
		readMessage.append(sender + " says:\t" + message + "\n");

	}

	@Override
	public void displayChatMessage(String sender, String message) {
		writeMessage.append(sender + " says:\t" + message);
	}

}
