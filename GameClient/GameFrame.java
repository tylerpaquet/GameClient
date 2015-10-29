package GameClient;


import java.awt.event.*;
import java.io.IOException;
import java.net.*;

import javax.swing.*;

public class GameFrame extends JFrame{
	
	private final int INPORT = 3434;
	public final static int WIDTH = 650;
	public final static int HEGIHT = 700;
	private final int PSIZE = 8;
	
	private DatagramSocket socket;
	private DatagramPacket inPacket;
	private DatagramPacket outPacket;
	
	private byte[] inBuf;
	private byte[] outBuf;
	
	private GamePanel gp;
	
	private boolean running = true;
	
	public GameFrame(InetAddress address,int port) {
		super("BomberKids");
		
		inBuf = new byte[PSIZE];
		outBuf = new byte[1];
		
		gp = new GamePanel(this);
		
		inPacket = new DatagramPacket(inBuf,inBuf.length);
		outPacket = new DatagramPacket(outBuf,outBuf.length,address,port);
		
		try {
			socket = new DatagramSocket( INPORT );
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	System.out.println("GameFrame Closed...");
		    	sendMsg( 6 );	//Alert Server
		    	running = false;
		        socket.close();
		    }
		});
		

		this.getContentPane().setLayout(null);
		this.getContentPane().add( gp );
		gp.setBounds(0, 0, 650, 700);
		this.setSize(650, 730);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		start();
	}
	
	private void start(){
		while( running ){
			
			try {
				if(!socket.isClosed()){
					socket.receive( inPacket );
					
					gp.setBuffer( inBuf );
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	public void sendMsg(int msg){
		outBuf[0] = (byte)msg;	
		
		try {
			socket.send( outPacket );
			//System.out.println("Message: "+msg + " sent...");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if( msg == 6){
			running = false;
			socket.close();
		}
	}
}
