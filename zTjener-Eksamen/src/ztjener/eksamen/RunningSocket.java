package ztjener.eksamen;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;


public class RunningSocket implements Runnable {
    
    Socket socket;
    ObjectInputStream in;
    DataOutputStream out;
    Tjener tjener;
    Commando type;
        
    String brukernavn ="";
    
    /**
     *
     * @param innSocket
     * @param tjener
     */
    public RunningSocket(Socket innSocket, Tjener tjener) {
        this.socket = innSocket;
        this.tjener = tjener;
    }
    
    /**
     *
     */
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
                        InetAddress inet = socket.getInetAddress();
                        
                        tjener.lastOppBrukerTilkoblet(beskjed, inet.getHostAddress());
                        System.out.println("Ny bruker med navn: " + brukernavn);
                        //Dersom brukere har lagt til rom, og denne brukeren er ny, send kommandoer til klienten, om å opprette rom
                        if (tjener.rom.size() > 0 ) {
                            giRom();
                        }
                        //Det samme her, bare med meldinger istedet for rom
                        if (tjener.alleMeldinger.size() > 0) {
                            giTidligereMeldinger();
                        }
                    } 
                    
                    else if (typeInput.equals("MELDING")) {
                        tjener.lastOppMelding(beskjed, brukernavn); //Laster opp meldingen til databasen
                        String meldingTilAlle = "[" + brukernavn + "] " + beskjed; 
                        tjener.alleMeldinger.add(typeInput + ";" + meldingTilAlle); //Legger meldingene inn i en liste
                        tjener.sendToAllClients(typeInput + ";" + meldingTilAlle);
                    }
                    
                    else if (typeInput.equals("ROM")) {
                        // Sender melding til alle klienter og ber om å legge til rom i liste
                        String meldingTilAlle = type.CREATE.name() + ";" + beskjed;
                        tjener.rom.add(meldingTilAlle); //Legger til romkommando inn i en liste
                        tjener.sendToAllClients(meldingTilAlle);
                        
                        // Legger til rom i liste på tjener-siden
                        brukernavn = splitString[2];
                        RomListe rl = new RomListe(brukernavn, beskjed);
                        tjener.romListe.add(rl);
                        tjener.listView.getItems().add(beskjed);
                    }
                    
                    else if (typeInput.equals("JOIN")) {
                        System.out.println(splitString[2] + " vil joine " + beskjed);
                        brukernavn = splitString[2];
                        RomListe rl = new RomListe(brukernavn, beskjed);
                        tjener.romListe.add(rl); 
                    }
                    
                    else if (typeInput.equals("REMOVE")) {
                        brukernavn = splitString[1];
                        String romNmr = splitString[2];
                        for(int i = 0; i < tjener.romListe.size(); i++) {
                            for(int j = 0; j < tjener.romListe.get(i).klienter.size(); j++) {
                                if(tjener.romListe.get(i).klienter.get(j).equals(brukernavn))
                                    tjener.romListe.get(i).klienter.remove(j);
                                
                                else if(tjener.romListe.get(i).klienter.isEmpty()) {
                                    tjener.romListe.remove(i);
                                    
                                    tjener.sendToAllClients(type.EMPTY.name() + ";" + romNmr);
                                }
                            }
                            
                        }
                    }
                    
                }catch (ClassNotFoundException ex) {System.out.println("ERROR CNF-feil på fil RunningSocket");} 
                
            }
        }catch(IOException ex) {System.out.println("ERROR, IO-feil i RunningSocket");
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {System.out.println("ERROR IO-feil på linje 115-119");}
        }
        
        
    }
        
    /**
     *
     * @param melding
     */
    public void skrivMelding(String melding) {
        try {
            out.writeUTF(melding);
            out.flush();
        } catch (IOException ex) {System.out.println("ERROR IO-feil på linje 130-133 i RunningSocket");}
    }

    /**
     *
     */
    public void giRom() {
        try {
            for (String etRom : tjener.rom) {
                out.writeUTF(etRom);
            }
        }catch(IOException ex) {System.out.println("Kunne ikke gi ny bruker eksisterende rom");}
    }
    
    /**
     *
     */
    public void giTidligereMeldinger() {
        try {
            for (String enMelding : tjener.alleMeldinger) {
                out.writeUTF(enMelding);
            }
        }catch(IOException ex) {System.out.println("Kunne ikke gi ny bruker tidligere eksisterende meldinger");}
    }
    
}
