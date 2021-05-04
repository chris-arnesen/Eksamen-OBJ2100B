/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztjener.eksamen;

/**
 *
 * @author Mats Engesund
 */
public class ClientHandler {
    
    protected String romNmr, bnavn; 
    
    
    public ClientHandler(String romNmr, String bnavn) {
        this.romNmr = romNmr; 
        this.bnavn = bnavn; 
    }
    
    
    public String getRomNmr() {
        return romNmr; 
    }
    
    public String getBnavn() {
        return bnavn;
    }
    
}
