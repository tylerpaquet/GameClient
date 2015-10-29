package GameClient;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener{

	
	private GameFrame gf;
	private byte[] buffer;

	public GamePanel(GameFrame gf){
		
		this.gf = gf;
		
		this.addKeyListener( this );
		this.setPreferredSize(new Dimension(650,700));
		this.setFocusable(true);
		this.requestFocus();
	}
	
	public void setBuffer(byte[] buf){
		buffer = buf;
		repaint();
	}
	
	public void paintComponent(Graphics g){
		
		g.setColor(Color.white);
		g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEGIHT+50);
		
		if(buffer == null)
			return;
		
		
		g.setColor(Color.green);
		int x1 = bytesToInt(buffer[0],buffer[1]);
		int y1 = bytesToInt(buffer[2],buffer[3]);
		int x2 = bytesToInt(buffer[4],buffer[5]);
		int y2 = bytesToInt(buffer[6],buffer[7]);
		
		
		
		g.fillOval(x1, y1, 10, 10);
		g.fillOval(x2, y2, 10, 10);
	}
	
	private int bytesToInt(byte b1,byte b2){
		int i1 = (int)b1;
		int i2 = (int)b2;
		return 128*i1 + i2;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
		//System.out.println("KEY PRESSED!!");
		
		if(arg0.getKeyCode() == KeyEvent.VK_UP){
			//System.out.println("UP");
			gf.sendMsg( 1 );
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_RIGHT){
			//System.out.println("RIGHT");
			gf.sendMsg( 2 );
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_DOWN){
			//System.out.println("DOWN");
			gf.sendMsg( 3 );
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_LEFT){
			//System.out.println("LEFT");
			gf.sendMsg( 4 );
		}
		
		/*
		if(arg0.getKeyCode() == KeyEvent.VK_SPACE){
			//System.out.println("BOMB");
			gf.sendMsg( 5 );
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_Q){
			//System.out.quiting
			gf.sendMsg( 6 );
		}
		*/
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
