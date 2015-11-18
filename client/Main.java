import java.io.IOException;

import javax.swing.JOptionPane;


public class Main {
    
    public static final int DEFAULT_PORT = 2000;
    public static final String DEFAULT_HOSTNAME = "127.0.0.1";
    
    /**
     * main method
     * @param args
     */
    public static void main(String[] args)
    {    
        ClientApplication clientApp = null;
        
        String hostName = "";
        int port = 0;
        
        if(args.length == 0)
        {
            hostName = DEFAULT_HOSTNAME;
            port = DEFAULT_PORT;
        }
        else if(args.length == 1)
        {
            hostName = args[0];
            port = DEFAULT_PORT;
        }
        else if(args.length == 2)
        {
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        }
        else
        {
            System.out.println("Invalid arguments.");
            System.exit(0);
        }
        
        
        try
        {
            clientApp = new ClientApplication(hostName, port);
        }catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Connection failed!");
            System.exit(-1);
        }
        
        new ClientWindow(clientApp);
    }
}
