package com.docassist;

import javafx.application.Application;
import javafx.stage.Stage;



public class app extends Application {
    public void start(Stage mystage){
        // Diese Zeile aktiviert die SVG-Unterstützung für die gesamte Anwendung
        
        new WelcomePage(mystage);
    }
    public static void main(String[] args) {
        launch(args);
    }
}