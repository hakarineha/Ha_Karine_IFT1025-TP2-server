package org.example.gui;

import client.controller.Client;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.models.Course;

import java.util.List;


public class ClientGui extends Application {
    private String sessionEnCours;
    private final Client client = new Client(1337);
    private final ClientController controller = new ClientController(client);

    @Override
    public void start(Stage stage) {

        HBox root = new HBox();
        root.setStyle("-fx-background-color: BEIGE");
        Scene scene = new Scene(root);
        Separator sep = new Separator();
        sep.setPadding(new Insets(5, 5, 5, 5));
        root.setMaxSize(700, 700);
        Font font = Font.font("verdana", 20);


// ******************** GAUCHE ********************
        VBox leftContainer = new VBox();
        leftContainer.setAlignment(Pos.CENTER);
        leftContainer.setPadding(new Insets(20, 20, 50, 50));

        // Cours
        TableView<Course> tableau = getCourseTableView();
        ObservableList<Course> selectedCourses = getCourseTableViewSelectionModel(tableau).getSelectedItems();

        leftContainer.getChildren().add(getTableLabel(font));
        leftContainer.getChildren().add(tableau);
        leftContainer.getChildren().add(getBottomHBox(tableau));


// ******************** DROITE ********************
        VBox rightContainer = getFormulaire(font, selectedCourses);

        root.getChildren().add(sep);
        root.getChildren().add(leftContainer);
        root.getChildren().add(rightContainer);


        stage.setTitle("Inscription UdeM");
        stage.setScene(scene);
        stage.show();


    }

    private static Label getTableLabel(Font font) {
        Label labelGauche = new Label("Liste des cours");
        labelGauche.setFont(font);
        labelGauche.setPadding(new Insets(10, 10, 10, 10));
        return labelGauche;
    }

    private static TableView.TableViewSelectionModel<Course> getCourseTableViewSelectionModel(TableView<Course> tableau) {
        TableView.TableViewSelectionModel<Course> selectionModel = tableau.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        return selectionModel;
    }

    private VBox getFormulaire(Font font, ObservableList<Course> selectedCourses) {
        VBox container = new VBox();
        container.setPadding(new Insets(10, 50, 10, 0));
        container.setAlignment(Pos.CENTER);

        // creating labels
        Text prenomLabel = new Text("Prénom");
        Text nomLabel = new Text("Nom");
        Text emailLabel = new Text("Email");
        Text matriculeLabel = new Text("Matricule");
        // creating text fields
        TextField prenomField = new TextField();
        TextField nomField = new TextField();
        TextField emailField = new TextField();
        TextField matriculeField = new TextField();

        // Formulaire d'inscription
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 50));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(prenomLabel, 0, 0);
        gridPane.add(prenomField, 1, 0);
        gridPane.add(nomLabel, 0, 1);
        gridPane.add(nomField, 1, 1);
        gridPane.add(emailLabel, 0, 2);
        gridPane.add(emailField, 1, 2);
        gridPane.add(matriculeLabel, 0, 3);
        gridPane.add(matriculeField, 1, 3);

        // creating envoi button
        Button boutonEnvoyer = new Button("Envoyer");
        boutonEnvoyer.setOnAction(event -> {
            String prenom = prenomField.getText();
            String nom = nomField.getText();
            String email = emailField.getText();
            String matricule = matriculeField.getText();
            sendRegistration(selectedCourses, prenom, nom, email, matricule);
        });

        container.getChildren().add(getTitleLabel(font));
        container.getChildren().add(gridPane);
        container.getChildren().add(boutonEnvoyer);
        return container;
    }

    private void sendRegistration(ObservableList<Course> selectedCourses, String prenom, String nom, String email, String matricule) {
        String messageFormulaireinvalid = "Le formulaire est invalide!";
        String messageEmailInvalid = "Le champ Email est invalide!";
        String messageMatriculeInvalid = "Le champ Matricule est invalide!";
        String messageSelectionInvalid = "Vous devez sélectionner un cours!";

        String errorMessage = messageFormulaireinvalid;

        int errorCountWhenRegister = 0;

        if (selectedCourses.isEmpty()) {
            errorMessage += "\n" + messageSelectionInvalid;
            errorCountWhenRegister += 1;
        }

        if (controller.showEmailInvalidModal(email)) {
            errorMessage += "\n" + messageEmailInvalid;
            errorCountWhenRegister += 1;
        }
        if (controller.showMatriculeInvalidModal(matricule)) {
            errorMessage += "\n" + messageMatriculeInvalid;
            errorCountWhenRegister += 1;
        }
        if (errorCountWhenRegister > 0) {
            MessageBox.displayError("Error", errorMessage);
        }
        if (errorCountWhenRegister == 0) {
            Course course = selectedCourses.get(0);
            String successMessage = "Félicitations! " + prenom + " " + nom + " est inscrit(e)\n avec succès pour le cours " + course.getCode() + "!";
            if (controller.submitRegistration(prenom, nom, email, matricule, course.getCode(), selectedCourses)) {
                MessageBox.displayInfo("Message", successMessage);
            } else {
                MessageBox.displayError("Error", "Il y a eu un problème durant l'inscription. Réessayez.");
            }
        }
    }

    private static Label getTitleLabel(Font font) {
        Label labelDroite = new Label("Formulaire d'inscription");
        labelDroite.setFont(font);
        return labelDroite;
    }

    private static TableView<Course> getCourseTableView() {
        TableView<Course> tableau = new TableView<>();
        TableColumn<Course, String> cl1 = new TableColumn<>("Code");
        cl1.setCellValueFactory(new PropertyValueFactory<>("code"));
        TableColumn<Course, String> cl2 = new TableColumn<>("Cours");
        cl2.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableau.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableau.getColumns().add(cl1);
        tableau.getColumns().add(cl2);
        tableau.setMaxSize(300, 400);
        return tableau;
    }

    private HBox getBottomHBox(TableView<Course> tableau) {
        // En bas du tableau
        HBox enBas = new HBox();
        enBas.setPadding(new Insets(25, 5, 5, 5));
        // Bouton choix de session à gauche
        enBas.getChildren().add(getChoixSessionMenuButton());
        enBas.getChildren().add(getBoutonCharger(tableau));
        return enBas;
    }

    private Button getBoutonCharger(TableView<Course> tableau) {
        Button boutonCharger = new Button("charger");
        // Bouton pour charger à droite
        boutonCharger.setOnAction(event -> {
            List<Course> coursesInSession = controller.loadCourses(sessionEnCours);
            tableau.getItems().clear();
            tableau.getItems().addAll(coursesInSession);
        });
        return boutonCharger;
    }

    private MenuButton getChoixSessionMenuButton() {
        MenuButton boutonChoixSession = new MenuButton("Choisir session");
        // creating the menu items for the boutonChoixSession
        MenuItem itemHiver = new MenuItem("Hiver");
        MenuItem itemEte = new MenuItem("Ete");
        MenuItem itemAutomne = new MenuItem("Automne");
        boutonChoixSession.getItems().addAll(itemHiver, itemEte, itemAutomne);

        itemHiver.setOnAction(event -> {
            chooseSession("hiver");
            boutonChoixSession.setText("Hiver");

        });
        itemEte.setOnAction(event -> {
            chooseSession("ete");
            boutonChoixSession.setText("Ete");
        });
        itemAutomne.setOnAction(event -> {
            chooseSession("automne");
            boutonChoixSession.setText("Automne");
        });
        return boutonChoixSession;
    }


    private void chooseSession(String session) {
        this.sessionEnCours = session;
    }


    public static void main(String[] args) {
        launch();
    }
}