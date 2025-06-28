package com.docassist;

import com.docassist.view.WelcomePage;

import javafx.application.Application;
import javafx.stage.Stage;



public class app extends Application {
    public void start(Stage mystage){
        
        new WelcomePage(mystage);
        mystage.setMinWidth(600);
        mystage.setMinHeight(450);
    }
    public static void main(String[] args) {
        launch(args);
    }
}