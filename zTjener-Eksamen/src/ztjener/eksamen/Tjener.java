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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
    final static int port = 8000;
    List<RunningSocket> connections = new ArrayList<RunningSocket>(); //Listen inneholder alle klient-threads
    List<String> rom = new ArrayList<>();
    List<String> alleMeldinger = new ArrayList<>();
    //static ObjectOutputStream out;
    //static ObjectInputStream in;
    //static ServerSocket server;
    //static Socket socket;
    //static RunningSocket rs;
    
    
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
    public static void lastOppMelding(String melding, String brukernavn) {
       
        String insert = "insert into melding (id, tekst, klokkeslett, brukernavn, romnr) values (null,'" + melding+"', '12.30','"+brukernavn+"', 1);";
          
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
        stmt.execute(insert);
             
    } catch(SQLException e) {System.out.println("Funka dårlig å sette inn ny data");}
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
        
      //Her starter vi en ny Thread for å ikke stoppe JavaFX threaden, inni threaden, venter vi på at folk skal koble seg til
      //Når de gjør det, så gir vi dem en ny Thread, HVER KLIENT HAR ALSTÅ EN THREAD MED EN TILKOBLING
        new Thread(() -> {
            try {
                ServerSocket server = new ServerSocket(port);
                while (true) {
                    Socket socket = server.accept();
                    System.out.println("Noen koblet seg til");
                    RunningSocket enConnection = new RunningSocket(socket, this);
                    connections.add(enConnection);
                    
                    Thread t = new Thread(enConnection);
                    t.start();
                }
            }catch (IOException ex) {System.out.println("ERROR, IO-feil på linje 32-41 i ZzTjener");}
        }).start();
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
        

        createNewTable();
        
        //createNewTable();

        launch(args);
    }
    public void broadcast(String melding) {
        this.connections.forEach(con -> {
            con.skrivMelding(melding);
        });
    }
    
    
    
}
