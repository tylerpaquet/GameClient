package ChatClient;

import java.io.IOException;
import java.net.*;

public class FindServer {

	/* Returns the connected socket to chatroom server
	 * */
	public static Socket getServer(String start,int endStart,int range,int port){
		
		// For all computers in eos lab
		for(int i = endStart;i < endStart+range;i++){
			
			// Creates IP address string
			String add = start + Integer.toString(i);
			System.out.println(add + "...");
			
			try{
				// Try to create socket
				Socket s = new Socket(add,port);
				
				// Only returns socket if it successfully connects
				return s;
				
			} catch (ConnectException e){
				// If connection refused
				System.out.println("Connect Refused");
			} catch (UnknownHostException e){
				// UnknownHost
				System.out.println("Unknown Host");
			} catch (NoRouteToHostException e){
				// Probably means firewall is blocking route
				System.out.println("No Route");
			} catch (IOException e){
				// forgot how this happens
				System.out.println("IOE");
			}
		}
		
		// Returns null if not able to connect to server
		return null;
	}
}
