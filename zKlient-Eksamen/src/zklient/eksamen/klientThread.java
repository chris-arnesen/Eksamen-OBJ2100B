/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zklient.eksamen;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author jacob
 */
public class klientThread implements Runnable{
    Socket socket;
    Klient klientVindu;
    
    // Test: 
    
    DataInputStream input;
    
    public klientThread(Socket socket, Klient klientVindu) {
        this.socket = socket;
        this.klientVindu = klientVindu;
    }
    
    @Override
    public void run() {
        while(true){
            try {
                //in = new ObjectInputStream(socket.getInputStream());
                //String tjenerMelding = (String)in.readObject();
                input = new DataInputStream(socket.getInputStream());
                String tjenerMelding = input.readUTF();
                
                System.out.println(tjenerMelding); //Vet ikke om break trengs, veldig usikker på den
            } catch (IOException ex) {System.out.println("ERROR IO-feil på linje 31-35 i klientThread"); //break;
            } //catch (ClassNotFoundException ex) {System.out.println("ERROR CNF-feil på linje 31-36 i klientThread");}
        }
    }
    
}