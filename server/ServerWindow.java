import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Creates a small window with a button that deactivates the server socket
 *
 */
public class ServerWindow extends JFrame
{
    private static String SERVER_ACTIVATED_LABEL_TEXT = "Server is running";
    private static String SERVER_DEACTIVATED_LABEL_TEXT = "Server is not running";
    private static String SERVER_ACTIVATED_BUTTON_TEXT = "Deactivate";
    private static String SERVER_DEACTIVATED_BUTTON_TEXT = "Activate";
    private static String WINDOW_TITLE = "Chat Server";
    private static String ACTIVATION_ERROR_MESSAGE = "Error in activation";
    private static String DEACTIVATION_ERROR_MESSAGE = "Error in deactivation";
    
    private JLabel serverStatusLabel; // A reference to a label that displays the status of the server
    private JButton toggleServerButton; // A reference to a button that will run or exit the server
    private ServerApplication serverApp; // A reference to serverapplication so this window is able to close sockets
    
    
    /**
     * Actives or deactivates the server depending on its current state
     */
    private void toggleServer()
    {
        if(serverApp.isActive())
            deactivate();
        else
            activate();
    }
    
    /**
     * Actives the server application
     */
    private void activate()
    {
        try
        {
            serverApp.activate();
            serverStatusLabel.setText(SERVER_ACTIVATED_LABEL_TEXT);
            toggleServerButton.setText(SERVER_ACTIVATED_BUTTON_TEXT);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, ACTIVATION_ERROR_MESSAGE);
        }
    }
    
    /**
     * Invokes cleanup functionality from the server application
     */
    private void deactivate()
    {
        try{
            
            serverApp.deactivate();
            serverStatusLabel.setText(SERVER_DEACTIVATED_LABEL_TEXT);
            toggleServerButton.setText(SERVER_DEACTIVATED_BUTTON_TEXT);
        } catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, DEACTIVATION_ERROR_MESSAGE);
        }
        
    }
    
    /**
     * Constructor
     */
    ServerWindow(ServerApplication serverApp)
    {
        this.setSize(400,80);
        this.setTitle(WINDOW_TITLE);
        this.setResizable(false);
        this.setLayout(new BorderLayout(10,10));
        this.setLocationRelativeTo(null);
        this.serverApp = serverApp;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent e)
            {
                ServerWindow.this.deactivate();
            }
            
        });
        
        serverStatusLabel = new JLabel(SERVER_DEACTIVATED_LABEL_TEXT);
        toggleServerButton = new JButton(SERVER_DEACTIVATED_BUTTON_TEXT);
        toggleServerButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent event) {
                ServerWindow.this.toggleServer();
            }        
        });
        
        this.add(serverStatusLabel, BorderLayout.NORTH);
        this.add(toggleServerButton, BorderLayout.SOUTH);
        this.show();
    }

}
