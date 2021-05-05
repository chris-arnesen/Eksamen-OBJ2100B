/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztjener.eksamen;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Mats Engesund
 */
public class RunningSocket implements Runnable {
    
    Socket socket;
    ObjectInputStream in;
    DataOutputStream out;
    Tjener tjener;
    
    public RunningSocket(Socket innSocket, Tjener tjener) {
        this.socket = innSocket;
        this.tjener = tjener;
    }
    
    @Override
    public void run() {
        
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            while(true) {
                
                try {
                    String linje = (String)in.readObject();
                    System.out.println(linje);
                    tjener.broadcast(linje);
                }catch (ClassNotFoundException ex) {System.out.println("ERROR på fil connectionThread 36-40");} 
                
            }
        }catch(IOException ex) {System.out.println("ERROR, IO-feil på linje 35-49 på connectionThread");
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {System.out.println("ERROR IO-feil på linje 47-49");}
        }
        
        
    }
        
    public void skrivMelding(String melding) {
        try {
            out.writeUTF(melding);
            out.flush();
        } catch (IOException ex) {System.out.println("ERROR IO-feil på linje 55-57 i ConnectionThread");}
    }
    
    
}
