package com.docassist;
import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



public class WelcomePage extends Page {

    Stage primaryStage;
    
    public WelcomePage(Stage primaryStage) {
        this.primaryStage = primaryStage;

        Scene welcomeScene = new Scene(this, 720, 480);
        primaryStage.setScene(welcomeScene) ;
        primaryStage.setTitle("DocAssist");
        primaryStage.show();
        this.setStyle("-fx-padding: 30; -fx-font-family: 'Arial'; -fx-font-size: 16");


        Text title = new Text("Willkommen bei DocAssist");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        HBox buttons = new HBox(50);
        buttons.setAlignment(Pos.CENTER);

        Button btnOpen = new Button("Dokument einlesen");
        Button btnNew = new Button("Neues Dokument erstellen");
        buttons.getChildren().addAll(btnOpen, btnNew);

        btnOpen.setOnAction(e -> {
            openFileChooser();
        });
        btnNew.setOnAction(e -> {
            openNewFile();
        });

        this.setTop(title);
        this.setCenter(buttons);
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Dokument zum Einlesen auswählen");

        // Dateitypen-Filter festlegen
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Unterstützte Dokumente", "*.docx", "*.pdf", "*.md"),
                new FileChooser.ExtensionFilter("Word Dokumente", "*.docx"),
                new FileChooser.ExtensionFilter("PDF Dokumente", "*.pdf"),
                new FileChooser.ExtensionFilter("Markdown Dokumente", "*.md"),
                new FileChooser.ExtensionFilter("Alle Dateien", "*.*")
        );

        // Datei-Explorer anzeigen und auf Auswahl warten
        File selectedFile = fileChooser.showOpenDialog(this.primaryStage);

        if (selectedFile != null) {
            System.out.println("Ausgewählte Datei: " + selectedFile.getAbsolutePath());
            
            openDoc(selectedFile);

        } else {
            System.out.println("Dateiauswahl abgebrochen.");
        }
    }

    private void openDoc(File file) {
        new EditPage(primaryStage, file);
    }

    private void openNewFile() {
        File newFile = new File("newFile");
        openDoc(newFile);
    }

}
