/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zklient.eksamen;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Klient extends Application {

    private static void forlatRommet() {
        try {
            String utTekst = type.REMOVE.name() + ";" + bNavn + ";" + labelChat.getText();
            out.writeObject(utTekst);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    enum Type {
        BNAVN, MELDING, ROM, JOIN, REMOVE
    }
    
    static String outputInfo ="";
    static Type type;
    
    
    //Socket
    static int port = 8000;
    static String host = "localhost";
    static ObjectOutputStream out = null;
    
    static String aktivtRom=""; //navnet på rommet som brukeren er i nå
    static List<String> alleMeldinger = new ArrayList<>();
    
    
    static String bNavn;
    
    
    static BorderPane bpane;
    //GUI-deklarasjoner til Login siden
    static Pane topLogin = new Pane();
    static Pane centerLogin = new Pane();
    static Pane bottomLogin = new Pane();
    static Label topLabelLogin = new Label("Tast inn brukernavn: ");
    static TextField txtLogin = new TextField();
    static Button btnLogin = new Button("Log inn");
    
    
    //GUI-deklarasjoner til Rom siden
    static Pane topRom = new Pane();
    static Pane centerRom = new Pane();
    static Pane bottomRom = new Pane();
    static Label labelRom = new Label("Chat-Rom: ");

    /**
     *
     */
    public static ListView list = new ListView(FXCollections.observableArrayList(Arrays.asList()));
    static Button btnNew = new Button("Opprett nytt Chat-rom");
    
    
    //GUI-deklarasjoner til Chat siden
    static Pane topChat = new Pane();
    static TextArea centerChat = new TextArea();
    static Pane bottomChat = new Pane();
    static Label labelChat = new Label();
    static TextField txtChat = new TextField();
    static Button btnChat = new Button("Send");
    static Button btnBack = new Button("Forlat rom");
    
    /**
     *
     * @param txt
     */
    public void addChatrom(String txt) {
        list.getItems().add(txt);
    }
    
    @Override
    public void start(Stage primaryStage) {
        bpane = new BorderPane();
        
        /* Login side panes */
        bpane.setTop(topLogin);
        bpane.setCenter(centerLogin);
        bpane.setBottom(bottomLogin);
        

        //Button action for login-knapp
        btnLogin.setOnAction(e -> {
            try {
                if (txtLogin.getText().equals("")) {
                    System.out.println("Feil: Du har ikke skrevet inn et brukernavn");
                } else {
                    bNavn = txtLogin.getText();
                    String utTekst = type.BNAVN.name() + ";" + bNavn;
                    out.writeObject(utTekst);
                    out.flush();
                    
                    bpane.getChildren().remove(centerLogin);
                    bpane.getChildren().remove(topLogin);
                    bpane.getChildren().remove(bottomLogin);
                    
                    bpane.setTop(topRom);
                    bpane.setCenter(centerRom);
                    bpane.setBottom(bottomRom);
                    topRom.getChildren().add(labelRom);
                    centerRom.getChildren().add(list);
                    bottomRom.getChildren().add(btnNew);
                }
            } catch (IOException ex) {System.out.println("Feil med login-funksjon");}
        });
        //button action for chat-knapp
        btnChat.setOnAction(e -> {
            try {
                if (txtChat.getText().equals("")) {
                    System.out.println("Feil: Du har ikke skrevet inn en melding");
                } else {
                    String utTekst = type.MELDING.name() + ";" + txtChat.getText() + ":" + aktivtRom;
                    out.writeObject(utTekst);
                    out.flush();
                }
                txtChat.clear();
            }catch(IOException ex) {System.out.println("Feil med send melding-funksjon");}
        });
        
        
        topLogin.setPrefHeight(100);
        topLogin.setStyle("-fx-border-color: black; -fx-background-color: grey;");
        centerLogin.setPrefHeight(300);
        centerLogin.setStyle("-fx-border-color: black; -fx-background-color: white;");
        bottomLogin.setPrefHeight(100);
        bottomLogin.setStyle("-fx-border-color: black; -fx-background-color: grey;");
        topLabelLogin.setStyle("-fx-text-fill:BLACK; -fx-font-size: 30;");
        
        topLogin.getChildren().add(topLabelLogin);
        topLabelLogin.setLayoutX(25);
        topLabelLogin.setLayoutY(25);
        
        centerLogin.getChildren().add(txtLogin);
        txtLogin.setLayoutX(150);
        txtLogin.setLayoutY(140);
        centerLogin.getChildren().add(btnLogin);
        btnLogin.setLayoutX(350);
        btnLogin.setLayoutY(140);
        /* Slutt på Login panes */
        
        /*  Rom side panes */
        topRom.setPrefHeight(50);
        topRom.setStyle("-fx-border-color: black; -fx-background-color: grey;");
        centerRom.setPrefHeight(450);
        centerRom.setStyle("-fx-border-color: black; -fx-background-color: white;");
        bottomRom.setPrefHeight(50);
        bottomRom.setStyle("-fx-border-color: black; -fx-background-color: grey;");
        labelRom.setStyle("-fx-text-fill:BLACK; -fx-font-size: 30;");
        list.setPrefWidth(600);
        
        
        //Dersom en bruker trykker på et rom i GUI'et
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (list.getSelectionModel().getSelectedItem().equals(list.getSelectionModel().getSelectedItem())) {
                    try {
                        String utTekst = type.JOIN.name() + ";" + list.getSelectionModel().getSelectedItem() + ";" + bNavn;
                        aktivtRom = (String)list.getSelectionModel().getSelectedItem();
                        labelChat.setText(aktivtRom);
                        
                        //Denne for-loopen viser alle meldinger tilhørende til det rommet, når en trykker seg inn på et rom
                        for (String s :alleMeldinger) {
                            String[] split = s.split(":"); //melding:romnr
                            if (split[1].equals(aktivtRom)) {
                                centerChat.appendText(split[0]+"\n");
                            }
                        }
                        
                        out.writeObject(utTekst);
                        out.flush();
                        
                        bpane.getChildren().remove(topRom);
                        bpane.getChildren().remove(centerRom);
                        bpane.getChildren().remove(bottomRom);

                        bpane.setTop(topChat);
                        bpane.setCenter(centerChat);
                        bpane.setBottom(bottomChat);
                    } catch (IOException ex) {
                        Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        labelRom.setLayoutX(25);
        labelRom.setLayoutY(7);
        btnNew.setLayoutX(25);
        btnNew.setLayoutY(7);
        btnNew.setOnAction(e -> openInput());
        
        /* Chat side panes */
        topChat.setPrefHeight(50);
        topChat.setStyle("-fx-border-color: black; -fx-background-color: grey;");
        centerChat.setPrefHeight(450);
        centerChat.setStyle("-fx-border-color: black; -fx-background-color: white;");
        bottomChat.setPrefHeight(50);
        bottomChat.setStyle("-fx-border-color: black; -fx-background-color: grey;");
        labelChat.setStyle("-fx-text-fill:BLACK; -fx-font-size: 30;");
        txtChat.setPrefWidth(500);
        
        labelChat.setLayoutX(25);
        labelChat.setLayoutY(7);
        btnChat.setLayoutX(530);
        btnChat.setLayoutY(7);
        txtChat.setLayoutX(20);
        txtChat.setLayoutY(7);
        btnBack.setLayoutX(530);
        btnBack.setLayoutY(7);
        centerChat.setEditable(false);
        
        topChat.getChildren().add(labelChat);
        bottomChat.getChildren().add(txtChat);
        bottomChat.getChildren().add(btnChat);
        topChat.getChildren().add(btnBack);
        /* Slutt på Chat side panes */
        
        
        Scene scene = new Scene(bpane, 600, 500);
        primaryStage.setTitle("Klient");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Alt dette er socket kommunikasjon, input til server, åpnes en gang, mens vi hører etter 
        //    output fra server i en annen thread hele tiden
        try {
            Socket socket = new Socket(host, port);
            System.out.println("Koblet til server");
            
            out = new ObjectOutputStream(socket.getOutputStream());
            klientThread inputListener = new klientThread(socket, this); 
            Thread t = new Thread(inputListener);
            t.start();
        }catch(IOException ex){System.out.println("Tilkobling til server feilet");}
        
    }
    
    /**
     *
     */
    public void openInput() {
        String txt = "";
        TextInputDialog txtBox = new TextInputDialog("Chatroom name");
        txtBox.setHeaderText("Create New Chatroom");
        Optional<String> result = txtBox.showAndWait();
     
        /* Måte å hente ut verdien på */
        if(result.isPresent()) {
            try {
                String utTekst = type.ROM.name() + ";" + result.get() + ";" + bNavn;
                aktivtRom = result.get();
                out.writeObject(utTekst);
                out.flush();
                
                bpane.getChildren().remove(topRom);
                bpane.getChildren().remove(centerRom);
                bpane.getChildren().remove(bottomRom);

                bpane.setTop(topChat);
                bpane.setCenter(centerChat);
                bpane.setBottom(bottomChat);
            } catch (IOException ex) {
                Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     *
     */
    //Action for backBtn, knapp for når bruker vil ut av et rom
    public static void backBtn() {
        btnBack.setOnAction(event -> {  
            forlatRommet();
            centerChat.clear();
            bpane.getChildren().remove(topChat);
            bpane.getChildren().remove(centerChat);
            bpane.getChildren().remove(bottomChat);

            bpane.setTop(topRom);
            bpane.setCenter(centerRom);
            bpane.setBottom(bottomRom);
        });
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        backBtn();
        launch(args);
    }
    
    
    
}
