package com.docassist.view;

import java.io.IOException;

import com.docassist.controller.WelcomeController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WelcomePage {

    public WelcomePage(Stage primaryStage) {
        try {
            // 1. FXML-Datei laden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/docassist/view/WelcomeScene.fxml"));
            Parent root = loader.load();

            // 2. Controller-Instanz holen
            WelcomeController controller = loader.getController();
            
            // 3. Dem Controller die Stage übergeben, damit er sie nutzen kann (z.B. für den FileChooser)
            controller.setStage(primaryStage);

            // 4. Szene erstellen und anzeigen
            Scene welcomeScene = new Scene(root, 720, 480);
            
            primaryStage.setScene(welcomeScene);
            primaryStage.setTitle("DocAssist");
            primaryStage.show();
            
        } catch (IOException e) {
            // Fehlerbehandlung, falls die FXML-Datei nicht geladen werden kann
            e.printStackTrace();
        }
    }
}
