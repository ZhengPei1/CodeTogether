package Network;

import Controller.Controller;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Objects;

/*
This class represents a server
It will
allow clients to connect
verify clients
send message (one to one & one to many)
receive message
 */
public class Server {
    private ArrayList<Socket> activeClients = new ArrayList<>();
    private ArrayList<DataOutputStream> clientConnection = new ArrayList<>();
    private ServerSocket serverSocket;
    
    private boolean shutDown = false;
    
    // constructor
    public Server(String serverPassword, ServerSocket serverSocket) {
        // load server socket
        this.serverSocket = serverSocket;
        
        // initialize server in a separate thread
        new Thread(){
            public void run(){
                initializeServer(serverPassword, serverSocket);
            }
        }.start();
    }
    
    // this method will initialize a serverSocket that's ready to connect with clients
    public void initializeServer(String serverPassword, ServerSocket serverSocket){
        try {
            
            // load new screens - server owner version
            Controller.loadOnline(2);
            
            // prompt messages
            Controller.printToMessageArea("Server is running on IP: " + InetAddress.getLocalHost().getHostAddress());
        
            // create client sockets to accept connection from clients
            while (!shutDown) {
            
                Socket clientSocket;
                int clientPort = 0;
                try {
                    // start acepting clients
                    clientSocket = serverSocket.accept();
                    clientPort = clientSocket.getPort();
                    
                    // prompt to the server owner
                    if(!clientConnection.isEmpty())
                        Controller.printToMessageArea("Client " + clientPort + " connected to server...");
                    else
                        Controller.printToMessageArea("You are now the server owner!");
                    
                } catch (IOException ex) {
                    // If can not connect the client to the server, print error message
                    if(!shutDown){
                        JOptionPane.showMessageDialog(new JFrame(),
                                "<html>Failed to connect new clients to the server<br>" +
                                        "Potential reasons are: <br>" +
                                        "1. Poor Internet connect on the Server side<br>" +
                                        "2. Poor Internet connection on the client side<br>" +
                                        "3. Interrupted thread<br>" +
                                        "</html>",
                                "Connection Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                    continue;
                }
            
            
                // create a new thread to serve the client
                int finalClientPort = clientPort;
                new Thread() {
                    public void run() {
                        DataInputStream input;
                        DataOutputStream output;
                        
                        try {
                            
                            // input and output stream from client
                            input = new DataInputStream(clientSocket.getInputStream());
                            output = new DataOutputStream(clientSocket.getOutputStream());
                            
                            // verify the client
                            if(!activeClients.isEmpty()){
                                Controller.printToMessageArea("Verifying Client...");
                                if(!Objects.equals(serverPassword, input.readUTF())){
                                    Controller.printToMessageArea("Client rejected!");
                                    output.writeUTF("Rejected");
                                    return;
                                }
                                Controller.printToMessageArea("Client accepted!");
                                output.writeUTF("You have been accepted!");
                            }
    
                            // add the client socket to active client
                            clientConnection.add(output);
                            activeClients.add(clientSocket);
                            
                            // the index where this client is stored at
                            int index = clientConnection.size()-1;
                            
                            // after a client joined, sync the current editing file with him
                            output.writeUTF(Controller.getCode());
                            
                            // accept input from client, then broadcast the msg to all the active clients
                            while (true) {
                                broadcastMessage(input.readUTF(), index);
                            }
                        
                        } catch (IOException e) {
                            // client disconnect message
                            Controller.printToMessageArea("Client " + finalClientPort + " disconnected from the server");
                        }
                    }
                }.start();
            }
            
        } catch (IOException e) {
            
            // show error message when server can't start
            try {
                serverSocket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(new JFrame(),
                    "<html>" +
                            "Server closed unexpectedly/Unable to start:<br>" +
                            "Potential reasons are:<br>" +
                            "1. poor internet connection<br>" +
                            "2. using VPN<br>" +
                            "3. firewall is on" +
                            "4. port occupied<br>" +
                            "</html>",
                    "Connection error",
                    JOptionPane.ERROR_MESSAGE);
            Controller.restart();
        }
    
    }
    
    // this method will send message to everyone in the server
    public void broadcastMessage(String msg, int index){
        
        // check if the socket connection is still alive, then print the message
        for(int i = 0; i < activeClients.size(); ++i){
            
            // skip the client that activates the algorithm or the clients that already disconnected from the server
            if(i == index || activeClients.get(i) == null) continue;
            

            // send message to this client
            try {
                clientConnection.get(i).writeUTF(msg);
                clientConnection.get(i).flush();
            } catch (IOException e) {
                // close the socket if the socket is no longer connected
                try {
                    activeClients.get(i).close();
                    activeClients.set(i, null);
                    clientConnection.get(i).close();
                    clientConnection.set(i, null);
                } catch (IOException ex) {
                    System.out.println("can not close client socket - server.java");
                }
            }

            
        }
    }
    
    // this method will shut down the server
    public void shutDownServer(){
        try {
            // close all the client connections
            for(Socket s : activeClients)
                s.close();
            // close all the output streams
            for(DataOutputStream outputStream : clientConnection)
                outputStream.close();
            shutDown = true;
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    
}