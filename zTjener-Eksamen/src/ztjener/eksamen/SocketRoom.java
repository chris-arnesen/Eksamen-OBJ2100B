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
    
    protected ArrayList<RunningSocket> rooms = new ArrayList<>();
    
    
    public SocketRoom(RunningSocket socket) {
        rooms.add(socket);
    }
    
    
    public void startRooms() {
        for (RunningSocket room : rooms)
            room.start();
    }
    
}
