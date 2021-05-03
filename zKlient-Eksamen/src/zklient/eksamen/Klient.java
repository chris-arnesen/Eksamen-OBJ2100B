/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zklient.eksamen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Klient extends Application {
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
    static Label labelRom = new Label("Chat-Rom: ");
    static ListView list = new ListView();
    
    
    
    @Override
    public void start(Stage primaryStage) {
    
        bpane = new BorderPane();
        /* Login side panes */
        bpane.setTop(topLogin);
        bpane.setCenter(centerLogin);
        bpane.setBottom(bottomLogin);
        
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
        labelRom.setStyle("-fx-text-fill:BLACK; -fx-font-size: 30;");
        
        labelRom.setLayoutX(25);
        labelRom.setLayoutY(7);
        
        
        Scene scene = new Scene(bpane, 600, 500);
        
        primaryStage.setTitle("Klient");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void logInn() {
    btnLogin.setOnAction((event) -> {
        bpane.getChildren().remove(centerLogin);
        bpane.getChildren().remove(topLogin);
        bpane.getChildren().remove(bottomLogin);
        
        bpane.setTop(topRom);
        bpane.setCenter(centerRom);
        topRom.getChildren().add(labelRom);
    
    });
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logInn();
        launch(args);
    }
    
}
