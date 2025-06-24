package com.docassist;

import javafx.scene.control.Button;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class HorizontalToolbar extends HBox {
    public HorizontalToolbar() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setStyle("-fx-background-color:rgb(150, 50, 150);");

        Button btnAddTxt = new Button("text");

        Button btnAddImg = new Button("image");

        this.getChildren().addAll(btnAddTxt, btnAddImg);
    }
}
