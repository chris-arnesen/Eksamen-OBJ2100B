/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zklient.eksamen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Klient extends Application {
    
    enum Type {
        BNAVN, MELDING, ROM, JOIN
    }
    
    static String outputInfo ="";
    static Type type;
    
    //Socket
    static int port = 8000;
    static String host = "localhost";
    static ObjectOutputStream out = null;
    
    
    static String bNavn;
    
    
    static BorderPane bpane;
    //Login deklarasjoner
    static Pane topLogin = new Pane();
    static Pane centerLogin = new Pane();
    static Pane bottomLogin = new Pane();
    static Label topLabelLogin = new Label("Tast inn brukernavn: ");
    static TextField txtLogin = new TextField();
    static Button btnLogin = new Button("Log inn");
    
    
    //Rom deklarasjoner
    static Pane topRom = new Pane();
    static Pane centerRom = new Pane();
    static Pane bottomRom = new Pane();
    static Label labelRom = new Label("Chat-Rom: ");
    public static ListView list = new ListView(FXCollections.observableArrayList(Arrays.asList()));
    static Button btnNew = new Button("Opprett nytt Chat-rom");
    
    
    //Chat deklarasjoner
    static Pane topChat = new Pane();
    static TextArea centerChat = new TextArea();
    static Pane bottomChat = new Pane();
    static Label labelChat = new Label();
    static TextField txtChat = new TextField();
    static Button btnChat = new Button("Send");
    static Button btnBack = new Button("Forlat rom");
    
    
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
        btnLogin.setOnAction(new ButtonListener());

        //Prøver meg på button Action
        //Det som skal skje når en trykker på login-knappen
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
        //Det som skal skje dersom en bruker trykker på knappen for å sende en melding
        /*btnChat.setOnAction(e -> {
            if (txt)
        });*/
        btnChat.setOnAction(e -> {
            try {
                if (txtChat.getText().equals("")) {
                    System.out.println("Feil: Du har ikke skrevet inn en melding");
                } else {
                    String utTekst = type.MELDING.name() + ";" + txtChat.getText();
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
        
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (list.getSelectionModel().getSelectedItem().equals(list.getSelectionModel().getSelectedItem())) {
                    try {
                        String utTekst = type.JOIN.name() + ";" + list.getSelectionModel().getSelectedItem() + ";" + bNavn;
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
        
        /*list.getItems().add("Item 1");
        list.getItems().add("Item 2");
        list.getItems().add("Item 3");*/
        /* Slutt på rom side panes */
        
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
    
    
    
    
    /*public static void logInn() {
        
        btnLogin.setOnAction((event) -> {
            
            bNavn = txtLogin.getText();
            if (bNavn.equals("")) {
                System.out.println("Navn-feltet er tomt");
            } else {
                try {
                    socket = new Socket(host, port);
                    out = new ObjectOutputStream(socket.getOutputStream());
                    
                    String outputInfo = type.BNAVN.name() + ";";
                    outputInfo+=bNavn;
                    
                    out.writeObject(outputInfo);
                    
                    out.close();
                    socket.close();
                    
                    bpane.getChildren().remove(centerLogin);
                    bpane.getChildren().remove(topLogin);
                    bpane.getChildren().remove(bottomLogin);
                    
                    bpane.setTop(topRom);
                    bpane.setCenter(centerRom);
                    bpane.setBottom(bottomRom);
                    topRom.getChildren().add(labelRom);
                    centerRom.getChildren().add(list);
                    bottomRom.getChildren().add(btnNew);
                } catch (IOException ex) { System.out.println("Feil med forbindelse til tjener"); }
            }
        });   
    }*/
    
    
    public void openInput() {
        String txt = "";
        TextInputDialog txtBox = new TextInputDialog("Chatroom name");
        txtBox.setHeaderText("Create New Chatroom");
        Optional<String> result = txtBox.showAndWait();
     
        /* Måte å hente ut verdien på */
        if(result.isPresent()) {
            try {
                String utTekst = type.ROM.name() + ";" + result.get() + ";" + bNavn;
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
    
    public static void backBtn() {
          btnBack.setOnAction(event -> {                          
                            bpane.getChildren().remove(topChat);
                            bpane.getChildren().remove(centerChat);
                            bpane.getChildren().remove(bottomChat);

                            bpane.setTop(topRom);
                            bpane.setCenter(centerRom);
                            bpane.setBottom(bottomRom);
                        });
    }
    
    /*
    public void createNewRoom(String txt) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream()); 
            
            String outputInfo = type.ROM.name() + ";";
            outputInfo += txt;        
            out.writeObject(outputInfo);
            
                        bpane.getChildren().remove(topRom);
                        bpane.getChildren().remove(centerRom);
                        bpane.getChildren().remove(bottomRom);

                        bpane.setTop(topChat);
                        bpane.setCenter(centerChat);
                        bpane.setBottom(bottomChat);
                      
            out.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    /*
    public void addRoomToList(String txt) {
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                try {
                    System.out.println("Du trykket på: " + list.getSelectionModel().getSelectedItem());
                    socket = new Socket(host, port);
                    out = new ObjectOutputStream(socket.getOutputStream());
                    
                    String outputInfo = type.JOIN.name() + ";";
                    outputInfo += list.getSelectionModel().getSelectedItem();
                    out.writeObject(outputInfo);
                    
                    /*
                        Her kommer det kode på hva som skjer etter
                        de har joinet/blitt lagt til i liste med rom:
                    */
                       
                    /*
                    in = new ObjectInputStream(socket.getInputStream());
                    String klientInput = (String)(in.readObject()); 
                    System.out.println(klientInput);
                    
                    socket.close();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        list.getItems().add(txt);
    }*/
    
    /*
    public static void chat() {
        btnNew.setOnAction((event) -> {
        bpane.getChildren().remove(topRom);
        bpane.getChildren().remove(centerRom);
        bpane.getChildren().remove(bottomRom);
        
        
        bpane.setTop(topChat);
        bpane.setCenter(centerChat);
        bpane.setBottom(bottomChat);
        topChat.getChildren().add(labelChat);
        bottomChat.getChildren().add(txtChat);
        bottomChat.getChildren().add(btnChat);
        
    }); 
    }
    */

    /*


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //sendMelding();
        //logInn();
        backBtn();
        //chat();
        launch(args);
    }
    
    private class ButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            try {
            String inputFieldTekst = txtLogin.getText();
            out.writeObject(inputFieldTekst);
            out.flush();
            }catch(IOException ex) {System.out.println("ERROR IO-feil på linje 71-74 på ZzKlient");}
        }
    }
    
}
