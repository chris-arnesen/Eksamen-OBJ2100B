package zklient.eksamen;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Platform;


public class klientThread implements Runnable{
    
    Socket socket;
    Klient klientVindu;
    DataInputStream input;
    
    public klientThread(Socket socket, Klient klientVindu) {
        this.socket = socket;
        this.klientVindu = klientVindu;
    }
    
    @Override
    public void run() {
        while(true){
            try {
                input = new DataInputStream(socket.getInputStream());
                String tjenerMelding = input.readUTF();
                
                /* STYKKER OPP MELDING FRA TJENER */
                String[] splitString = tjenerMelding.split(";");
                String typeInput = splitString[0];
                String beskjed = splitString[1];
               
                /* SJEKKER HVA TYPEINFOEN BETYR */
                if(typeInput.equals("CREATE")) {
                    // Varsler klient(er) om å legge til chat-rom
                    System.out.println("Noen lagde et chatrom: " + " " + tjenerMelding);
                    klientVindu.addChatrom(beskjed);
                    Platform.runLater(() -> {
                        klientVindu.labelChat.setText(beskjed);
                    });
                }
                else if(typeInput.equals("MELDING")){
                    String[] mldSplit = beskjed.split(":"); //Beskjed struktur - "melding:romnavn" eks("heisann:rom1")
                    String mld = mldSplit[0];
                    String mldRom = mldSplit[1];
                    klientVindu.alleMeldinger.add(beskjed);
                    if (mldSplit[1].equals(klientVindu.aktivtRom)) {
                        Platform.runLater(() -> {
                            klientVindu.centerChat.appendText(mldSplit[0] + "\n");    
                        });
                    }
                }
                else if(typeInput.equals("EMPTY")) {
                    System.out.println(tjenerMelding);
                }
                
            } catch (IOException ex) {System.out.println("ERROR IO-feil i klientThread");
            } 
        }
    }
    
}
