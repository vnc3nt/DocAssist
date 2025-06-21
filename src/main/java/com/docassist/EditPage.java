package com.docassist;

import java.io.File;
import java.util.List;

import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.Loader;


public class EditPage extends Page{
    Stage primaryStage;

    public EditPage(Stage primaryStage, File file) {
        this.primaryStage = primaryStage;

        Scene editScene = new Scene(this, 720, 480);
        primaryStage.setScene(editScene) ;
        primaryStage.setTitle("DocAssist");
        primaryStage.show();
        this.setStyle("-fx-padding: 30; -fx-font-family: 'Arial'; -fx-font-size: 16");

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
