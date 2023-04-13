package org.example.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageBox {
    public static void displayError(String titre, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(titre);
        HBox hBox = new HBox();
        Font font = Font.font("verdana", 15);

        Label header = new Label("Error");
        header.setFont(font);
        header.setPadding(new Insets(5, 105, 5, 5));
        header.setAlignment(Pos.TOP_LEFT);


        ImageView errorIcon = new ImageView(new Image(MessageBox.class.getResourceAsStream("errorIcon.png")));
        errorIcon.setFitHeight(20);
        errorIcon.setFitWidth(20);


        hBox.getChildren().add(header);
        hBox.getChildren().add(errorIcon);


        Label label = new Label();
        label.setText(message);
        label.setPadding(new Insets(10, 10, 10, 10));
        Button boutonOk = new Button("OK");
        boutonOk.setOnAction(e -> window.close());

        VBox layout = new VBox();
        layout.getChildren().addAll(hBox, new Separator());
        layout.getChildren().addAll(label, boutonOk);
        layout.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void displayInfo(String titre, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(titre);
        HBox hBox = new HBox();
        Font font = Font.font("verdana", 15);

        Label header = new Label("Message");
        header.setFont(font);
        header.setPadding(new Insets(5, 105, 5, 5));
        header.setAlignment(Pos.TOP_LEFT);


        ImageView errorIcon = new ImageView(new Image(MessageBox.class.getResourceAsStream("infoIcon.png")));
        errorIcon.setFitHeight(20);
        errorIcon.setFitWidth(20);


        hBox.getChildren().add(header);
        hBox.getChildren().add(errorIcon);


        Label label = new Label();
        label.setText(message);
        label.setPadding(new Insets(10, 10, 10, 10));
        Button boutonOk = new Button("OK");
        boutonOk.setOnAction(e -> window.close());

        VBox layout = new VBox();
        layout.getChildren().addAll(hBox, new Separator());
        layout.getChildren().addAll(label, boutonOk);
        layout.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
