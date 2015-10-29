package ChatClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;

import GameClient.GameDriver;

public class Invite extends JFrame{

	private final int BG1 = 0x0F5D2C;
	private final int BG2 = 0x297B48;
	
	private String address;
	private String name;
	private PrintWriter serverOut;
	private Socket ChallengerSocket;
	
	private JLabel lblInfo,lblText;
	private JButton btnDecline,btnAccept;
	
	private btnListener btnList;
	
	public Invite(String address,String name,PrintWriter out){
		this.address = address;
		this.name = name;
		this.serverOut = out;
		
		try {
			ChallengerSocket = new Socket(address,23456);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.setTitle("It's On!");
		
		//Name and Address Label
		lblInfo = new JLabel(name + " (" + address + ")",JLabel.CENTER);
		lblInfo.setBounds(0, 20, 400, 40);
		lblInfo.setFont(new Font("Dialog.bold" , Font.BOLD, 20));
		lblInfo.setOpaque(true);
		lblInfo.setBackground(new Color(BG2));
		
		//Challenge Text
		lblText = new JLabel("Has Challendged You!",JLabel.CENTER);
		lblText.setBounds(0, 100, 400, 40);
		lblText.setFont(new Font("Dialog.bold" , Font.BOLD, 20));
		lblText.setOpaque(true);
		lblText.setBackground(new Color(BG2));
		
		// Button Listener
		btnList = new btnListener(this);
		
		//Decline Button
		btnDecline = new JButton("Decline");
		btnDecline.setBackground(new Color(BG2));
		btnDecline.setBounds(new Rectangle(240,200,100,50));
		btnDecline.setFocusPainted(false);
		btnDecline.addActionListener(btnList);
		this.getContentPane().add(btnDecline);
		
		//Accept Button
		btnAccept = new JButton("Accept");
		btnAccept.setBackground(new Color(BG2));
		btnAccept.setBounds(60,200,100,50);
		btnAccept.setFocusPainted(false);
		btnAccept.addActionListener(btnList);
		this.getContentPane().add(btnAccept);
		
		// JFrame Stuff
		this.getContentPane().setLayout(null);
		this.getContentPane().add(lblInfo);
		this.getContentPane().add(lblText);
		
		this.getContentPane().setBackground(new Color(BG1));
		this.setSize(400, 300);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private class btnListener implements ActionListener{
		
		Invite parent;
		
		public btnListener(Invite parent){
			this.parent = parent;
		}

		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == btnDecline){
				
				//Send Decline Message
				try {
					
					PrintWriter out = new PrintWriter(ChallengerSocket.getOutputStream(),true);
					out.println("DECLINE");
					System.out.println("DECLINED CHALLENGE");
					ChallengerSocket.close();
					parent.dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				//parent.dispose();
			}
			
			if(e.getSource() == btnAccept){
				
				try {
					
					PrintWriter out = new PrintWriter(ChallengerSocket.getOutputStream(),true);
					out.println("ACCEPT");
					serverOut.println("ACCEPT " + address);
					System.out.println("ACCEPTED CHALLENGE");
					ChallengerSocket.close();
					new GameDriver().start();
					// Start Game Driver
					parent.dispose();
					
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				parent.dispose();
			}
		}
		
	}
}
