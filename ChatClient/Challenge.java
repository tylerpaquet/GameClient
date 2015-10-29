package ChatClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;

import GameClient.GameDriver;

public class Challenge extends JFrame{
	
	// Color and time constants
	private final int DTIME = 1000;
	private final int BG1 = 0x0F5D2C;
	private final int BG2 = 0x297B48;

	//Swing components
	private JButton btnCancel;
	private JLabel waiting,dots;
	
	//false if cancels
	boolean isWaiting = true;
	
	//Current number of dots to show in frame
	private int numDots;
	
	public Challenge() throws IOException{
		this.setTitle("Challenge extended...");

		//Waiting
		waiting = new JLabel("Waiting",JLabel.CENTER);
		waiting.setBounds(0, 20, 400, 40);
		waiting.setFont(new Font("Dialog.bold" , Font.BOLD, 20));
		waiting.setOpaque(true);
		waiting.setBackground(new Color(BG2));

		//Dots label
		numDots = 1;
		dots = new JLabel(".",JLabel.CENTER);
		dots.setBounds(0, 120, 400, 40);
		dots.setFont(new Font("Dialog.bold" , Font.BOLD, 20));
		dots.setOpaque(true);
		dots.setBackground(new Color(BG2));
		
		//Cancel Button
		btnCancel = new JButton("Cancel");
		btnCancel.setBackground(new Color(BG2));
		btnCancel.setBounds(new Rectangle(150,200,100,50));
		btnCancel.setFocusPainted(false);
		//btnCancel.addActionListener(btnList);
		
		
		this.getContentPane().add(btnCancel);
		this.getContentPane().add(waiting);
		this.getContentPane().setLayout(null);
		this.getContentPane().add(dots);
		
		//Frame gui stuff
		this.getContentPane().setBackground(new Color(BG1));
		this.setSize(400, 300);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		timerAction ta = new timerAction();
		Timer timer = new Timer(DTIME,ta);
		timer.start();
		
		new Handler(this).start();
	}

	
	public class Handler extends Thread{
		
		Challenge parent;
		
		public Handler(Challenge c){
			parent = c;
		}
		
		public void run(){
			ServerSocket listener;
			try {
				listener = new ServerSocket(23456);
				Socket socket = listener.accept();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String str = in.readLine();
				
				if(str.equals("DECLINE")){
					listener.close();
					parent.dispose();
				}
				
				if(str.equals("ACCEPT")){
					System.out.println("GAME ACCEPTED!!!");
					new GameDriver().start();
					// CREATE NEW GAME DRIVER HERE, YEAGH
					listener.close();
				}
					
				parent.dispose();
			} catch (IOException e) {
				parent.dispose();
				e.printStackTrace();
			}
		}
	}
	

	private class timerAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			numDots = (numDots + 1) % 6;
			if(numDots == 0)
				numDots = 1;
			setDots(numDots);
		}
	}

	private void setDots(int n){
		String str = "";
		for(int i = 0;i < n;i++){
			if(i != 0)
				str += " ";
			str += ".";
		}
		dots.setText(str);
		this.revalidate();
	}
}
