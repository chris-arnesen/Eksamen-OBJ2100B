/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztjener.eksamen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Mats Engesund
 */
public class RunningSocket extends Thread {
    
    static int port = 8000;
    static ObjectOutputStream out;
    static ObjectInputStream in;
    static ServerSocket server;
    static Socket socket;
    
    protected ArrayList<SocketRoom> rooms; 
    
    
    public RunningSocket() {
        rooms = new ArrayList<>();
    }
        
    @Override() 
    public void run() {
        try {
            server = new ServerSocket(port);
                            
            while(true) {
                socket = server.accept();
                in = new ObjectInputStream(socket.getInputStream());
                String bNavn = (String)(in.readObject()); // Her får du brukernavnet som blir sendt fra klient
                
                if(bNavn.equals("Nytt chatroom "))
                    System.out.println(bNavn);

                in.close();
                socket.close();
            }
        } catch(IOException | ClassNotFoundException ex) {
        }
    }
    
}
