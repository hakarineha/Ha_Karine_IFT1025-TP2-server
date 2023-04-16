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

/**
 * La classe MessageBox permet l'affichage de boîtes de dialogue qui avise l'utilisateur si son inscription a été enregistrée ou si elle contenait une erreur.
 */

public class MessageBox {
    /**
     * Affiche une boîte de dialogue indiquant quelles sont les erreurs d'entrées de l'utilisateur durant sa tentative d'inscription à un cours.
     * @param titre Le titre de la fenêtre.
     * @param message Le message d'erreur affiché dans la boîte.
     */
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
        label.setPadding(new Insets(10, 20, 10, 20));
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

    /**
     * Affiche une boîte de dialogue indiquant quelles sont les erreurs d'entrées de l'utilisateur durant sa tentative d'inscription à un cours.
     * @param titre Le titre de la fenêtre.
     * @param message Le message affiché dans la boîte.
     */
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

        ImageView infoIcon = new ImageView(new Image(MessageBox.class.getResourceAsStream("infoIcon.png")));
        infoIcon.setFitHeight(20);
        infoIcon.setFitWidth(20);

        hBox.getChildren().add(header);
        hBox.getChildren().add(infoIcon);

        Label label = new Label();
        label.setText(message);
        label.setPadding(new Insets(10, 20, 10, 20));
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
