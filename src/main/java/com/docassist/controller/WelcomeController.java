package com.docassist.controller;

import java.io.File;

import com.docassist.view.EditPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class WelcomeController {

    private Stage primaryStage;

    // Diese Methode wird von der WelcomePage aufgerufen, um die Stage zu übergeben.
    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    // Diese Methode wird vom "Dokument einlesen"-Button aufgerufen (via onAction="#handleOpen").
    @FXML
    private void handleOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Dokument zum Einlesen auswählen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Unterstützte Dokumente", "*.docx", "*.pdf", "*.md"),
                new FileChooser.ExtensionFilter("Word Dokumente", "*.docx"),
                new FileChooser.ExtensionFilter("PDF Dokumente", "*.pdf"),
                new FileChooser.ExtensionFilter("Markdown Dokumente", "*.md"),
                new FileChooser.ExtensionFilter("Alle Dateien", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(this.primaryStage);

        if (selectedFile != null) {
            System.out.println("Ausgewählte Datei: " + selectedFile.getAbsolutePath());
            openDoc(selectedFile);
        } else {
            System.out.println("Dateiauswahl abgebrochen.");
        }
    }

    // Diese Methode wird vom "Neues Dokument erstellen"-Button aufgerufen (via onAction="#handleNew").
    @FXML
    private void handleNew(ActionEvent event) {
        File newFile = new File("newFile");
        openDoc(newFile);
    }
    
    // Die Hilfsmethoden bleiben erhalten.
    private void openDoc(File file) {
        new EditPage(primaryStage, file);
    }
}
