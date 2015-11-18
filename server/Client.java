import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


/**
 * A client thread
 *
 */
class Client implements Runnable {
    
    public static int BUFFER_MAX_LENGTH = 25000;
    public static String MESSAGE_ENCODING = "UTF-8";

    private ServerApplication serverApp;
    private Thread clientThread;
    private OutputStream out;
    private InputStream in;
    private Socket socket;
    private boolean active = true;
    private final String hostName;
    
    static int count = 0;
    int id = 0;
    
    /**
     * State of the art constructor
     * @param socket
     * @param out
     * @param in
     * @param chat
     */
    Client(ServerApplication serverApp, Socket socket, OutputStream out, InputStream in)
    {
        clientThread = new Thread(this);
        
        this.serverApp = serverApp;
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.id = ++count;
        this.hostName = socket.getInetAddress().getHostName();
        
        clientThread.start();
    }
    
    /**
     * Performs cleanup for this client
     */
    public void deactivate() throws IOException
    {
        active = false;
        socket.close();
    }
    
    /**
     * Sends a message to the client
     * @param message The message to be sent
     */
    void sendMessage(String message)
    {
        try {
            out.write(new String(hostName + ": " + message).getBytes());
        } catch (IOException e) 
        {
            
        }
    }
    
    String receiveMessage() throws IOException
    {
        String message = null;
        byte[] buffer = new byte[BUFFER_MAX_LENGTH];

        if(in.read(buffer, 0, BUFFER_MAX_LENGTH) == -1)
            throw new IOException();
        
        message = new String(buffer, MESSAGE_ENCODING);

        return message;
    }

    /**
     * Thread execution
     */
    public void run() {
        
        System.out.println("Thread " + count + " launching.");
        
        try
        {
            
            String inputLine, outputLine;
            
            outputLine = "";
            
            
            while(!socket.isClosed() || !socket.isConnected() || socket.isBound())
            {
                inputLine = receiveMessage();
                
                if(inputLine != null)
                {
                    outputLine = inputLine;
                    serverApp.broadcastMessage(outputLine);
                }
                
            }
            
            deactivate(); // Client is closed, perform cleanup
            
        } catch (IOException e) {System.out.println("Client exit");}
        
        System.out.println("Client exit");
    }
    
    boolean isActive() { return active; }
    
}
