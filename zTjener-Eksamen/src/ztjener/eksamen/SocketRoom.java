/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztjener.eksamen;

import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Mats Engesund
 */
public class SocketRoom {
    
    public String romNavn; 
    public RunningSocket rs; 
    
    
    public SocketRoom(String romNavn, RunningSocket rs) {
        this.romNavn = romNavn; 
        this.rs = rs; 
    }
    
    public RunningSocket getRS() {
        return rs; 
    }
    
    public String getNavn() {
        return romNavn; 
    }
    
}
