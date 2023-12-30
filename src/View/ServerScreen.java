package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.UnknownHostException;

// this is the server screen that allows the user to join/create a server
public class ServerScreen {
    
    private JFrame frame = new JFrame();
    
    private JTabbedPane tabbedPane = new JTabbedPane();
    
    
    // create server panel
    private JPanel createServerPane = new JPanel();
    private JTextField serverNameFieldCreateServer = new JTextField();
    private JTextField passwordFieldCreateServer = new JTextField();
    private JTextField IPAddressCreateServer = new JTextField();
    private JButton createServerButton = new JButton("Create Server");
    
    // join server panel
    private JPanel joinServerPane = new JPanel();
    private JTextField serverNameFieldJoinServer = new JTextField();
    private JTextField passwordFieldJoinServer = new JTextField();
    private JTextField IPAddressJoinServer = new JTextField();
    private JButton joinServerButton = new JButton("Join Server");
    
    // generate a new server socket
    ServerSocket serverSocket;
    {
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    // initializes server screen
    public ServerScreen(){
        // set up JFrame
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setContentPane(new JLabel(new ImageIcon("resources/background.jpg")));
        frame.setTitle("codeTogether");
        frame.setLayout(null);

        // set up icon
        try {
            frame.setIconImage(ImageIO.read(new File("resources/codeTogetherIcon.png")));
        } catch (IOException e) {
            System.out.println("can not load icon");
        }
        
        // set up the create server panel
        createServerPane.setLayout(null);
        createServerPane.setOpaque(false);
        createServerPane.setBounds(0, 0, 600, 400);
        
        JLabel prompt1 = new JLabel("Server Name: ", JLabel.CENTER);
        prompt1.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        prompt1.setForeground(Color.white);
        prompt1.setBounds(20, 25, 250, 40);
        createServerPane.add(prompt1);
        
        JLabel prompt2 = new JLabel("Server IP: ", JLabel.CENTER);
        prompt2.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        prompt2.setForeground(Color.white);
        prompt2.setBounds(20, 90, 250, 40);
        createServerPane.add(prompt2);

        JLabel prompt3 = new JLabel("Server Password: ", JLabel.CENTER);
        prompt3.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        prompt3.setForeground(Color.white);
        prompt3.setBounds(20, 150, 250, 40);
        createServerPane.add(prompt3);
        
        createServerButton.setBounds(150, 240, 300, 50);
        createServerButton.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        createServerPane.add(createServerButton);
        
        // set up input fields
        serverNameFieldCreateServer.setBounds(300, 25, 250, 40);
        serverNameFieldCreateServer.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
        serverNameFieldCreateServer.setBackground(Color.gray);

        // check if there's a valid port to use, if so, set it as server password
        int possiblePort = serverSocket.getLocalPort();
        if(possiblePort == -1)
            serverNameFieldCreateServer.setText("Sorry, some unknown error occurred. Please reload the program.");
        else
            serverNameFieldCreateServer.setText(Integer.toString(possiblePort));
        serverNameFieldCreateServer.setEditable(false);
        createServerPane.add(serverNameFieldCreateServer);

        IPAddressCreateServer.setBounds(300, 90, 250, 40);
        IPAddressCreateServer.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
        IPAddressCreateServer.setBackground(Color.gray);
        try {
            IPAddressCreateServer.setText(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            IPAddressCreateServer.setText("can not detect your ip");
            createServerButton.setVisible(false);
        }
        IPAddressCreateServer.setEditable(false);
        createServerPane.add(IPAddressCreateServer);
    
        passwordFieldCreateServer.setBounds(300, 150, 250, 40);
        passwordFieldCreateServer.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
        createServerPane.add(passwordFieldCreateServer);

        // set up join server panel
        joinServerPane.setLayout(null);
        joinServerPane.setOpaque(false);
        joinServerPane.setBounds(0, 0, 600, 400);
    
        JLabel prompt4 = new JLabel("Server Name: ", JLabel.CENTER);
        prompt4.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        prompt4.setForeground(Color.white);
        prompt4.setBounds(20, 25, 250, 40);
        joinServerPane.add(prompt4);

        JLabel prompt5 = new JLabel("IP address: ", JLabel.CENTER);
        prompt5.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        prompt5.setForeground(Color.white);
        prompt5.setBounds(20, 90, 250, 40);
        joinServerPane.add(prompt5);
        
        JLabel prompt6 = new JLabel("Server Password: ", JLabel.CENTER);
        prompt6.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        prompt6.setForeground(Color.white);
        prompt6.setBounds(20, 150, 250, 40);
        joinServerPane.add(prompt6);
    
        joinServerButton.setBounds(150, 240, 300, 50);
        joinServerButton.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        joinServerPane.add(joinServerButton);
    
        // set up input fields
        serverNameFieldJoinServer.setBounds(300, 25, 250, 40);
        serverNameFieldJoinServer.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
        joinServerPane.add(serverNameFieldJoinServer);
    
        passwordFieldJoinServer.setBounds(300, 150, 250, 40);
        passwordFieldJoinServer.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
        joinServerPane.add(passwordFieldJoinServer);

        IPAddressJoinServer.setBounds(300, 90, 250, 40);
        IPAddressJoinServer.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
        try {
            IPAddressJoinServer.setText(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            IPAddressJoinServer.setText("can not detect your ip");
        }
        joinServerPane.add(IPAddressJoinServer);
        
        // add the tabbed pane to the frame
        tabbedPane.setBounds(0, 0, 600, 400);
        tabbedPane.addTab("Create Server", createServerPane);
        tabbedPane.addTab("Join Server", joinServerPane);
        tabbedPane.setOpaque(false);
        frame.add(tabbedPane);
        
        // set up the frame
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
    
    
    // when changing to online mode, they can no longer open/join server, but they can still view server info
    // mode 1 - client, mode 2 - server owner
    public void onlineMode(int mode){
        // disable edit
        serverNameFieldCreateServer.setEditable(false);
        serverNameFieldCreateServer.setBackground(Color.gray);
        IPAddressCreateServer.setEditable(false);
        IPAddressCreateServer.setBackground(Color.gray);
        passwordFieldCreateServer.setEditable(false);
        passwordFieldCreateServer.setBackground(Color.gray);
        createServerButton.setVisible(false);
        
        serverNameFieldJoinServer.setEditable(false);
        serverNameFieldJoinServer.setBackground(Color.gray);
        IPAddressJoinServer.setEditable(false);
        IPAddressJoinServer.setBackground(Color.gray);
        passwordFieldJoinServer.setEditable(false);
        passwordFieldJoinServer.setBackground(Color.gray);
        joinServerButton.setVisible(false);
        
        // remove useless tab based on mode
        if(mode == 1)
            tabbedPane.remove(0);
        else
            tabbedPane.remove(1);
    
        tabbedPane.setTitleAt(0, "Server Info ");
    }
    
    // getters and setters
    public JFrame getFrame() {
        return frame;
    }
    
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
    
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
    
    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
    
    public JPanel getCreateServerPane() {
        return createServerPane;
    }
    
    public void setCreateServerPane(JPanel createServerPane) {
        this.createServerPane = createServerPane;
    }
    
    public JTextField getServerNameFieldCreateServer() {
        return serverNameFieldCreateServer;
    }
    
    public void setServerNameFieldCreateServer(JTextField serverNameFieldCreateServer) {
        this.serverNameFieldCreateServer = serverNameFieldCreateServer;
    }
    
    public JTextField getPasswordFieldCreateServer() {
        return passwordFieldCreateServer;
    }
    
    public void setPasswordFieldCreateServer(JTextField passwordFieldCreateServer) {
        this.passwordFieldCreateServer = passwordFieldCreateServer;
    }
    
    public JButton getCreateServerButton() {
        return createServerButton;
    }
    
    public void setCreateServerButton(JButton createServerButton) {
        this.createServerButton = createServerButton;
    }
    
    public JPanel getJoinServerPane() {
        return joinServerPane;
    }
    
    public void setJoinServerPane(JPanel joinServerPane) {
        this.joinServerPane = joinServerPane;
    }
    
    public JTextField getServerNameFieldJoinServer() {
        return serverNameFieldJoinServer;
    }
    
    public void setServerNameFieldJoinServer(JTextField serverNameFieldJoinServer) {
        this.serverNameFieldJoinServer = serverNameFieldJoinServer;
    }
    
    public JTextField getPasswordFieldJoinServer() {
        return passwordFieldJoinServer;
    }
    
    public void setPasswordFieldJoinServer(JTextField passwordFieldJoinServer) {
        this.passwordFieldJoinServer = passwordFieldJoinServer;
    }
    
    public JButton getJoinServerButton() {
        return joinServerButton;
    }
    
    public void setJoinServerButton(JButton joinServerButton) {
        this.joinServerButton = joinServerButton;
    }
    
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
    
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public JTextField getIPAddressCreateServer() {
        return IPAddressCreateServer;
    }

    public void setIPAddressCreateServer(JTextField IPAddressCreateServer) {
        this.IPAddressCreateServer = IPAddressCreateServer;
    }

    public JTextField getIPAddressJoinServer() {
        return IPAddressJoinServer;
    }

    public void setIPAddressJoinServer(JTextField IPAddressJoinServer) {
        this.IPAddressJoinServer = IPAddressJoinServer;
    }
}
