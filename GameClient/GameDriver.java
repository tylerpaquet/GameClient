package GameClient;

import java.io.IOException;
import java.net.*;

public class GameDriver extends Thread{

	private final int PORTL = 13337;

	private int port;
	private InetAddress address;
	
	private DatagramSocket socket;
	
	public GameDriver(){
		System.out.println("GAME CONSTUCTOR");
		
		try {
			socket = new DatagramSocket( PORTL );
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run(){		
		
		byte[] portBuffer = new byte[175];
		DatagramPacket inPacket = new DatagramPacket(portBuffer,portBuffer.length);
		
		try {
			System.out.println("Waiting for server...");
			socket.receive( inPacket );
			address = inPacket.getAddress();
			port = inPacket.getPort();
			System.out.println("Server: "+address.getHostAddress() + ":" + port);
			System.out.println("You are player..." + portBuffer[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		socket.close();
		new GameFrame(address,port);
		System.out.println("Create Game Frame Here....");
	}
}
