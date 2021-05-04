/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztjener.eksamen;

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
public class RunningSocket extends Thread {
    
    static int port = 8000;
    static ObjectOutputStream out;
    static ObjectInputStream in;
    static ServerSocket server;
    static Socket socket;
    ArrayList<ClientHandler> romListe;
    String bnavn;
        
    
    public RunningSocket() {
        romListe = new ArrayList<>();
    }
    
    @Override() 
    public void run() {
        try {
            server = new ServerSocket(port);
            String bruker = "";    
            while(true) {
                socket = server.accept();
                in = new ObjectInputStream(socket.getInputStream());
                String klientInput = (String)(in.readObject()); // Her får du melding som blir sendt fra klient
                    
                String[] arrOfStr = klientInput.split(";");
                String type = arrOfStr[0];
                String info = arrOfStr[1];
                
                if (type.equals("BNAVN")) {
                   // Her kommer funksjoner for dersom et brukernavn blir sendt
                    bruker = info;
                    System.out.println("Dette er et brukernavn.. ps DET FUNKER FOR FAEN" + info);
                    this.bnavn = info; 
                } 
                
                else if (type.equals("MELDING")) {
                    // Her kommer funksjoner for dersom en melding blir sendt
                    Tjener.lastOppMelding(info, bruker);
                    System.out.println("Melding ble sendt!");
                }
                
                else if (type.equals("ROM")) {
                    // Her kommer funksjoner for dersom et rom vil bli opprettet
                    System.out.println("Dette er et rom: " + info + ", " + type); // teste at det funker
                    
                    System.out.println("Brukernavn: " + bnavn); // tester lokal variabel 
                    
                    romListe.add(new ClientHandler(info, bnavn));
                    
                    for(int i = 0; i < romListe.size(); i++)
                        if(romListe.get(i).getBnavn().equals(bnavn)) 
                            System.out.println(bnavn + ": Du er herved medlem av grupperom " + romListe.get(i).getRomNmr());
                }
                
                else if(type.equals("JOIN")) {
                    // Her kommer funksjoner for dersom at noen joiner et rom
                    System.out.println(bnavn + " vil joine rom " + info); // test om jeg fikk riktig input
                    
                    romListe.add(new ClientHandler(info, bnavn));
                    
                    out = new ObjectOutputStream(socket.getOutputStream());
                    
                    String outputInfo = bnavn + ";" + info;
                    
                    out.writeObject(outputInfo);
                    
                    out.close();
                    socket.close();
                }
                    
                
                in.close();
                socket.close();
            }
        } catch(IOException | ClassNotFoundException ex) {
        }
    }
    
}
