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


public class Tjener extends Application {
    
    private static String url = "jdbc:sqlite:eksamen.db"; 
    //Socket
    int port = 8000;
    ObjectOutputStream out;
    ObjectInputStream in;
    ServerSocket server;
    Socket socket;
    
    
    private Connection connectDB() {
        //String url = "jdbc:sqlite:C:\\Users\\Mats Engesund\\Documents\\NetBeansProjects\\OBJ2100\\eksamen.db";
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
        //root.getChildren().add(btn);
        
        Scene scene = new Scene(bpane, 600, 500);
        
        primaryStage.setTitle("Tjener");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        server = new ServerSocket(port);
        
        while (true) {
            socket = server.accept();
            in = new ObjectInputStream(socket.getInputStream());
            String bNavn = (String)(in.readObject()); // Her får du brukernavnet som blir sendt fra klient
            
            in.close();
            socket.close();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
