import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

class ClientWindow extends JFrame
{
    private ClientApplication clientApplication;
    public UpdateThread updateThread;
    
    // Subcomponents
    
    private JPanel mainPanel;
    private JButton submitButton; // The reply button
    private final JTextArea contentsField; // The chat contents field
    private JTextField replyField; // The text that will be sent to the server as an reply
    
    public void updateChat(String message)
    {
        contentsField.append(message);
    }
    
    /**
     * Toggles the connection to the server
     */
    void toggleConnection()
    {
        if(clientApplication.isActive())
            connect();
        else
            disconnect();
    }
    
    /**
     * Attempts to connect to the server
     */
    void connect()
    {
        try {
            clientApplication.activate();
            
            this.setTitle(clientApplication.getHostName() + ":" + clientApplication.getPort());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect");
            System.exit(-1);
        }
        
        updateThread.activate();
    //    SwingUtilities.invokeLater(updateThread);
    }
    
    /**
     * Attempts to disconnect form the server
     */
    void disconnect()
    {
        updateThread.deactivate();
        
        try
        {
            clientApplication.deactivate();
        }
        catch(IOException e1)
        {
            JOptionPane.showMessageDialog(null, "Error in disconnecting");
        }
    }
    
    
    /**
     * constructor
     * @param clientApplication
     */
    ClientWindow(ClientApplication clientApplication)
    {
        this.clientApplication = clientApplication;
        this.setSize(400,400);
        this.setResizable(false);
        this.setLayout(new BorderLayout(10,10));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {
                disconnect();
            }
        });
        
        this.setTitle("Chat Client " + clientApplication.getHostName() + ":" + clientApplication.getPort());
        
        GridLayout grid = new GridLayout(0,2);
        
        grid.setHgap(10);
        
        mainPanel = new JPanel(grid);
            
        contentsField = new JTextArea(10,60);
        contentsField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        contentsField.setWrapStyleWord(false);
        contentsField.setEditable(false);
//        contentsField.setLineWrap(true);
        
        JScrollPane pane = new JScrollPane(contentsField);
        
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        
        
            
        replyField = new JTextField("Write something here");
        
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent e)
            {
                ClientWindow.this.clientApplication.sendMessage(replyField.getText());
            }
                
        });
        
        mainPanel.add(new JLabel("Write message here:"));
        mainPanel.add(replyField);
        mainPanel.add(submitButton);
        
        
        JPanel north = new JPanel();
        north.add(pane);
        this.add(north, BorderLayout.NORTH);
        
        this.add(mainPanel, BorderLayout.SOUTH);
        this.setVisible(true);
        
        pack();
        
        updateThread = new UpdateThread(this, clientApplication, contentsField);
        
        connect();
    }
}
