/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztjener.eksamen;

import java.util.ArrayList;

/**
 *
 * @author Mats Engesund
 */
public class RomListe {
    
    ArrayList<String> klienter = new ArrayList<>(); 
    String romnavn; 
    
    public RomListe(String klient, String romnavn) {
        klienter.add(klient); 
        this.romnavn = romnavn; 
    }
    
}
