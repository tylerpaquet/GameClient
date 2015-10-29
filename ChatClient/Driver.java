package ChatClient;


import java.io.*;
import java.net.Socket;

import javax.swing.JFrame;

public class Driver {

	// Port server uses to listen for connections
	public static int PORT = 12345;
	
	public static void main(String[] args) throws IOException {
		
		// Tries all computers in EOS lab for a running server
		Socket ss = FindServer.getServer("148.61.162.",101,23,PORT);
		
		// If no server is found it exits
		if(ss == null){
			System.out.println("Could not find a server...");
			System.exit(0);
		}
		
		// If a server is found creates the ChatRoom GUI
		MainFrame client = new MainFrame(ss);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
	}

}
