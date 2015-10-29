package ChatClient;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class MainFrame {


	//Layout Stuff
	private final int BG1 = 0x0F5D2C;  //Main Color
	private final int BG2 = 0x297B48;  //Secondary Colors

	// frame is the main JFrame
	public JFrame frame;
	// JPanels hold the swing objects
	private JPanel inputPanel,messagePanel,userPanel;
	// Where chat messages are displayed
	private JTextArea messageArea;
	// Where outgoing messages are typed
	private JTextField textField;
	// Buttons
	private JButton btnExit,btnInvite;
	// List shows names of other clients in chatroom
	private JList<String> userList;
	// Listener used by buttons
	private btnListener btnList;

	// Network Stuff
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;


	// Chat Stuff
	private String userName;
	private ArrayList<String> user_names;

	Invite invite;
	
	public MainFrame(Socket socket){
		this.socket = socket;

		btnList = new btnListener();

		// Creates List to store naems
		user_names = new ArrayList<String>();
		// Gets Client's username from System Computer
		userName = System.getProperty("user.name");


		frame = new JFrame("GVSU ChessChat v0.1");
		frame.getContentPane().setLayout(null);
		setLayout(frame.getContentPane());
		frame.getContentPane().setBackground(new Color(BG1));
		frame.pack();
		frame.repaint();
		frame.setSize(800, 600);
		frame.setResizable(false);

		// Used for when press 'ENTER' on outgoing textfield
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Sends Server the message typed by Client
				out.println("MESSAGE "+userName + ": "+textField.getText());
				
				// Resets the text in the Field
				textField.setText(" ");
			}
		});



	}

	private void setLayout(Container pane){

		// Chat Input Text
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(550,25));
		textField.setBackground(new Color(BG2));
		textField.setBorder(BorderFactory.createEmptyBorder());
		textField.setText(" ");
		textField.setEditable(false);
		inputPanel = new JPanel();
		inputPanel.add(textField);
		inputPanel.setBackground(new Color(BG1));
		inputPanel.setBounds(new Rectangle(20,525,560,35));
		pane.add(inputPanel);

		// Chat Message Area
		messageArea = new JTextArea();
		DefaultCaret caret = (DefaultCaret)messageArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		messageArea.append("Connected to "+socket.getInetAddress()+":"+socket.getPort()+"\n");
		messageArea.setEditable(false);
		messageArea.setBackground(new Color(BG2));
		messageArea.setBorder(BorderFactory.createEmptyBorder());	
		messagePanel = new JPanel();
		messagePanel.setBackground(new Color(BG1));
		JScrollPane sp = new JScrollPane(messageArea);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setPreferredSize(new Dimension(550,490));
		messagePanel.add(sp);
		messagePanel.setBounds(new Rectangle(20,20,560,500));
		pane.add(messagePanel);

		//UserList
		userList = new JList<String>();
		userPanel = new JPanel();
		userList.setBackground(new Color(BG2));
		userList.setPreferredSize(new Dimension(160,375));
		userPanel.add(userList);
		userPanel.setBackground(new Color(BG1));
		userPanel.setBounds(new Rectangle(600,20,175,400));
		pane.add(userPanel);

		//Invite Button
		btnInvite = new JButton("INVITE");
		btnInvite.setBackground(new Color(BG2));
		btnInvite.setBounds(new Rectangle(600,442,175,50));
		btnInvite.setFocusPainted(false);
		btnInvite.addActionListener(btnList);
		pane.add(btnInvite);

		//Exit Button
		btnExit = new JButton("EXIT");
		btnExit.setBackground(new Color(BG2));
		btnExit.setBounds(new Rectangle(600,504,175,50));
		btnExit.addActionListener(btnList);
		btnExit.setFocusPainted(false);
		pane.add(btnExit);
	}
	
	public void run() throws IOException{
		
		// Recieves messages from server
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		// Sends messages to server
		out = new PrintWriter(socket.getOutputStream(),true);
		
		while(true){
			// Get message from server
			String line = in.readLine();
			
			if(line == null)	continue;
			
			// Prints message
			System.out.println("====> " + line);
			
			// How the client reacts to different mesages from the server
			if(line.startsWith("SUBMITNAME")){
				// If it wants name, send name
				out.println(System.getProperty("user.name"));
			} else if (line.startsWith("NAMEACCEPTED")){
				// If it says you are ok, enable messaging
				textField.setEditable(true);
			} else if (line.startsWith("MESSAGE")){
				// If it is a message from another client
				messageArea.append(line.substring(8)+"\n");
			} else if(line.startsWith("USER")){
				// If a new user just joined - make new list and update JList
				user_names.add(line.substring(5));
				userList.setListData(user_names.toArray(new String[user_names.size()]));
				
			} else if(line.startsWith("INVITE")){
				String[] args = line.split(" ");
				invite = new Invite(args[2],args[1],out);
			} else if(line.startsWith("LEAVES")){
				// If a user leaves the chatroom
				String uName = line.substring(7);
				System.out.println(uName);
				for(int i = 0;i < user_names.size();i++){
					if(user_names.get(i).equals(uName)){
						user_names.remove(i);
					}
				}
				userList.setListData(user_names.toArray(new String[user_names.size()]));
			} else {
				// If some unknown message occurs
				messageArea.append("HEYYOPO");
			}
		}
	}

	/*
	 * Actionlister JButtons used for buttons in frame
	 */
	private class btnListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {

			//Exit Button
			if(e.getSource() == btnExit){
				System.exit(0);
			}
			
			//Invite Player
			if(e.getSource() == btnInvite){
				
				// If no player is selected -> return
				if(userList.getSelectedIndex() == -1)
					return;

				// Create new challenge frame
				try {
					new Challenge();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// Issue challenge to selected user through server
				out.println("INVITE "+userList.getSelectedValue());
			}
		}

	}
}
