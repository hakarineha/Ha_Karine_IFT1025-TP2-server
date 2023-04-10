package client.gui;

import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ClientGUI extends Application {
    public static void main(String[] args) {
        ClientGUI.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox rootGauche = new VBox();
        VBox rootDroite = new VBox();
        Scene sceneGauche = new Scene(rootGauche, 400, 150);
        Scene sceneDroite = new Scene(rootDroite, 400, 150);
        Text titreG = new Text("Liste des cours");
        Text titreD = new Text("Formulaire d'inscription");



    }
}
