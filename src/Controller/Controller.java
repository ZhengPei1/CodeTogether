package Controller;

import Model.*;
import Network.Client;
import Network.Server;
import View.CodeScreen.CodeScreen;
import View.SettingScreen;
import View.Tutorial;
import View.FileChoosers.DeleteFile;
import View.FileChoosers.OpenProject;
import View.CodeScreen.FileGeneration;
import View.HistoryScreen;
import View.ServerScreen;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/*
This class enables data to transfer between View and Model
Some variables and functions in this class are declared as public & static so the other classes can access them
directly without creating another instance of Controller(which shouldn't happen)
 */
public class Controller {
    
    // screens and models
    private static CodeScreen codeScreen;
    private static ServerScreen serverScreen = new ServerScreen();
    private static OpenProject openProject = new OpenProject();
    private static CursorUpdate cursorUpdate;
    private static CodeExecution codeExecution;
    private static SettingScreen settingScreen;
    
    
    // server and client, if a person is server owner, then server == null would be false. Same for client
    private static Server server = null;
    private static Client client = null;
    private static String rootDirectory;
    public static String fileEditing;
    
    // constructor that initializes the offline version of the program
    public Controller(){
        // initialize code screen with the return value from file chooser
        rootDirectory = openProject.chosenDirectory();
        fileEditing = rootDirectory +  File.separator + "src" + File.separator + "Main.java";
        codeExecution = new CodeExecution(rootDirectory);
        codeScreen = new CodeScreen(rootDirectory, 0);
        cursorUpdate = new CursorUpdate(codeScreen.getCodeShareArea().getText().length(), 0, codeScreen.getCodeShareArea().getText());
        codeScreen.getFrame().setVisible(true);

        // initialize setting
        Setting.initializeSetting();
        settingScreen = new SettingScreen();

        // start local history saving
        new Thread(){
            public void run() {
                Timer timer = new Timer(Setting.HISTORY_SAVE_INTERVAL * 60000,null);
                timer.addActionListener( new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // do not save local history for client
                        if(client == null || server != null)
                            LocalHistory.saveHistory(fileEditing, rootDirectory);

                        // change timer interval
                        timer.setDelay(Setting.HISTORY_SAVE_INTERVAL * 60000);
                    }
                });

                timer.setInitialDelay(0);
                timer.setRepeats(true);
                timer.start();
            }
        }.start();
        
        // open the tutorial screen
        new Tutorial();
    
        initializeCodeScreen();
        initializeServerScreen();
    }
    
    // this method sets up the action listeners for the buttons in the codeScreen
    private static void initializeCodeScreen(){
        // save button
        codeScreen.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Utility.saveCode(fileEditing, codeScreen.getCodeShareArea().getText())){
                    printToMessageArea("successfully saved to file " + fileEditing );
                }else{
                    printToMessageArea("can not save to file " + fileEditing );
                }
            }
        });
        
        // new file button
        codeScreen.getNewFileButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FileGeneration(rootDirectory, 0);
            }
        });
    
        // run button
        codeScreen.getRunCodeButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // save code - client
                if(client != null && server == null){
                    // extract the class name first
                    String fileName = Utility.extractClassName(codeScreen.getCodeShareArea().getText());
                    if(fileName == null){
                        // tell the user we didn't find the class name
                        Controller.printToMessageArea("unable to extract class name, please make sure to write your class as:\n" +
                                "public class + className{\n" +
                                "   code\n" +
                                "}\n");
                        return;
                    }
                    fileEditing = rootDirectory + File.separator + "onlineTemp" + File.separator + fileName;

                    // save the code to the online temp directory
                    if(Utility.saveCode(fileEditing, codeScreen.getCodeShareArea().getText())){
                        printToMessageArea("successfully saved to file " + fileEditing );
                    }else{
                        printToMessageArea("can not save to file " + fileEditing );
                    }
                }

                // save code - server owner, offline
                else if(Setting.SAVE_BEFORE_RUN)
                    if(Utility.saveCode(fileEditing, codeScreen.getCodeShareArea().getText())){
                        printToMessageArea("successfully saved to file " + fileEditing );
                    }else{
                        printToMessageArea("can not save to file " + fileEditing );
                    }
                
                // run code
                new Thread(){
                    public void run(){
                        if(client != null && server == null)
                            codeExecution.runCode(rootDirectory + File.separator + "onlineTemp", fileEditing);
                        else
                            codeExecution.runCode(rootDirectory + File.separator + "src", fileEditing);
                        updateTreeView();
                    }
                }.start();

            }
        });

        // file history button
        codeScreen.getHistoryButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalHistory.loadHistory(fileEditing, rootDirectory);
            }
        });

        // setting button
        codeScreen.getSettingButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingScreen.getFrame().setVisible(true);
            }
        });
        
        // invite button
        codeScreen.getInviteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverScreen.getFrame().setVisible(true);
            }
        });
        
        // disconnect button
        codeScreen.getDisconnectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(client != null)
                    client.disconnect();
                
                if(server != null)
                    server.shutDownServer();
                
                // do not need to call restart because the error message in the Server/Client will restart
               // restart();
            }
        });
        
        // delete button
        codeScreen.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteFile(rootDirectory, fileEditing);
            }
        });
        
        // export button
        codeScreen.getExportButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FileGeneration(rootDirectory, 1);
            }
        });
        
        // tutorial button
        codeScreen.getHelpButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Tutorial();
            }
        });
        
        // caret listener to help keep track of the cursor position on the text field
        codeScreen.getCodeShareArea().addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
               cursorUpdate.setOldCursorPosition(codeScreen.getCodeShareArea().getCaretPosition(), codeScreen.getCodeShareArea().getText());
            }
        });
        
        // key listener to trigger synchronization algorithm whenever a client started typing
        codeScreen.getCodeShareArea().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(client != null)
                    client.sendToServer(codeScreen.getCodeShareArea().getText());
            }
        });
        
        
        // tree listener to control file update, and trigger synchronization
        if(codeScreen.getFileExplorer() != null){
            codeScreen.getFileExplorer().getTree().addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    
                    
                    // check the new file the user has clicked on
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) codeScreen.getFileExplorer().getTree().getLastSelectedPathComponent();
            
                    if(node == null)
                        return;
            
                    String fileName = node.toString();
            
                    // if it's a java file, read the info inside and cast it onto the codeScreen
                    if(fileName.contains(".java") ){
                        
                        // save the code first
                        if(Setting.SAVE_BEFORE_SWITCH)
                            if(Utility.saveCode(fileEditing, codeScreen.getCodeShareArea().getText())){
                                printToMessageArea("successfully saved to file " + fileEditing );
                            }else{
                                printToMessageArea("can not save to file " + fileEditing );
                            }

                        // create a file history entry for the closed file
                        LocalHistory.saveHistory(fileEditing, rootDirectory);
                        
                        // start reading the code
                        File codeSource = new File(rootDirectory + File.separator + "src" + File.separator + fileName);
                        Controller.fileEditing = codeSource.getAbsolutePath();
                        Controller.modifyCodeArea(Utility.readCode(codeSource.getAbsolutePath()));

                        // create a file history entry for the newly opened file
                        LocalHistory.saveHistory(fileEditing, rootDirectory);
                
                        // synchronize if the program is connecting to a server
                        if(client != null)
                            client.sendToServer(codeScreen.getCodeShareArea().getText());
                    }
                    else if(fileName.equals("src")){
                        //do nothing
                    }
                    else{
                        Controller.printToMessageArea("This program can only edit java file");
                    }
                }
            });
        }
    }
    
    // this method sets up the action listener in the server screen
    private static void initializeServerScreen(){
        
        // create server button
        serverScreen.getCreateServerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serverPassword = serverScreen.getPasswordFieldCreateServer().getText();
                
                // check if the server password is valid
                if(!Utility.isValidServerPassword(serverPassword)){
                    JOptionPane.showMessageDialog(new JFrame(),
                            "<html>" +
                                    "Server Password can only include alphabets and numbers, " +
                                    "make sure your server password didn't include any space & " +
                                    "it's not empty" +
                                    "</html>",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                
                // initialize server with IP and socket
                server = new Server(serverPassword, serverScreen.getServerSocket());

                Timer waitTimer = new Timer(1000,  null);
                waitTimer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(server != null){
                            client = new Client(serverScreen.getServerSocket().getLocalPort(), serverScreen.getPasswordFieldCreateServer().getText(), serverScreen.getIPAddressJoinServer().getText(), false);
                            waitTimer.stop();
                        }
                    }
                });
                waitTimer.start();

            }
        });
        
        // join server button
        serverScreen.getJoinServerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if the server name and password are valid
                String serverName = serverScreen.getServerNameFieldJoinServer().getText();
                String serverPassword = serverScreen.getPasswordFieldJoinServer().getText();
                if(!Utility.isValidServerName(serverName) || !Utility.isValidServerPassword(serverPassword)){
                    JOptionPane.showMessageDialog(new JFrame(),
                            "<html>" +
                                    "Server password can only include alphabets and numbers, " +
                                    "and Server name can only include numbers, " +
                                    "make sure your server name and password doesn't include any space & " +
                                    "it's not empty" +
                                    "</html>",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                client = new Client((Integer.parseInt(serverName)), serverPassword, serverScreen.getIPAddressJoinServer().getText(), true);
                
            }
        });
    }
    
    // this function will load two different types of screens - for client or server owner
    // mode 1 - client, mode 2 - server owner
    public static void loadOnline(int mode){
    
        // load the new coding screen/Server Screen
        codeScreen.getFrame().dispose();
        codeScreen = new CodeScreen(rootDirectory, mode);
        initializeCodeScreen();
        codeScreen.getFrame().setVisible(true);
        
        // make server screen invisible
        serverScreen.getFrame().setVisible(false);
    
        serverScreen.onlineMode(mode);
        
    }
    
    
    // this function will return the code in the codeShare area
    public static String getCode(){
        return codeScreen.getCodeShareArea().getText();
    }
    
    // call this function to update code area with new code, do not create a new instance of controller
    public static void modifyCodeArea(String code){
        codeScreen.getCodeShareArea().setText(code);
        codeScreen.getCodeShareArea().setCaretPosition(cursorUpdate.findNewCursorPos(codeScreen.getCodeShareArea().getText()));
    }
    
    // call this function to append new messages at the end of the console
    public static void printToMessageArea(String msg) {
        codeScreen.getMessageTextArea().append(msg + "\n");
    }
    
    // call this function to update tree view, do not create a new instance of controller
    public static void updateTreeView(){
        if(client == null || server != null)
            codeScreen.getFileExplorer().update(new File(rootDirectory));

    }
    
    // restart function will allow the user to choose another project and start everything over again - offline mode
    public static void restart(){
        
        // reset variables
        if(codeScreen !=null )
            codeScreen.getFrame().dispose();
        if(serverScreen != null)
            serverScreen.getFrame().dispose();
    
        codeScreen = null;
        serverScreen = new ServerScreen();
        openProject = new OpenProject();
        cursorUpdate = null;
        codeExecution = null;
        server = null;
        client = null;
        rootDirectory = null;
        fileEditing = null;
        
        
        // reset screens and models
        rootDirectory = openProject.chosenDirectory();
        fileEditing = rootDirectory + File.separator + "src" + File.separator + "Main.java";
        codeExecution = new CodeExecution(rootDirectory);
        codeScreen = new CodeScreen(rootDirectory, 0);
        cursorUpdate = new CursorUpdate(codeScreen.getCodeShareArea().getText().length(), 0, codeScreen.getCodeShareArea().getText());
        codeScreen.getFrame().setVisible(true);
    
        initializeCodeScreen();
        initializeServerScreen();
    }
}
