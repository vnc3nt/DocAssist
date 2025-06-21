package com.docassist;

import javafx.application.Application;
import javafx.stage.Stage;


public class app extends Application {
    public void start(Stage mystage){
        new WelcomePage(mystage);
    }
    public static void main(String[] args) {
        launch(args);
    }
}