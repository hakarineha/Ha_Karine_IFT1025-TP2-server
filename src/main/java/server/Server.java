package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream; // note à moi-même: pour lire
    private ObjectOutputStream objectOutputStream; // note à moi-même: pour écrire l'objet sur un stream dans son format sérialisé
    private final ArrayList<EventHandler> handlers;


    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajouter un handler à la liste handlers
     * @param h est un handler.
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Appelle la fonction handle() de tous les handlers définis avec la commande et l'argument spécifié.
     * En d'autres mots, la méthode fait passer le message aux objets qui font la manipulation des événements
     * en découpant le traitement d'un événement.
     * @param cmd la commande
     * @param arg les arguments
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectOutputStream.flush();
                objectInputStream = new ObjectInputStream(client.getInputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            System.out.println("request obtained from client is" + line);
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }


    protected ArrayList<Course> getCoursesFromFile() {
        ArrayList<Course> courses = new ArrayList<>();

        try {
            RandomAccessFile file = new RandomAccessFile("/Users/karineha/Desktop/IFT1025/Ha_Karine_IFT1025-TP2-server/src/main/java/server/data/cours.txt", "r");
            String line = file.readLine();
            while (line != null) {
                String[] courseArray = line.split("\t");
                // [IFT1025, Prog1, Automne]
                Course course = new Course(courseArray[1], courseArray[0], courseArray[2]);
                courses.add(course);
                line = file.readLine();
            }
            file.close();

        } catch (IOException e) {
            System.err.print(e);
        }
        return courses;
    }

    /**
     * Lire un fichier texte contenant des informations sur les cours et les transformer en liste d'objets 'Course'.
     * La méthode filtre les cours par la session spécifiée en argument.
     * Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     * La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     *
     * @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        // TODO: implémenter cette méthode
        List<Course> courses = getCoursesFromFile()
                .stream()
                .filter(course -> course.getSession().equalsIgnoreCase(arg)) //filtre basé sur ce predicate (qui est une condition)
                .collect(Collectors.toList()); // on collecte, recueille ce qu'on a filtré, et donc convertir le stream filtré en liste

        try {
            objectOutputStream.writeObject(courses);
        } catch (IOException e) {
            System.err.print(e);
        }

    }

    /**
     * Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     * et renvoyer un message de confirmation au client.
     * La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        // TODO: implémenter cette méthode
        try {
            RegistrationForm registrationForm = (RegistrationForm) objectInputStream.readObject();
            FileWriter fwInscription = new FileWriter("/Users/karineha/Desktop/IFT1025/Ha_Karine_IFT1025-TP2-server/src/main/java/server/data/inscription.txt");
            BufferedWriter writer = new BufferedWriter(fwInscription);
//            String session = registrationForm.getCourse().getSession();
//            String codeCours = registrationForm.getCourse().getCode();
//            String matricule = registrationForm.getMatricule();
//            String prenom = registrationForm.getPrenom();
//            String nom = registrationForm.getNom();
//            String email = registrationForm.getEmail();
//            String inscription = session + "\t" + codeCours + "\t"
//                    + matricule + "\t" + prenom + "\t" + nom + "\t"
//                    + email + "\n";
            String inscription = registrationForm.getInscription();
            writer.append(inscription);
            writer.close();
            objectOutputStream.writeObject(true);

        } catch (IOException | ClassNotFoundException e) {
            System.err.print(e);
        }

        try {
            objectOutputStream.writeObject(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}

