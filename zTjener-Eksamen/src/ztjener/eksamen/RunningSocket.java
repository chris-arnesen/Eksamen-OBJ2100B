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
    ArrayList<SocketRoom> sr;
        
    
    public RunningSocket() {
        sr = new ArrayList<>();
    }
    
    @Override() 
    public void run() {
        try {
            server = new ServerSocket(port);
                
            while(true) {
                socket = server.accept();
                in = new ObjectInputStream(socket.getInputStream());
                String klientInput = (String)(in.readObject()); // Her får du brukernavnet som blir sendt fra klient
                    
                String[] arrOfStr = klientInput.split(";");
                String type = arrOfStr[0];
                String info = arrOfStr[1];
                if (type.equals("BNAVN")) {
                   //Her kommer funksjoner for dersom et brukernavn blir sendt
                    System.out.println("Dette er et brukernavn.. ps DET FUNKER FOR FAEN" + info);
                } 
                else if (type.equals("MELDING")) {
                    //Her kommer funksjoner for dersom en melding blir sendt
                }
                else if (type.equals("ROM")) {
                    System.out.println("Dette er et rom: " + info + ", " + type);
                    sr.add(new SocketRoom(info, new RunningSocket()));
                }
                    
                //System.out.println("DET FUNKER FOR FAEN " + klientInput);
                in.close();
                socket.close();
            }
        } catch(IOException | ClassNotFoundException ex) {
        }
    }
    
}
