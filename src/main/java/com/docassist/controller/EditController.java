package com.docassist.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditController {

    @FXML private ScrollPane contentScrollPane;
    @FXML private Slider zoomSlider;  

    private Stage primaryStage;
    private File currentFile;

    private VBox pageBox;
    private double baseWidth;
    private double baseHeight;
    private static final double PADDING = 50.0;


    public void initData(Stage stage, File file) {
        decorateSliderThumb(); 

        this.primaryStage = stage;
        this.currentFile  = file;
        primaryStage.setTitle("DocAssist - " + currentFile.getName());

        zoomSlider.valueProperty().addListener(
            (obs, ov, nv) -> applyScale(nv.doubleValue() / 100)
        );
        contentScrollPane.addEventFilter(ScrollEvent.SCROLL, this::handleWheelZoom);

        processFile(file);
    }

    /** setzt den Zoomfaktor (1.0 = 100 %) */
    private void applyScale(double factor) {
        if (pageBox != null) {          // nur PDFs werden skaliert
            pageBox.setScaleX(factor);
            pageBox.setScaleY(factor);
            double w = baseWidth  + 2 * PADDING;    // links + rechts
            double h = baseHeight + 2 * PADDING;    // oben  + unten
            pageBox.setPrefSize(w * factor, h * factor);
        }
    }

    /** Strg/Cmd + Mausrad Zoom */
    private void handleWheelZoom(ScrollEvent e) {
        if (e.isControlDown() || e.isMetaDown()) {
            double step  = e.getDeltaY() > 0 ? 10 : -10;
            double value = clamp(zoomSlider.getValue() + step,
                                 zoomSlider.getMin(), zoomSlider.getMax());
            zoomSlider.setValue(value);
            e.consume();
        }
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
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

    private void displayPdf(File pdfFile) {
        pageBox = new VBox(10);
        pageBox.setAlignment(Pos.CENTER);
        pageBox.setPadding(new Insets(PADDING));

        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, 150);
                WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                
                ImageView imageView = new ImageView(fxImage);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(600);
                
                pageBox.getChildren().add(imageView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Fehler beim Laden des PDFs: " + e.getMessage());
            contentScrollPane.setContent(errorLabel); // Fehler im ScrollPane anzeigen
            return;
        }

        pageBox.applyCss();
        pageBox.layout();
        baseWidth  = pageBox.getBoundsInLocal().getWidth();
        baseHeight = pageBox.getBoundsInLocal().getHeight();

        contentScrollPane.setContent(pageBox);
        applyScale(zoomSlider.getValue() / 100);   // Startzoom
    }

    private void displayDocx(File docxFile) {
        Label placeholder = new Label("Die Anzeige von .docx-Dateien wird noch nicht unterstützt.");
        placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: grey;");
        contentScrollPane.setContent(placeholder);
    }

    private void displayMarkdown(File mdFile) {
        try {
            String content = new String(Files.readAllBytes(mdFile.toPath()));
            Label placeholder = new Label("Markdown-Vorschau ist noch nicht implementiert.\n\nInhalt:\n" + content);
            contentScrollPane.setContent(placeholder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayUnsupportedFormat(File file) {
        Label placeholder = new Label("Dateiformat '" + getFileExtension(file) + "' wird nicht unterstützt.");
        placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: red;");
        contentScrollPane.setContent(placeholder);
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf + 1);
    }
    
    // --- Methoden für die Toolbar-Buttons ---

    @FXML
    private void handleAddText(ActionEvent event) {
        System.out.println("Button 'Text hinzufügen' geklickt!");
        // Hier kommt die Logik zum Hinzufügen von Text
    }

    @FXML
    private void handleAddImage(ActionEvent event) {
        System.out.println("Button 'Bild hinzufügen' geklickt!");
        // Hier kommt die Logik zum Hinzufügen von Bildern
    }

    private void decorateSliderThumb() {

        // erst ausführen, wenn der Skin existiert
        Platform.runLater(() -> {
            // Thumb-Knoten ermitteln
            StackPane thumb = (StackPane) zoomSlider.lookup(".thumb");
            if (thumb != null) {
                // Beschriftung erzeugen und binden
                Label thumbLabel = new Label();
                thumbLabel.textProperty().bind(
                    Bindings.createStringBinding(
                        () -> String.format("%.1fx", zoomSlider.getValue() / 100.0),
                        zoomSlider.valueProperty())
                );
                thumbLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: black; padding:0;");

                thumb.getChildren().add(thumbLabel);
            }
        });
    }
}
