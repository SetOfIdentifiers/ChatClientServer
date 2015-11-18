import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;


public class ServerApplication implements Runnable{
    
    private int port;
    private Thread thread; // Thread reference for the server
    private boolean active = false; // The state of the main server thread
    private ServerSocket serverSocket; // socket for the server
    private OutputStream out; // output stream
    private InputStream in; // input stream
    private volatile ArrayDeque<Client> clientStack = new ArrayDeque<Client>();
    
    
    /**
     * Broadcasts a message to all clients currently connected
     * @param message
     */
    public void broadcastMessage(String message)
    {
        Iterator<Client> clientIterator = clientStack.descendingIterator();
        
        while(clientIterator.hasNext())
        {
            Client client = clientIterator.next();
            
            if(client.isActive())
                client.sendMessage(message);
            else
                clientIterator.remove();
        }
    }
    
    /**
     * Deactivates all client threads
     * @throws IOException 
     */
    private void deactivateClients() throws IOException
    {
        while(!clientStack.isEmpty())
            clientStack.pop().deactivate();
    }
    
    /**
     * Deactivates the server socket
     * @throws IOException
     */
    private void deactivateSocket() throws IOException
    {
        if(serverSocket != null)
            serverSocket.close();
    }
    
    /**
     * Performs cleanup operations
     * @throws IOException
     */
    public void deactivate() throws IOException
    {
        active = false;
        
        deactivateClients();
        deactivateSocket();
    }
    
    /**
     * Performs initialization operations
     * @throws IOException
     */
    public void activate() throws IOException
    {
        serverSocket = new ServerSocket(port);
        active = true;
        
        while(!(thread == null) && thread.isAlive());
        
        thread = new Thread(this);
        thread.start();
        
    }
    
    /**
     * Constructor
     * @param port
     */
    ServerApplication(int port)
    {
        this.port = port;
    }

    @Override
    /**
     * Thread execution cycle
     */
    public void run() {
        
        Socket clientSocket;
        
        try
        {
            while(active && (clientSocket = serverSocket.accept()) != null)
            {
                out = clientSocket.getOutputStream();
                in = clientSocket.getInputStream();
                
                clientStack.push(new Client(this, clientSocket, out, in)); // Keep track of reference for later cleanup    
            }
        
        } catch(IOException e) {}
        
    }
    
    boolean isActive() { return active; }
}
