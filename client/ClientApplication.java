import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * This class handles connections to the chat server
 *
 */
public class ClientApplication {
    
    private static int BUFFER_MAX_LENGTH = 2500;
    private static String MESSAGE_ENCODING = "UTF-8"; // Encoding of received messages
    
    private Socket socket; // Socket
    private OutputStream out; // Output stream from server
    private InputStream in; // Input stream from server
    private int port; // Port that will be used to connect to server
    private String hostName; // Hostname that will be used to connect to the server
    private boolean active = false; // Whether or not the client is connected
    
    int getPort() { return port; }
    String getHostName() { return hostName; }
    
    /**
     * constructor
     * @param hostName
     * @param port
     * @throws IOException
     */
    ClientApplication(String hostName, int port) throws IOException
    {
        this.port = port;
        this.hostName = hostName;
    }
    
    /**
     * Attempts a connection to server
     * @throws IOException
     */
    void activate() throws IOException
    {
        active = true;
        socket = new Socket(hostName, port);
        
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }
    
    /**
     * Sends a message to the server
     * @param Message
     */
    void sendMessage(String Message)
    {
        
        try
        {
            out.write(Message.getBytes());
        } catch(Exception e) 
        { 
            JOptionPane.showMessageDialog(null, "Unable to send message");
        }
        
    }
    
    /**
     * Attempts to get a message from the server
     * @return fromServer
     */
    String getMessage()
    {
        String fromServer = null;
        byte[] buffer = new byte[BUFFER_MAX_LENGTH];
        
        try
        {
            in.read(buffer, 0, BUFFER_MAX_LENGTH);
            fromServer = new String(buffer, MESSAGE_ENCODING);
        } catch(Exception e) 
        { 
            JOptionPane.showMessageDialog(null, "Unable to receive message");
        }
        
        return fromServer;
    }
    
    /**
     * Deactivates the socket
     * @throws IOException
     */
    void deactivate() throws IOException
    {
        active = false;
        
        if(socket != null)
        {
            socket.close();
            System.out.println("Closed socket");
        }
            
    }
    
    boolean isActive() { return active; }

}
