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
    Commando type;
    
    String brukernavn ="";
    
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
                    
                    String[] splitString = linje.split(";");
                    String typeInput = splitString[0];
                    String beskjed = splitString[1];
                    
                    //Skiller mellom type input fra brukeren. dvs: er det en melding, brukernavn osv..
                    if (typeInput.equals("BNAVN")) {
                        brukernavn = beskjed;
                        System.out.println("Ny bruker med navn: " + brukernavn);
                    } 
                    
                    else if (typeInput.equals("MELDING")) {
                        tjener.lastOppMelding(beskjed, brukernavn); //Laster opp meldingen til databasen
                        String meldingTilAlle = "[" + brukernavn + "] " + beskjed; 
                        tjener.broadcast(typeInput + ";" + meldingTilAlle);
                    }
                    
                    else if (typeInput.equals("ROM")) {
                        // blablabla lager rom
                        String meldingTilAlle = type.CREATE.name() + ";" + beskjed;
                        tjener.broadcast(meldingTilAlle);
                    }
                    
                    else if (typeInput.equals("JOIN")) {
                        System.out.println(beskjed + " vil joine " + splitString[2]);
                    }
                    
                    //tjener.broadcast(linje);
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
