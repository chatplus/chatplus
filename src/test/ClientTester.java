import java.io.IOException;
import java.util.Scanner;

import ch.hszt.mdp.chatplus.logic.concrete.SimpleMessage;
import ch.hszt.mdp.chatplus.logic.concrete.TcpServerPeer;
import ch.hszt.mdp.chatplus.logic.contract.context.IClientContext;


public class ClientTester implements IClientContext{

	public static void main(String[] args) throws IOException {
		
		 Scanner in = new Scanner(System.in);
		 String name = in.nextLine();
		 String input;
		 
		 TcpServerPeer peer = new TcpServerPeer();
		 peer.setContext((new ClientTester()));
		 peer.setServerIP("192.168.0.55");
		 peer.setServerPort(9999);
		 peer.Init();
		 peer.Start();
		 
		 while((input = in.nextLine()) != "")
		 {
			 SimpleMessage msg = new SimpleMessage();
			 msg.setSender(name);
			 msg.setMessage(input);
			 peer.send(msg);
		 }
	}
		
	@Override
	public void displayChatMessage(String sender, String message) {
		System.out.println(sender + ":\t" + message);
	}

}
