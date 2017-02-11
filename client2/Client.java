package ClientServer.client2;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import ClientServer.Message;

public class Client {
	Socket requestSocket;           //socket connect to the server
	final int clientNum = 2;			//unique number for client
	ObjectOutputStream out;         //stream write to the socket
 	ObjectInputStream in;          //stream read from the socket
	final String BASE_PATH = "C:/Users/jerryraj/Desktop/ClientServer/client";
	
	public void Client() {}

	// Thread to receive data from server
    public class readingThr implements Runnable {
        Socket requestSocket;

        public readingThr(Socket requestSocket) {
            this.requestSocket = requestSocket;
        }

        public void run() {
            try {
                in = new ObjectInputStream(requestSocket.getInputStream());
                while (true) {
                    try {
                        Message receivedMsg = (Message) in.readObject();
                        if(receivedMsg.msgType.toLowerCase().contains("message")) {
                            System.out.println("@client"+receivedMsg.sender+":"+receivedMsg.message);
                        }
                        else if(receivedMsg.msgType.toLowerCase().contains("file")) {
                            String fileName = receivedMsg.fileName;
                            String fileType = receivedMsg.fileType;
                            String filePath = BASE_PATH +clientNum+"/"+fileName+"."+fileType;
                            String write_path = new File("").getAbsolutePath()+"/"+fileName+clientNum+"."+fileType;
                            System.out.println("File: " + fileName+"."+fileType + " sent by client" + receivedMsg.sender);
                            BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                            fileOutputStream.write(receivedMsg.fileData);
                            fileOutputStream.flush();
                        }
                        else
                        {
                        	System.out.println("Input format not found..");
                        }

                    } catch (ClassNotFoundException error) {
                        System.out.println("Input format not found..");
                    }
                }
            } catch (IOException e) {

            }
        }
    }
    
	void startClient()
	{
		try{			
			requestSocket = new Socket("localhost", 8000);
			System.out.println("Connected to localhost in port 8000");
			
			new Thread(new readingThr(requestSocket)).start();
			//initialize inputStream and outputStream
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			
			// Send client number
			out.writeObject(clientNum);
			out.flush();
				
			//get Input from standard input
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				System.out.print("Please enter command as a string \n");
				//read a sentence from the standard input
				String message = bufferedReader.readLine();
				Message msgObj = parse(message);
				//Send the sentence to the server
				sendMessage(msgObj);
			}
		}
		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
		} 
		catch ( ClassNotFoundException e ) {
            		System.err.println("Class not found");
        	} 
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//Close connections
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	
	Message parse(String msg) throws IOException, ClassNotFoundException
	{
		if(msg != null || !msg.isEmpty())
		{
			String[] tokens = msg.split("\"");
			int sender = clientNum;
			String msgCast = tokens[0].split(" ")[0];
			String msgType = tokens[0].split(" ")[1];
			String receiver = null;
			String blockCl = null;
			if(msgCast.toLowerCase().contains("unicast"))
			{
				receiver = tokens[2];
			}
			else if(msgCast.toLowerCase().contains("blockcast"))
			{
				blockCl = tokens[2];
			}
			
			String message = null;
			String fileName = null;
			String fileType = null;
			byte[] fileData = null;
			if(msgType.toLowerCase().contains("message"))
			{
				message = tokens[1];
			}
			else if(msgType.toLowerCase().contains("file"))
			{
				String tempName = tokens[1];
				String fullName = tempName.substring(tempName.indexOf('/')+1, tempName.length());
				fileName = fullName.substring(0,fullName.indexOf('.'));
				fileType = fullName.substring(fullName.indexOf('.')+1);
				
				File fd = new File(BASE_PATH + clientNum +"/"+fileName+"."+fileType);
                BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(fd));
                byte[] buffer = new byte[8192];
                fileInputStream.read(buffer);
				fileData = buffer;
			}
			return new Message(sender, receiver, msgCast, msgType, fileName,
						fileType, fileData, blockCl, message);
		}
		return null;
	}
	
	//send a message to the output stream
	void sendMessage(Message msg)
	{
		try{
			if(msg != null)
			{
				//stream write the message
				out.writeObject(msg);
				out.flush();
				System.out.println("Message sent");
			}
			else{
				System.out.println("Invalid message");
			}
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	//main method
	public static void main(String args[])
	{
		Client client = new Client();
		client.startClient();
	}

}
