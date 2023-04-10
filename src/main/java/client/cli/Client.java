package client.cli;

import server.EventHandler;
import server.models.Course;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Socket socket;
    public ObjectInputStream objectInputStream; // note à moi-même: pour lire
    public ObjectOutputStream objectOutputStream; // note à moi-même: pour écrire l'objet sur un stream dans son format sérialisé
    private final ArrayList<EventHandler> handlers;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.flush();
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.handlers = new ArrayList<>();
        this.addEventHandler(this::handleEvents);
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
     *
     * @param session La session choisie, soit automne, hiver ou été.
     * @return Retourne une ArrayList de type Course envoyée du serveur. Cet ArrayList</Course> contient seulement les cours de la session choisie.
     */
    public ArrayList<Course> getCourseSession(String session) {
        Request request = new Request(Commande.CHARGER, session);
        try {
            objectOutputStream.writeObject(request);
            return (ArrayList<Course>) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.print(e);
        }
        return new ArrayList<>();
    }

    public boolean registerStudent(String prenom, String nom, String email, String matricule, String codeCours, List<Course> coursesInSession) {
        //TODO: check that the course id offered in that session
        //TODO: envoyer Request(Commande.INSCRIRE, "") ensuite tu flush.
        //TODO: objet RegistrationForm, envoies, flush
        //TODO: reçois une confirmation en boolean du serveur


        return false;
    }
}
