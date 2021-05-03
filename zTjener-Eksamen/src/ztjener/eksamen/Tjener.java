/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztjener.eksamen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class Tjener extends Application {
    
    public static class runningSocket extends Thread {
        static int port = 8000;
        static ObjectOutputStream out;
        static ObjectInputStream in;
        static ServerSocket server;
        static Socket socket;
        
        @Override() 
        public void run() {
            try {
                server = new ServerSocket(port);
                
                while(true) {
                    socket = server.accept();
                    in = new ObjectInputStream(socket.getInputStream());
                    String bNavn = (String)(in.readObject()); // Her får du brukernavnet som blir sendt fra klient
                    System.out.println("DET FUNKER FOR FAEN " + bNavn);
                    in.close();
                    socket.close();
                }
            } catch(IOException | ClassNotFoundException ex) {
            }
        }
    }
    
    protected final int WIDTH = 600, HEIGHT = 500; 
    
    
    //Socket 
    static int port = 8000;
    static ObjectOutputStream out;
    static ObjectInputStream in;
    static ServerSocket server;
    static Socket socket;
    
    
    // Listview
    protected String chat1 = "Chat 1"; 
    protected String chat2 = "Chat 2"; 
    protected String chat3 = "Chat 3";
    
    
    private Connection connectDB() {
        String url = "jdbc:sqlite:eksamen.db"; 
        Connection con = null; 
        try {
            con = DriverManager.getConnection(url); 
            System.out.println("Tilkobling til DB er etablert");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }
    
    
    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        Tjener app = new Tjener(); 
        app.connectDB();
        
        BorderPane bpane = new BorderPane();
        bpane.setLeft(getRooms());
        
        Scene scene = new Scene(bpane, WIDTH, HEIGHT);
        primaryStage.setTitle("Tjener");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        
        
    }
        
    
    public Pane getRooms() {
        Pane rooms = new Pane();
        rooms.setPadding(new Insets(5, 5, 5, 50));
        
        ObservableList<String> chatNames = FXCollections.observableArrayList(chat1, chat2, chat3);
        ListView<String> roomList = new ListView<String>(chatNames);
        roomList.setPrefWidth(WIDTH/3);
        roomList.setPrefHeight(HEIGHT);
        rooms.getChildren().add(roomList);
        
        return rooms; 
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        (new runningSocket()).start();
        launch(args);
        
        
        /*server = new ServerSocket(port);
        
        while (true) {
            socket = server.accept();
            in = new ObjectInputStream(socket.getInputStream());
            String bNavn = (String)(in.readObject()); // Her får du brukernavnet som blir sendt fra klient
            
            in.close();
            socket.close();
        }*/
        
        
        
        
        
    }
    
}
