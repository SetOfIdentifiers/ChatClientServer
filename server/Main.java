import java.io.IOException;
import java.net.ServerSocket;



public class Main {
    
    public static final int SERVER_DEFAULT_PORT = 2000;
    
    /**
     * State of the art main method
     * @param args
     */
    public static void main(String[] args)
    {
        int port = 0;
        if(args.length == 0)
            port = SERVER_DEFAULT_PORT;
        else if(args.length == 1)
        {
            port = Integer.parseInt(args[1]);
        }
        else
        {
            System.out.println("Invalid arguments");
            System.exit(-1);
        }
            
        ServerApplication serverApp = null;
        
        serverApp = new ServerApplication(port);
        
        new ServerWindow(serverApp);
    }
    
}
