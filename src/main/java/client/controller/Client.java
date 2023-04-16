package client.controller;

import client.model.Commande;
import client.model.Request;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe Client permet au client de se connecter au serveur et d'envoyer des requêtes
 * pour consulter les cours offerts pour une session et/ou pour s'inscrire à un cours.
 * Cette classe Client joue le rôle de contrôleur entre la vue et le serveur.
 */

public class Client {
    private Socket socket;

    private final int port;

    private ObjectInputStream objectInputStream;

    private ObjectOutputStream objectOutputStream;

    /**
     * Le constructeur du client.
     * @param port Le port sur lequel le serveur écoute les requêtes qui lui sont envoyées.
     */
    public Client(int port) {
        this.port = port;
    }

    private void connectToServer() {
        try {
            this.socket = new Socket("localhost", this.port);
            objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au serveur");
            new RuntimeException(e);
        }
    }

    /**
     * Déconnecte le client en fermant les flux d'entrée, de sortie ainsi que le socket.
     * @throws IOException si une erreur lors de l'arrêt du flux a lieu.
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        socket.close();
    }

    /**
     * Envoie la requête au serveur et récupère la liste de cours pour une session spécifiée.
     * @param session La session choisie, soit "automne", "hiver" ou "ete".
     * @return ArrayList de type Course envoyée du serveur. Celle-ci contient seulement les cours de la session choisie.
     */
    public ArrayList<Course> getCourseSession(String session) {
        connectToServer();
        Request request = new Request(Commande.CHARGER, session);
        try {
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            return (ArrayList<Course>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.print(e);
        }
        return new ArrayList<>();
    }

    /**
     * Envoie la requête d'inscription au serveur et envoie le formulaire d'inscription pour inscrire l'étudiant(e).
     * @param prenom le prénom de l'étudiant(e) qui figure dans le formulaire d'inscription
     * @param nom le nom de l'étudiant(e) qui figure dans le formulaire d'inscription
     * @param email l'adresse courriel de l'étudiant(e) qui figure dans le formulaire d'inscription
     * @param matricule la matricule de l'étudiant(e) qui figure dans le formulaire d'inscription
     * @param codeCours le code de cours auquel l'étudiant(e) souhaite s'inscrire
     * @param coursesInSession la liste de cours disponibles pour la session demandée
     * @return boolean true si l'inscription a été enregistrée par le serveur et false dans le cas contraire.
     */
    public boolean registerStudent(String prenom, String nom, String email, String matricule, String codeCours, List<Course> coursesInSession) {
        // Recherche du cours de type Course associé au code de cours.
        Course courseToRegister = coursesInSession.stream().filter(course -> course.getCode().equalsIgnoreCase(codeCours)).findFirst().orElseThrow(() -> new RuntimeException("Le cours demandé n'existe pas dans cette session."));

        connectToServer();
        Request request = new Request(Commande.INSCRIRE, "");
        try {
            // Flux de sortie : envoi de la requête
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RegistrationForm registrationForm = new RegistrationForm(prenom, nom, email, matricule, courseToRegister);
        try {
            // Flux de sortie : envoi du formulaire d'inscription avec les informations de l'étudiant(e)
            objectOutputStream.writeObject(registrationForm);
            objectOutputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            // Flux d'entrée : retourne true si l'inscription a été enregistrée.
            return (boolean) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.print(e);
        }

        return false;
    }

    /**
     * Vérifie si l'adresse courriel entrée par l'utilisateur est valide.
     * Si l'adresse courriel contient un "@" et un ".".
     * @param email l'adresse courriel entrée par l'utilisateur
     * @return boolean true si l'adresse courriel respecte les critères mentionnés et false si c'est invalide.
     */
    public boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    /**
     * Vérifie si la matricule entréepar l'utilisateur est valide.
     * Si la matricule est constituée de 8 chiffres.
     * @param matricule la matricule entrée par l'utilisateur
     * @return boolean true si la matricule respecte les critères mentionnés et false si c'est invalide.
     */
    public boolean isMatriculeValid(String matricule) {
        if (matricule.length() == 8) {
            try {
                Double.parseDouble(matricule);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Vérifie si le code de cours entré par l'utilisateur correspond à un code de cours existant.
     * @param codeCours le code de cours entré par l'utilisateur.
     * @param coursesInSession la liste de cours pour une session.
     * @return boolean true si le code de cours est valide et false si ce n'est pas le cas.
     */
    public boolean isCodeCoursValid(String codeCours, List<Course> coursesInSession) {
        for (Course course : coursesInSession) {
            if (course.getCode().equalsIgnoreCase(codeCours)) {
                return true;
            }
        }
        return false;
    }
}
