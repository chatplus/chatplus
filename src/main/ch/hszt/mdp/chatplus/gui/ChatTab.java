package ch.hszt.mdp.chatplus.gui;

import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatTab extends JPanel {

	private static final long serialVersionUID = 1184203454761235976L;
	private JTextArea messageDisplay;
	private JScrollPane messageDisplayScroll;
	private JTextArea messageWriting;
	private JLabel messageWritingLabel;
	private JScrollPane messageWritingScroll;
	private JButton sendButton;
	private JList userList;
	private JScrollPane userListScroll;
	private String tabName;
	private ChatWindow chatWindow;
	private JButton leaveBoardButton;
	private LinkedList<String> users = new LinkedList<String>();
	private JPanel centerBottomButtonPanel;
	private JPanel centerBottomPanel;
	private JPanel centerPanel;

	public ChatTab(ChatWindow chatWindow, String tabName) {
		this.chatWindow = chatWindow;
		this.tabName = tabName;
		initComponents();
	}

	/**
	 * Init Components
	 * 
	 * Adds all required GUI elements
	 */

	private void initComponents() {

		messageDisplayScroll = new JScrollPane();
		messageDisplay = new JTextArea();
		messageWritingScroll = new JScrollPane();
		messageWriting = new JTextArea();
		messageWritingLabel = new JLabel();
		userListScroll = new JScrollPane();
		userList = new JList();
		sendButton = new JButton();
		leaveBoardButton = new JButton();
		centerPanel = new JPanel();
		centerBottomPanel = new JPanel();
		centerBottomButtonPanel = new JPanel();

		this.setBorder(BorderFactory.createLineBorder(new java.awt.Color(237,
				236, 235), 7));
		this.setLayout(new java.awt.BorderLayout(10, 10));

		centerPanel.setLayout(new java.awt.BorderLayout(10, 10));

		messageDisplay.setColumns(20);
		messageDisplay.setRows(5);
		messageDisplay.setEditable(false);
		messageDisplay.setMargin(new java.awt.Insets(10, 10, 10, 10));
		messageDisplayScroll.setViewportView(messageDisplay);

		centerPanel.add(messageDisplayScroll, java.awt.BorderLayout.CENTER);

		centerBottomPanel.setLayout(new java.awt.BorderLayout(10, 10));

		messageWritingLabel.setText("Enter your message here");
		centerBottomPanel.add(messageWritingLabel, java.awt.BorderLayout.NORTH);

		messageWriting.setColumns(20);
		messageWriting.setRows(5);
		messageWriting.setMargin(new java.awt.Insets(10, 10, 10, 10));
		messageWritingScroll.setViewportView(messageWriting);

		centerBottomPanel.add(messageWritingScroll,
				java.awt.BorderLayout.CENTER);

		centerBottomButtonPanel.setLayout(new java.awt.BorderLayout(10, 10));

		sendButton.setText("Send");
		sendButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendButtonActionPerformed(evt);
			}
		});
		centerBottomButtonPanel.add(sendButton, java.awt.BorderLayout.EAST);

		leaveBoardButton.setText("Leave board");
		if (tabName.equals("Public")) {
			leaveBoardButton.setEnabled(false);
		}
		leaveBoardButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				leaveBoardButtonActionPerformed(evt);
			}
		});
		centerBottomButtonPanel.add(leaveBoardButton,
				java.awt.BorderLayout.WEST);

		centerBottomPanel.add(centerBottomButtonPanel,
				java.awt.BorderLayout.SOUTH);

		centerPanel.add(centerBottomPanel, java.awt.BorderLayout.SOUTH);

		this.add(centerPanel, java.awt.BorderLayout.CENTER);

		userList.setToolTipText("Available users");
		userList.setFixedCellWidth(200);
		userListScroll.setViewportView(userList);

		this.add(userListScroll, java.awt.BorderLayout.EAST);

	}

	private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {
		chatWindow.sendMessage(messageWriting.getText(), tabName);
		messageWriting.setText("");
	}

	private void leaveBoardButtonActionPerformed(java.awt.event.ActionEvent evt) {
		chatWindow.leaveBoard(tabName);
	}

	/**
	 * Display chat message
	 * 
	 * Display a message which was sent by the server
	 * 
	 * @param sender
	 * @param message
	 */

	public void displayChatMessage(String sender, String message) {
		messageDisplay.append(sender + " says: " + message + "\n");
	}

	/**
	 * Add or remove user from list according to isOnline state
	 * 
	 * @param username
	 * @param isOnline
	 */

	public void notifyUserStatusChange(String username, boolean isOnline) {

		if (!isOnline) {
			users.remove(username);
			messageDisplay.append(username + " has left this board" + "\n");
		} else {
			if (!users.contains(username)) {
				users.add(username);
				messageDisplay.append(username + " has entered this board"
						+ "\n");
			}
		}
		userList.setListData(users.toArray());

	}

}
