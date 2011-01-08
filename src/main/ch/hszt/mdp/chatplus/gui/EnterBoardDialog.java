package ch.hszt.mdp.chatplus.gui;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

/**
 *
 * @author  pascalbeyeler
 */
public class EnterBoardDialog extends JDialog {

	private JTextField boardName;
	private JLabel boardNameLabel;
	private JButton enterBoardButton;

	/** Creates new form EnterBoardDialog */
	public EnterBoardDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	private void initComponents() {

		boardNameLabel = new JLabel();
		boardName = new JTextField();
		enterBoardButton = new JButton();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		boardNameLabel.setText("Board name:");

		enterBoardButton.setText("Enter board");
		enterBoardButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				enterBoardButtonActionPerformed(evt);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(enterBoardButton)
								.addGroup(layout.createSequentialGroup()
										.addComponent(boardNameLabel)
										.addGap(18, 18, 18)
										.addComponent(boardName, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)))
										.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(boardNameLabel)
								.addComponent(boardName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(enterBoardButton)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		pack();
	}


	private void enterBoardButtonActionPerformed(java.awt.event.ActionEvent evt) {
		dispose();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				EnterBoardDialog dialog = new EnterBoardDialog(new JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}
	
	public String getBoardName() {
		return boardName.getText();
	}


}

