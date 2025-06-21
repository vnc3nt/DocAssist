package com.docassist;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.Loader;


public class EditPage extends Page{
    Stage primaryStage;

    public EditPage(Stage primaryStage, File file) {
        this.primaryStage = primaryStage;

        this.setStyle("-fx-padding: 30; -fx-font-family: 'Arial'; -fx-font-size: 16");

        processFile(file);

        Scene editScene = new Scene(this, 720, 480);
        primaryStage.setScene(editScene) ;
        primaryStage.setTitle("DocAssist - " + file.getName());
        primaryStage.show();
        

    }

     /**
     * Prüft den Dateityp und ruft die entsprechende Anzeigemethode auf.
     */
    private void processFile(File file) {
        String extension = getFileExtension(file);

        switch (extension.toLowerCase()) {
            case "pdf":
                displayPdf(file);
                break;
            case "docx":
                displayDocx(file);
                break;
            case "md":
                displayMarkdown(file);
                break;
            default:
                displayUnsupportedFormat(file);
                break;
        }
    }

    /**
     * Zeigt ein PDF-Dokument an, indem jede Seite als Bild gerendert wird.
     */
    private void displayPdf(File pdfFile) {
        VBox pdfContainer = new VBox(10); // Vertikaler Container für die Seiten mit 10px Abstand
        pdfContainer.setAlignment(Pos.CENTER);

        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);
            
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                // Rendere die Seite in ein AWT-Bild mit einer guten Auflösung (z.B. 150 DPI)
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, 150);
                
                // Konvertiere das AWT-Bild in ein JavaFX-Image
                WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                
                ImageView imageView = new ImageView(fxImage);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(600); // Begrenze die Breite für eine gute Ansicht
                
                pdfContainer.getChildren().add(imageView);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Fehler beim Laden des PDFs: " + e.getMessage());
            this.setCenter(errorLabel);
            return;
        }

        // Mache den Container scrollbar, falls das PDF viele Seiten hat
        ScrollPane scrollPane = new ScrollPane(pdfContainer);
        scrollPane.setFitToWidth(true); // Passt die Breite des ScrollPane-Inhalts an

        this.setCenter(scrollPane); // Zeige das scrollbare PDF in der Mitte an
    }

    //TODO WordAnzeige
    private void displayDocx(File docxFile) {
        Label placeholder = new Label("Die Anzeige von .docx-Dateien wird noch nicht unterstützt.");
        placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: grey;");
        this.setCenter(placeholder);
    }

    //TODO MarkDownAnzeige
    private void displayMarkdown(File mdFile) {
        // Zukünftige Implementierung:
        // 1. Markdown-Datei einlesen
        // 2. Mit einer Bibliothek (z.B. Flexmark) in HTML umwandeln
        // 3. Den HTML-String in einer WebView anzeigen
        try {
            String content = new String(Files.readAllBytes(mdFile.toPath()));
            // Hier würde die Konvertierung zu HTML stattfinden
            WebView webView = new WebView();
            // webView.getEngine().loadContent(htmlContent, "text/html");
            
            // Vorerst nur als Text anzeigen
            Label placeholder = new Label("Markdown-Vorschau ist noch nicht implementiert.\n\nInhalt:\n" + content);
            this.setCenter(new ScrollPane(placeholder));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayUnsupportedFormat(File file) {
        Label placeholder = new Label("Dateiformat '" + getFileExtension(file) + "' wird nicht unterstützt.");
        placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: red;");
        this.setCenter(placeholder);
    }

    // liefert Dateiendung zurück
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // keine Dateiendung
        }
        return name.substring(lastIndexOf + 1);
    }



    private void findAndListFormFields(File pdfFile) {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm != null) {
                List<PDField> fields = acroForm.getFields();
                System.out.println("Gefundene Formularfelder:");
                for (PDField field : fields) {
                    // Den Namen des Feldes für die UI ausgeben
                    System.out.println("- " + field.getFullyQualifiedName());
                    // Hier könntest du Logik zum visuellen Hervorheben des Feldes einfügen,
                    // z.B. durch Ändern der Rahmenfarbe des Widgets.
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillFormField(File sourceFile, File destinationFile, String fieldName, String value) {
        try (PDDocument document = Loader.loadPDF(sourceFile)) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm != null) {
                PDField field = acroForm.getField(fieldName);
                if (field != null) {
                    field.setValue(value);
                    System.out.println("Feld '" + fieldName + "' mit Wert '" + value + "' ausgefüllt.");
                } else {
                    System.err.println("Fehler: Feld mit Namen '" + fieldName + "' nicht gefunden.");
                }
            }
            // Speichere die Änderungen in einer neuen Datei
            document.save(destinationFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
