package Network;

import Controller.Controller;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/*
This class represents a client
It will
Connect to the server
Complete Verification
Receive message
Send message
 */
public class Client {
    
    private Socket clientSocket;
    // input and output
    private DataInputStream input;
    private DataOutputStream output;
    
    // the constructor, it will initialize a client socket and complete verification
    public Client(int serverName, String serverPassword, String IPAddress, boolean neededVerification) {
        try {
            // create client socket
            clientSocket = new Socket(IPAddress, serverName);

            // input and output
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            output = new DataOutputStream(clientSocket.getOutputStream());

            // verification
            if (neededVerification) {
                sendToServer(serverPassword);
                if (input.readUTF().equals("Rejected")) {
                    clientSocket.close();
                    JOptionPane.showMessageDialog(new JFrame(),
                            "<html>" +
                                    "Server rejected your connection request:<br>" +
                                    "Potential reasons are:<br>" +
                                    "1. wrong server name/password<br>" +
                                    "2. server and client are not connected to the same network<br>" +
                                    "</html>",
                            "Connection error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // load new screens - client version
            if (neededVerification)
                Controller.loadOnline(1);

            // prompts
            if (neededVerification) {
                Controller.printToMessageArea("Your identity " + clientSocket.getLocalPort());
                Controller.printToMessageArea("Your machine is running on IP: " + IPAddress);
                Controller.printToMessageArea("Verifying your identity...");
                Controller.printToMessageArea("Verified! you are now connected to the server!");
            }

            // thread that allows the client to accept info from server
            new Thread() {
                public void run() {

                    // accept input from server
                    while (true) {
                        try {
                            Controller.modifyCodeArea(input.readUTF());
                        } catch (IOException e) {
                            try {
                                clientSocket.close();
                                JOptionPane.showMessageDialog(new JFrame(),
                                        "<html>" +
                                                "You have lost the connection with the server:<br>" +
                                                "Potential reasons are:<br>" +
                                                "1. poor internet connection<br>" +
                                                "2. the owner shut down the server<br>" +
                                                "</html>",
                                        "Connection error",
                                        JOptionPane.ERROR_MESSAGE);
                                Controller.restart();
                                return;
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
            }.start();

        } catch (IOException e) {

            // if failed to connect to the server, show warning message
            JOptionPane.showMessageDialog(new JFrame(),
                    "<html>" +
                            "Can not connect to the server:<br>" +
                            "Potential reasons are:<br>" +
                            "1. poor internet connection<br>" +
                            "2. the owner shut down the server/ server didn't start<br>" +
                            "3. wrong ServerName/Password Information<br>" +
                            "4. client and server are not on the same network<br>" +
                            "</html>",
                    "Connection error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    
    // this method will send a message from the client to the server, then the server will show it to everyone
    public void sendToServer(String msg) {
        // send message to server
        try {
            output.writeUTF(msg);
            output.flush();
        } catch (IOException e) {
            // warning message
            try {
                clientSocket.close();
                JOptionPane.showMessageDialog(new JFrame(),
                        "<html>" +
                                "You have lost the connection with the server:<br>" +
                                "Potential reasons are:<br>" +
                                "1. poor internet connection<br>" +
                                "2. the owner shut down the server<br>" +
                                "</html>",
                        "Connection error",
                        JOptionPane.ERROR_MESSAGE);
                ;
                Controller.restart();
                return;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            
        }
    }
    
    // this method will close client connection
    public void disconnect() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}