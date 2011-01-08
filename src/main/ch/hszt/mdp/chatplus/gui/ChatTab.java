package ch.hszt.mdp.chatplus.gui;

import java.util.LinkedList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class ChatTab extends JPanel{

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
		
        messageDisplayScroll.setAutoscrolls(true);
        
        messageDisplay.setColumns(20);
        messageDisplay.setRows(5);
        messageDisplayScroll.setViewportView(messageDisplay);

        messageWriting.setColumns(20);
        messageWriting.setRows(5);
        messageWritingScroll.setViewportView(messageWriting);

        messageWritingLabel.setText("Enter your message here");

        userList.setToolTipText("Available users");
        userListScroll.setViewportView(userList);

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });
        
        leaveBoardButton.setText("Leave board");
        leaveBoardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leaveBoardButtonActionPerformed(evt);
            }
        });

        GroupLayout chatTabPanelLayout = new GroupLayout(this);
        this.setLayout(chatTabPanelLayout);
        chatTabPanelLayout.setHorizontalGroup(
            chatTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(chatTabPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(chatTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(messageDisplayScroll, GroupLayout.PREFERRED_SIZE, 420, GroupLayout.PREFERRED_SIZE)
                    .addComponent(messageWritingLabel)
                    .addComponent(messageWritingScroll, GroupLayout.PREFERRED_SIZE, 420, GroupLayout.PREFERRED_SIZE)
                    .addGroup(chatTabPanelLayout.createSequentialGroup()
                        .addComponent(leaveBoardButton)
                        .addGap(257, 257, 257)
                        .addComponent(sendButton)))
                .addGap(6, 6, 6)
                .addComponent(userListScroll, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE))
        );
        chatTabPanelLayout.setVerticalGroup(
            chatTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(chatTabPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(messageDisplayScroll, GroupLayout.PREFERRED_SIZE, 310, GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(messageWritingLabel)
                .addGap(10, 10, 10)
                .addComponent(messageWritingScroll, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(chatTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(leaveBoardButton)
                    .addComponent(sendButton)))
            .addGroup(chatTabPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(userListScroll, GroupLayout.PREFERRED_SIZE, 490, GroupLayout.PREFERRED_SIZE))
        );
       	
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
	 * @return void
	 */

	public void displayChatMessage(String sender, String message) {
		messageDisplay.append(sender + " says:\t" + message + "\n");
	}	
	
	

	public void notifyUserStatusChange(String username,
			boolean isOnline) {
		
		if(!isOnline) {
			users.remove(username);
		} else {
			if(!users.contains(username)) {
				users.add(username);
			}
		}
		userList.setListData(users.toArray());
	
	}
	
}
