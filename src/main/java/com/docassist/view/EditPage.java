package com.docassist.view;

import java.io.File;
import java.io.IOException;

import com.docassist.controller.EditController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EditPage {

    public EditPage(Stage primaryStage, File file) {
        try {
            // 1. FXML-Datei laden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/docassist/view/EditScene.fxml"));
            Parent root = loader.load();

            // 2. Controller-Instanz holen
            EditController controller = loader.getController();

            // 3. Dem Controller die Stage und die Datei übergeben
            controller.initData(primaryStage, file);

            // 4. Szene erstellen und anzeigen
            Scene editScene = new Scene(root, 720, 480);
            primaryStage.setScene(editScene);
           

            // Der Titel wird jetzt im Controller gesetzt, da er vom Dateinamen abhängt
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
