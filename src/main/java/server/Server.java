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

/**
 * Server est une classe de serveur qui attend de recevoir des commandes d'un client, soit un étudiant de l'UdeM.
 * Ensuite, cette classe traite la commande et peut soit renvoyer au client une liste de cours pour une session en particulier, soit l'inscrire à un cours.
 */

public class Server {
    /**
     * La commande pour inscrire l'étudiant à un cours.
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     * La commande pour charger la liste de cours pour une session.
     */
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Constructeur de la classe Server.
     *
     * @param port Le port sur lequel le serveur écoute les commandes envoyées du client.
     * @throws IOException si une erreur d'entrée ou de sortie du stream a lieu.
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajoute un handler à la liste de handlers.
     *
     * @param h est un handler.
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Attend la connexion d'un client pour le connecter au serveur.
     * Une fois le client connecté, la méthode reçoit les commandes envoyées et déconnecte le client dès que l'écoute termine.
     * La méthode assigne les variables objectOutputStream et objectInputStream aux flux entrant et sortant associés au socket.
     */
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

    /**
     * Récupère la requête du client constituée d'une commande et des arguments.
     * Avertie tous les handlers lorsqu'une requête d'un client survient.
     *
     * @throws IOException            si une erreur du flux d'entrée de la requête du client a lieu.
     * @throws ClassNotFoundException si une classe utilisée n'est pas trouvée dans le chemin d'accès au répertoire de classes et de packages.
     */

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            System.out.println("request obtained from client is " + line);
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Sépare la ligne de requête du client en commande et en args pour ensuite les placer dans un objet de type Pair.
     *
     * @param line La ligne de requête qui contient la commande et l'argument.
     * @return la commande et les arguments en type Pair<>.
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Déconnecte le client en fermant les flux d'entrée, de sortie ainsi que le socket.
     *
     * @throws IOException si une erreur lors de l'arrêt du flux a lieu.
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Appelle la méthode qui gère l'inscription d'un(e) étudiant(e) ou bien la méthode qui charge la liste de cours, dépendamment de la commande reçue.
     *
     * @param cmd La commande de la requête, soit "INSCRIRE" ou "CHARGER".
     * @param arg Les arguments de la requête, soit un String vide, soit la session choisie, c'est-à-dire "hiver", "ete" ou "automne".
     */

    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     * Lis le fichier cours.txt et transforme chaque cours existant en type Course.
     * La méthode gère les exceptions si une erreur se produit lors de la lecture d'un fichier.
     *
     * @return ArrayList de Course contenant les cours existants.
     */

    protected ArrayList<Course> getCoursesFromFile() {
        ArrayList<Course> courses = new ArrayList<>();

        try {
            String file = "/data/cours.txt";
            System.out.println("File name in resources: " + file);
            try (InputStream in = getClass().getResourceAsStream(file); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line = reader.readLine();
                while (line != null) {
                    String[] courseArray = line.split("\t");
                    Course course = new Course(courseArray[1], courseArray[0], courseArray[2]);
                    courses.add(course);
                    line = reader.readLine();
                }
            }

        } catch (IOException e) {
            System.err.print(e);
        }
        return courses;
    }

    /**
     * Renvoie la liste de cours de type Course (filtrée selon la session demandée) au client.
     * La méthode gère les exceptions si une erreur se produit dans le flux de sortie.
     *
     * @param arg La session pour laquelle on veut récupérer la liste de cours, c'est-à-dire "hiver", "automne" ou "ete".
     */
    public void handleLoadCourses(String arg) {
        // Filtrage de la liste de cours basé sur une condition, soit la session spécifiée en argument.
        List<Course> courses = getCoursesFromFile().stream().filter(course -> course.getSession().equalsIgnoreCase(arg)).collect(Collectors.toList()); // Récupération de ce qui a été filtré et conversion du stream filtré en liste.
        try {
            objectOutputStream.writeObject(courses);
        } catch (IOException e) {
            System.err.print(e);
        }
    }

    /**
     * Récupère l'objet 'RegistrationForm' envoyé par le client et enregistre l'inscription dans le fichier inscription.txt.
     * La méthode gère les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     * Si l'inscription est réussie, un boolean true est envoyé au client.
     */
    public void handleRegistration() {
        try {
            // Flux d'entrée : réception du formulaire d'inscription avec les informations de l'étudiant(e).
            RegistrationForm registrationForm = (RegistrationForm) objectInputStream.readObject();

            // Enregistrement de l'inscription de l'étudiant(e) dans le fichier d'inscriptions.
            FileWriter fwInscription = new FileWriter("src/main/java/server/data/inscription.txt", true);
            BufferedWriter writer = new BufferedWriter(fwInscription);
            String inscription = registrationForm.getInscription();
            writer.append(inscription);
            writer.close();

            // Flux de sortie : envoi true au client pour attester l'enregistrement de l'inscription.
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

