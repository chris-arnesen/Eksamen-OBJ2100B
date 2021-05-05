package ztjener.eksamen;

import java.util.ArrayList;


public class RomListe {
    
    ArrayList<String> klienter = new ArrayList<>(); 
    String romnavn; 
    
    /**
     *
     * @param klient
     * @param romnavn
     */
    public RomListe(String klient, String romnavn) {
        klienter.add(klient); 
        this.romnavn = romnavn; 
    }
    
}
