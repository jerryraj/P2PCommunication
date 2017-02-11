package ClientServer;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import ClientServer.Message;

public class Server {

	final int sPort = 8000;   //The server will be listening on this port number
	ServerSocket listener;	  //Socket connection;
	
	Handler[] clients = new Handler[40];    //Handler for accepted socket connection
	
	
	public static void main(String[] args) throws Exception {
		// Start server
		Server server = new Server();
		server.startServer();
	}
	
	void startServer() throws Exception 
	{	
		try {
        listener = new ServerSocket(sPort);
		System.out.println("The server is running."); 
		int clientNum = 0;
		
		while(true) {
			
			clients[clientNum] = new Handler(listener.accept(), clients, clientNum+1);							
			new Thread(clients[clientNum]).start();
			clientNum++;
			System.out.println("Client client" + clientNum + " connected!");			
			}
		} finally {
				listener.close();
		}  
    	
	}

		/**
     	* A handler thread class.  Handlers are spawned from the listening
     	* loop and are responsible for dealing with a single client's requests.
     	*/
    	private static class Handler implements Runnable {
			private Socket connection;
        	private Handler[] clients;
        	private ObjectInputStream in;	//stream read from the socket
        	private ObjectOutputStream out; //stream write to the socket
        	private int ClientNo;			//The index number of the client

        	public Handler(Socket connection, Handler[] clients, int no) {
            	this.connection = connection;
            	this.clients = clients;
	    		this.ClientNo = no;
        	}

        public void run() {
 		try{
			//initialize Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			try {
                    ClientNo = (Integer) in.readObject();
                }
                catch(ClassNotFoundException e){
					//ignore exception
                }
			try{
				while(true)
				{
					//receive the message sent from the client
					Message message = (Message)in.readObject();
					//show the message to the user
					if(message.msgCast.toLowerCase().contains("broadcast"))
					{
						int senderCl = message.sender;	
						System.out.println("client" + senderCl + " broadcasted " + message.msgType);
						
						for(int i = 0; i < clients.length; i++)
						{
							if((clients[i] != null) && (clients[i].ClientNo == senderCl)) continue;
							if(clients[i] != null)
							clients[i].sendMessage(message);
						}
					}
					else if(message.msgCast.toLowerCase().contains("unicast"))
					{
						int senderCl = message.sender;	
						int receiverCl = getNumber(message.receiver);
						System.out.println("client" + senderCl + " unicast " + message.msgType + " to client" + receiverCl);
						for(int i = 0; i < clients.length; i++)
						{
							if((clients[i] != null) && (clients[i].ClientNo == receiverCl)) 
							clients[i].sendMessage(message);
						}
					}
					else if(message.msgCast.toLowerCase().contains("blockcast"))
					{
						int senderCl = message.sender;	
						int blockedCl = getNumber(message.blockCl);
						System.out.println("client" + senderCl + " blockcast " + message.msgType + " excluding client" + blockedCl);
						for(int i = 0; i < clients.length; i++)
						{
							if ((clients[i] != null) &&(clients[i].ClientNo == blockedCl || clients[i].ClientNo == senderCl)) continue;
							if(clients[i] != null)
							clients[i].sendMessage(message);
						}
					}	
				}
			}
			catch(ClassNotFoundException classnot){
					System.err.println("Data received in unknown format");
				}
		}
		catch(IOException ioException){
			System.out.println("Disconnect with Client " + ClientNo);
		}
		finally{
			//Close connections
			try{
				in.close();
				out.close();
				connection.close();
			}
			catch(IOException ioException){
				System.out.println("Disconnect with Client " + ClientNo);
			}
		}
	}

	//send a message to the output stream
	public void sendMessage(Message msg)
	{
		try{
			this.out.writeObject(msg);
			this.out.flush();
			
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//get client number from the name
    public static int getNumber(String clientName){

        int lastIndex = clientName.length()-1;
        int number = 0, place = 1;
        while(Character.isDigit(clientName.charAt(lastIndex))){
            number = place * clientName.charAt(lastIndex)-'0' + number;
            place *= 10;
            lastIndex--;
        }
        return number;
    }
	}

}


