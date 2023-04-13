package client.controller;

import client.model.Commande;
import client.model.Request;
import server.EventHandler;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Socket socket;
    public final int port;
    public ObjectInputStream objectInputStream; // note à moi-même: pour lire
    public ObjectOutputStream objectOutputStream; // note à moi-même: pour écrire l'objet sur un stream dans son format sérialisé
    private final ArrayList<EventHandler> handlers;

    public Client(int port) {
        this.port = port;
        this.handlers = new ArrayList<>();
        this.addEventHandler(this::handleEvents);
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

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    public void handleEvents(String cmd, String arg) {
        System.out.println("rediriger les requests vers les méthodes");
//        if (cmd.equals(REGISTER_COMMAND)) {
//            handleRegistration();
//        } else if (cmd.equals(LOAD_COMMAND)) {
//            handleLoadCourses(arg);
//        }
    }

    public void run() {
//        try {
////            objectInputStream = new ObjectInputStream(socket.getInputStream());
////            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//        } catch (IOException e) {
//            System.err.print("IOException: " + e);
//        }


    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        socket.close();
    }

    /**
     * @param session La session choisie, soit automne, hiver ou été.
     * @return Retourne une ArrayList de type Course envoyée du serveur. Cet ArrayList</Course> contient seulement les cours de la session choisie.
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

    public boolean registerStudent(String prenom, String nom, String email, String matricule, String codeCours, List<Course> coursesInSession) {
        //TODO: check that the course id offered in that session

        Course course = coursesInSession.stream().filter(course1 -> course1.getCode().equalsIgnoreCase(codeCours)).findFirst().orElseThrow(() -> new RuntimeException("Le cours demandé n'existe pas dans cette session."));

        connectToServer();
        Request request = new Request(Commande.INSCRIRE, "");

        try {
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RegistrationForm registrationForm = new RegistrationForm(prenom, nom, email, matricule, course);
        try {
            objectOutputStream.writeObject(registrationForm);
            objectOutputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            return (boolean) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.print(e);
        }

        return false;
    }

    public boolean isEmailValid(String email) {
        //TODO: implémenter cette méthode
        return email.contains("@") && email.contains(".");
    }

    public boolean isMatriculeValid(String matricule) {
        if (matricule.length() == 8){
            try {
                Double.parseDouble(matricule);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public boolean isCodeCoursValid(String codeCours, List<Course> coursesInSession) {
        for (Course course : coursesInSession) {
            if (course.getCode().equalsIgnoreCase(codeCours)) {
                return true;
            }
        }
        return false;
    }
}
