package ztjener.eksamen;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


public class Tjener extends Application {

    /**
     *
     */
    protected final int WIDTH = 600, 

    /**
     *
     */
    HEIGHT = 500; 
    private static String url = "jdbc:sqlite:eksamen.db"; 
    
    
    //Socket 
    final static int port = 8000;
    List<RunningSocket> connections = new ArrayList<RunningSocket>(); //Listen inneholder alle klient-threads
    List<String> rom = new ArrayList<>();
    List<String> alleMeldinger = new ArrayList<>();

    
    /**
     *
     */
    public static ListView listView = new ListView(FXCollections.observableArrayList(Arrays.asList()));

    /**
     *
     */
    public static ArrayList<RomListe> romListe = new ArrayList<>();
    
    TextArea txtarea;
    
    
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


    /**
     *
     */
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
        } catch(SQLException e) {
            System.out.println("Kunne ikke opprette tabell");
        }
    }

    /**
     *
     * @param melding
     * @param brukernavn
     */
    public static void lastOppMelding(String melding, String brukernavn) {
       
        String insert = "insert into melding (id, tekst, klokkeslett, brukernavn, romnr) values (null,'" + melding+"', '12.30','"+brukernavn+"', 1);";
          
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
        stmt.execute(insert);
             
        } catch(SQLException e) {System.out.println("Funka dårlig å sette inn ny data");}
    }
    
    /**
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        Tjener app = new Tjener(); 
        app.connectDB();
        
        BorderPane bpane = new BorderPane();
        bpane.setLeft(getRoomPane());
        bpane.setCenter(getRightPane());
        
        Scene scene = new Scene(bpane, WIDTH, HEIGHT);
        primaryStage.setTitle("Tjener");
        primaryStage.setScene(scene);
        primaryStage.show();
        
      //Her starter vi en ny Thread for å ikke stoppe JavaFX threaden. Inni threaden venter vi på at folk skal koble seg til
      //Når de gjør det, så gir vi dem en ny Thread, hver klient får altså en thread
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
            }catch (IOException ex) {System.out.println("ERROR, IO-feil på linje 126-137 i Tjener");}
        }).start();
    }
        
    /**
     *
     * @return
     */
    public Pane getRoomPane() {
        Pane rooms = new Pane();
        
        listView.setPrefWidth(WIDTH/3);
        listView.setPrefHeight(HEIGHT);
        
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                txtarea.clear();
                for(int i = 0; i < romListe.size(); i++) {
                    for(int j = 0; j <= romListe.get(i).klienter.size(); i++) {
                        if(romListe.get(i).klienter.isEmpty())
                            listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
                        else 
                            txtarea.appendText(romListe.get(i).klienter.get(j) + "\n");
                    }
                }
            }
        });

        
        rooms.getChildren().add(listView);
        
        return rooms; 
    }
    
    /**
     *
     * @return
     */
    public Pane getRightPane() {
        Pane rightPane = new Pane();
        rightPane.setStyle("-fx-background-color: black");
        
        txtarea = new TextArea();
        txtarea.setPrefWidth((WIDTH/3)*2);
        txtarea.setPrefHeight(HEIGHT);
        txtarea.setEditable(false);
        rightPane.getChildren().add(txtarea);
        
        return rightPane;
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {  
        createNewTable();
        launch(args);
    }

    /**
     *
     * @param melding
     */
    public void sendToAllClients(String melding) {
        this.connections.forEach(con -> {
            con.skrivMelding(melding);
        });
    }
    
    
    
}
