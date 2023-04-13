package org.example.gui;

import client.controller.Client;
import server.models.Course;

import java.util.ArrayList;
import java.util.List;

public class ClientController {

    Client client;

    public ClientController(Client client) {
        this.client = client;
    }

    public List<Course> loadCourses(String sessionEnCours) {
        ArrayList<Course> coursesSession = client.getCourseSession(sessionEnCours);
        return coursesSession;
    }
    public boolean showEmailInvalidModal(String email){
        return !client.isEmailValid(email);
    }
    public boolean showMatriculeInvalidModal(String matricule){
        return !client.isMatriculeValid(matricule);
    }

    public boolean submitRegistration(String prenom, String nom, String email, String matricule, String codeCours, List<Course> coursesInSession){
        return client.registerStudent(prenom, nom, email, matricule, codeCours, coursesInSession);
    }


}
