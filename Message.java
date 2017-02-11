package ClientServer;
import java.io.*;

// Class to implement format of communication packet between Server and Clients
public class Message implements Serializable{
    public int sender;
    public String receiver;
    public String msgCast;
    public String msgType;
    public String fileName;
    public String fileType;
    public byte[] fileData;      
    public String blockCl;
    public String message;
    public Message(int sender,String receiver,String msgCast,String msgType,String fileName,
                   String fileType, byte[] fileData,String blockCl, String message)
    {                   
        this.sender = sender;
        this.receiver = receiver;
        this.msgCast = msgCast;
        this.msgType = msgType;        
        this.fileName = fileName;
        this.fileType = fileType;    
        this.fileData = fileData;
        this.blockCl = blockCl;
        this.message = message;
    }

}