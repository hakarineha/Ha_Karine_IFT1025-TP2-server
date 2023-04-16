package org.example.gui;

import client.controller.Client;
import server.models.Course;

import java.util.List;

/**
 * Classe contrôleur qui lie la vue de la classe ClientGui avec la classe Client.
 * Cette classe ClientController permet donc d'isoler l'affichage graphique de l'envoi des requêtes et du traitement de celles-ci.
 */
public class ClientController {
    /**
     * Instance de la classe client.
     */
     Client client;

    /**
     * Constructeur de la classe ClientController.
     * @param client Client connecté au serveur.
     */
    public ClientController(Client client) {
        this.client = client;
    }

    /**
     * Obtient la liste de cours d'une session spécifiée.
     * @param sessionEnCours String de la session sélectionnée, soit "hiver", "ete" ou "automne".
     * @return La liste de cours de la session spécifiée.
     */
    public List<Course> loadCourses(String sessionEnCours) {
        return client.getCourseSession(sessionEnCours);
    }

    /**
     * Remplit un formulaire d'inscription.
     * @param prenom Le prénom entré de l'étudiant(e) la liste de cours disponibles pour la session demandée.
     * @param nom Le nom de l'étudiant(e) qui figure dans le formulaire d'inscription.
     * @param email L'adresse courriel de l'étudiant(e) qui figure dans le formulaire d'inscription.
     * @param matricule La matricule de l'étudiant(e) qui figure dans le formulaire d'inscription.
     * @param codeCours Le code de cours auquel l'étudiant(e) souhaite s'inscrire.
     * @param coursesInSession La liste de cours disponibles pour la session demandée.
     * @return Un boolean true si l'inscription a été enregistrée par le serveur et false dans le cas contraire.
     */
    public boolean submitRegistration(String prenom, String nom, String email, String matricule, String codeCours, List<Course> coursesInSession) {
        return client.registerStudent(prenom, nom, email, matricule, codeCours, coursesInSession);
    }

    /**
     * Indique si la fenêtre d'erreur dû à une entrée de courriel invalide doit être affichée.
     * @param email L'adresse courriel entrée par l'utilisateur.
     * @return Un boolean true si l'adresse courriel entrée par l'utilisateur est invalide.
     */
    public boolean showEmailInvalidModal(String email) {
        return !client.isEmailValid(email);
    }

    /**
     * Indique si la fenêtre d'erreur dû à une entrée de matricule invalide doit être affichée.
     * @param matricule La matricule entrée par l'utilisateur.
     * @return Un boolean true si la matricule entrée par l'utilisateur est invalide.
     */
    public boolean showMatriculeInvalidModal(String matricule) {
        return !client.isMatriculeValid(matricule);
    }

}
