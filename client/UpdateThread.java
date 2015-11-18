import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

class UpdateThread implements Runnable
{
    private boolean active = false; // Thread state
    private Thread t; // Thread reference
    private ClientApplication clientApplication; // Client application reference
    private final JTextArea contentsField; // Reference to a field to update with server broadcasts
    private ClientWindow clientWindow;
    
    /**
     * constructor
     * @param clientApplication
     * @param contentsField
     */
    UpdateThread(ClientWindow clientWindow, ClientApplication clientApplication, JTextArea contentsField)
    {
        this.clientApplication = clientApplication;
        this.contentsField = contentsField;
        this.clientWindow = clientWindow;
        
    }
    
    /**
     * Activates the thread
     */
    public void activate()
    {
        t = new Thread(this);
        active = true;
        t.start();
    }
    
    /**
     * Deactivates the thread
     */
    public void deactivate()
    {
        active = false;
    }

    /**
     * Thread execution
     */
    public void run() {
        
        
        while(active)
        {
            final String fromServer = clientApplication.getMessage();
            if(!fromServer.trim().equals(""))
            {
                 SwingUtilities.invokeLater(new Runnable() {
                     public void run()
                     {
                         contentsField.append(fromServer.trim() + "\n");
                     }
                 });
            }
        
            
        }
        
        try {
            clientApplication.deactivate();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error in disconnecting");
        }
        
        System.out.println("Should not happen");
    }
    
}
