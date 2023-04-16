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

/**
 * La classe ClientGui est une interface graphique qui lance le programme du client et
 * qui réagit à l'utilisateur lorsque ce dernier tente de consulter des cours de l'UdeM
 * et d'envoyer une inscription pour un cours.
 */

public class ClientGui extends Application {
    private String sessionEnCours;
    private final Client client = new Client(1337);
    private final ClientController controller = new ClientController(client);

    /**
     * Affiche l'interface graphique de l'application.
     *
     * @param stage La fenêtre de l'application.
     */
    @Override
    public void start(Stage stage) {
        // La racine de l'arbre d'objets de la scène.
        HBox root = new HBox();
        root.setMaxSize(700, 700);
        root.setStyle("-fx-background-color: BEIGE");
        Font font = Font.font("verdana", 20);

        // Le contenu graphique à afficher dans la fenêtre.
        Scene scene = new Scene(root);

        // Création d'un séparateur sep.
        Separator sep = new Separator();
        sep.setPadding(new Insets(5, 5, 5, 5));

// ******************** GAUCHE : Liste de cours par session ********************
        VBox leftContainer = new VBox();
        leftContainer.setAlignment(Pos.CENTER);
        leftContainer.setPadding(new Insets(20, 20, 50, 50));

        // Le tableau affichant la liste cours pour une session spécifiée. Le nom et le code de cours sont affichés.
        TableView<Course> tableau = getCourseTableView();
        ObservableList<Course> selectedCourses = getCourseTableViewSelectionModel(tableau).getSelectedItems();

        leftContainer.getChildren().add(getTableLabel(font));
        leftContainer.getChildren().add(tableau);
        leftContainer.getChildren().add(getBottomHBox(tableau));

// ******************** DROITE : Formulaire d'inscription ********************
        VBox rightContainer = getFormulaire(font, selectedCourses);

        root.getChildren().add(sep);
        root.getChildren().add(leftContainer);
        root.getChildren().add(rightContainer);

// ******************** STAGE : Fenêtre de l'application ********************
        stage.setTitle("Inscription UdeM");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * @return La structure du tableau pouvant contenir le code du cours et le nom.
     */
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

    /**
     * Permet à l'utilisateur de sélectionner une rangée du tableau TableView.
     *
     * @param tableau Le tableau de cours créé par la méthode getCourseTableView().
     * @return Le tableau selectionModel qui permet la sélection d'un élément spécifique du contenu.
     */
    private static TableView.TableViewSelectionModel<Course> getCourseTableViewSelectionModel(TableView<Course> tableau) {
        TableView.TableViewSelectionModel<Course> selectionModel = tableau.getSelectionModel();
        // L'utilisateur peut sélectionner une seule rangée contenant un cours ainsi que son code.
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        return selectionModel;
    }

    /**
     * Organise le visuel du formulaire d'inscription qui se trouve à droite de la fenêtre.
     *
     * @param font            La police d'écriture et la taille du texte pour l'affichage du Label "Formulaire d'inscription".
     * @param selectedCourses La liste de cours pour la session demandée.
     * @return Un conteneur d'éléments "container" qui est un VBox dans lequel se trouve le formulaire d'inscription et le bouton d'envoi.
     */
    private VBox getFormulaire(Font font, ObservableList<Course> selectedCourses) {
        // Conteneur d'éléments
        VBox container = new VBox();
        container.setPadding(new Insets(10, 50, 10, 0));
        container.setAlignment(Pos.CENTER);

        // Texte indiquant l'information à entrer
        Text prenomLabel = new Text("Prénom");
        Text nomLabel = new Text("Nom");
        Text emailLabel = new Text("Email");
        Text matriculeLabel = new Text("Matricule");

        // Les champs de texte dans lesquels l'utilisateur entre son information.
        TextField prenomField = new TextField();
        TextField nomField = new TextField();
        TextField emailField = new TextField();
        TextField matriculeField = new TextField();

        // Organisation visuelle du formulaire d'inscription
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

        // Bouton d'envoi du formulaire d'inscription
        Button boutonEnvoyer = new Button("Envoyer");
        boutonEnvoyer.setOnAction(event -> {
            String prenom = prenomField.getText();
            String nom = nomField.getText();
            String email = emailField.getText();
            String matricule = matriculeField.getText();
            sendRegistration(selectedCourses, prenom, nom, email, matricule);
        });

        // Assemblage d'éléments graphiques dans le conteneur
        container.getChildren().add(getFormLabel(font));
        container.getChildren().add(gridPane);
        container.getChildren().add(boutonEnvoyer);
        return container;
    }

    /**
     * Vérifie si l'inscription est complète et valide pour ensuite
     * appeler la méthode du contrôleur qui se charge d'envoyer le formulaire d'inscription de l'utilisateur.
     * Affiche une boîte de dialogue d'erreur reliée à l'inscription ou de confirmation à l'inscription.
     *
     * @param selectedCourses La liste de cours pour la session demandée (ayant un cours de sélectionné par l'utilisateur).
     * @param prenom          Le prénom entré par l'utilisateur.
     * @param nom             Le nom entré par l'utilisateur.
     * @param email           L'adresse courriel entrée par l'utilisateur.
     * @param matricule       La matricule entrée par l'utilisateur.
     */
    private void sendRegistration(ObservableList<Course> selectedCourses, String prenom, String nom, String email, String matricule) {
        // Messages pouvant être affichés dans la boîte de dialogue.
        String messageSelectionInvalid = "Vous devez sélectionner un cours!";
        String messageFormulaireinvalid = "Le formulaire est invalide!";
        String messageEmailInvalid = "Le champ Email est invalide!";
        String messageMatriculeInvalid = "Le champ Matricule est invalide!";

        // Gestion des erreurs possibles de l'utilisateur.
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

        // Si aucune erreur n'a été détectée.
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

    /**
     * Organise le conteneur d'éléments qui se trouve sous le tableau de liste de cours.
     * À gauche, il y a le bouton du menu qui affiche les sessions "Hiver", "Ete" et "Automne".
     * À côté, il y a le bouton pour charger la liste de cours associée à la session sélectionnée.
     * @param tableau Le tableau pouvant contenir le code du cours et le nom.
     * @return Le conteneur d'éléments HBox contenant le bouton de choix de session et le bouton pour charger.
     */
    private HBox getBottomHBox(TableView<Course> tableau) {
        // En bas du tableau de cours.
        HBox enBas = new HBox();
        enBas.setPadding(new Insets(25, 5, 5, 5));
        // Bouton pour choisir une session (à gauche).
        enBas.getChildren().add(getBoutonMenuChoixDeSession());
        enBas.getChildren().add(getBoutonCharger(tableau));
        return enBas;
    }

    /**
     * Obtient et crée le bouton qui permet la sélection de la session en cours.
     * Assigne la sessionEnCours comme étant la session sélectionnée dans le menu.
     * L'utilisateur peut choisir entre "Hiver", "Ete" ou "Automne".
     * @return Le bouton pour sélectionner la session, soit le boutonChoixSession.
     */
    private MenuButton getBoutonMenuChoixDeSession() {
        MenuButton boutonChoixSession = new MenuButton("Choisir session");
        MenuItem itemHiver = new MenuItem("Hiver");
        MenuItem itemEte = new MenuItem("Ete");
        MenuItem itemAutomne = new MenuItem("Automne");
        boutonChoixSession.getItems().addAll(itemHiver, itemEte, itemAutomne);
        // On met à jour la variable de la classe sessionEnCours, comme étant la session sélectionnée dans le menu.
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

    /**
     * Crée le bouton qui obtient et charge la liste de cours dans le tableau affiché.
     * Le bouton met à jour le contenu du tableau en fonction de la session sélectionnée.
     * @param tableau Le tableau affichant les cours pour une session spécifiée.
     * @return Le bouton pour charger la liste de cours selon la session sélectionnée, soit le boutonCharger.
     */
    private Button getBoutonCharger(TableView<Course> tableau) {
        Button boutonCharger = new Button("charger");
        boutonCharger.setOnAction(event -> {
            List<Course> coursesInSession = controller.loadCourses(sessionEnCours);
            tableau.getItems().clear();
            tableau.getItems().addAll(coursesInSession);
        });
        return boutonCharger;
    }

    /**
     * Créé le titre du côté gauche : "Liste de cours".
     * @param font La police d'écriture et la taille du texte.
     * @return Le titre "Liste de cours" de type Label.
     */
    private static Label getTableLabel(Font font) {
        Label labelGauche = new Label("Liste des cours");
        labelGauche.setFont(font);
        labelGauche.setPadding(new Insets(10, 10, 10, 10));
        return labelGauche;
    }

    /**
     * Créé le titre du côté droit : "Formulaire d'inscription".
     * @param font La police d'écriture et la taille du texte.
     * @return Le titre "Formulaire d'inscription" de type Label.
     */
    private static Label getFormLabel(Font font) {
        Label labelDroite = new Label("Formulaire d'inscription");
        labelDroite.setFont(font);
        return labelDroite;
    }

    /**
     * Démarre le programme d'interface graphique.
     */
    public static void main(String[] args) {
        launch();
    }
}