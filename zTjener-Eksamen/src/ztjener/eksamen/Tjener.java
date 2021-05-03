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
    
    // Lengde og bredde på vinduet
    protected final int WIDTH = 600, HEIGHT = 500; 
    private static String url = "jdbc:sqlite:eksamen.db"; 
    
    
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

    
    //Metode for å oprette nye tabeller
    public static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS melding (\n"
                + "     id integer PRIMARY KEY autoincrement, \n"
                + "     tekst string NOT NULL, \n"
                + "     klokkeslett date NOT NULL, \n"
                + "     brukernavn string NOT NULL, \n"
                + "     romnr integer NOT NULL \n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            //stmt.execute(sql1);
        } catch(SQLException e) {
            System.out.println("Funka dårlig å opprette ny tabell ja");
        }
    }
    
    //Laste opp melding fra bruker til databasen
    public static void lastOppMelding() {
    //btn.setOnAction((event) -> {
       
          String insert = "insert into melding (id, tekst, klokkeslett, brukernavn, romnr) values (null, 'her er min kanon', '12.30','Jon_Kanon352', 1);";
          
           try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(insert);
             
        } catch(SQLException e) {
            System.out.println("Funka dårlig å sette inn ny data");
        }
            
    //});
    
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        Tjener app = new Tjener(); 
        app.connectDB();
        
        BorderPane bpane = new BorderPane();
        bpane.setLeft(getRoomPane());
        
        Scene scene = new Scene(bpane, WIDTH, HEIGHT);
        primaryStage.setTitle("Tjener");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
        
    
    public Pane getRoomPane() {
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
    public static void main(String[] args) {
        (new RunningSocket()).start();

        createNewTable();
        lastOppMelding();
        //createNewTable();

        launch(args);
    }
    
}
