package client.cli;

import server.models.Course;
import server.models.RegistrationForm;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliLauncher {
    public final static int PORT = 1337;
    private final static Scanner keyboard = new Scanner(System.in);
    public static void main(String[] args) {

        Client client;
        try {
            Socket socket = new Socket("localhost", PORT);
            client = new Client(socket);
            client.run();

            System.out.println("Client is running...");

            System.out.println("*** Bienvenue au portail d'inscription du cours de l'UDEM ***");

            ArrayList<Course> courses = doLoadCourses(client);
            for (int i = 0; i < courses.size(); i++) {
                System.out.println((i + 1) + ". " + courses.get(i).getCode() + "\t" + courses.get(i).getName());
            }
            getOperationFromUser(client, courses);

            client.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void getOperationFromUser(Client client, ArrayList<Course> coursesForRegistration) {
        System.out.println("> Choix: ");
        System.out.println("1. Consulter les cours offerts pour une autre session");
        System.out.println("2. Inscription à un cours");
        System.out.print("> Choix: ");
        int choix = keyboard.nextInt();
        switch (choix) {
            case 1 -> {
                ArrayList<Course> courses = doLoadCourses(client);
                for (int i = 0; i < courses.size(); i++) {
                    System.out.println((i + 1) + ". " + courses.get(i).getCode() + "\t" + courses.get(i).getName());
                }
                getOperationFromUser(client, courses);
            }
            case 2 -> {
                doRegister(client,coursesForRegistration );

            }
            default -> {
                System.out.print(choix + " n'est pas un des choix acceptés\nVeuillez choisir\n1 ou 2\n> Choix: ");
                while (!keyboard.hasNextInt()) {
                    System.out.print(keyboard.nextLine() + " n'est pas un des choix acceptés\nVeuillez choisir\n1 ou 2\n> Choix: ");
                }
            }

        }

    }


    private static void doRegister(Client client, ArrayList<Course> courses) {
        System.out.print("Veuillez saisir votre prénom: ");
        String prenom = keyboard.next();
        System.out.print("Veuillez saisir votre nom: ");
        String nom = keyboard.next();
        System.out.print("Veuillez saisir votre email: ");
        String email = keyboard.next();
        System.out.print("Veuillez saisir votre matricule: ");
        String matricule = keyboard.next();
        System.out.print("Veuillez saisir le code du cours: ");
        String codeCours = keyboard.next().toUpperCase();

        boolean isSuccesfullyRegistered = client.registerStudent(prenom, nom, email, matricule, codeCours, courses);
        if (isSuccesfullyRegistered) {
            System.out.println("Félicitations! Inscription réussie de " + prenom + "au cours " + codeCours + ".");
        } else {
            System.out.println("Le cours est invalide ou n'est pas disponible pour la session choisie.");
        }

    }

    /**
     * Affiche la liste de cours disponibles pour une session donnée.
     * Cette session est un choix de l'utilisateur.
     *
     * @param client Client connecté au serveur. Je mens peut-être... à voir.
     */
    private static ArrayList<Course> doLoadCourses(Client client) {
        System.out.print("Veuillez choisir la session pour laquelle vous voulez consulter la liste de cours:" +
                "\n1. Automne\n2. Hiver\n3. Ete\n> Choix: ");
        while (!keyboard.hasNextInt()) {
            System.out.print(keyboard.nextLine() + " n'est pas un des choix acceptés.\nVeuillez choisir parmi les choix 1, 2 ou 3.\n> Choix: ");
        }
        int choix = keyboard.nextInt();
        ArrayList<Course> courses = client.getCourseSession(getSessionFromUser(choix));
        return courses;
    }


    /**
     * @param choix Choix de session de l'utilisateur, c'est-à-dire 1, 2 ou 3.
     * @return Retourne en String le choix de session (automne, hiver ou été) correspondant au choix de l'utilisateur.
     */
    private static String getSessionFromUser(int choix) {
        String messageCoursSession = "Les cours offerts pendant la session %s sont :%n";
        switch (choix) {
            case 1 -> {
                System.out.printf(messageCoursSession, "d'automne");
                return "automne";
            }
            case 2 -> {
                System.out.printf(messageCoursSession, "d'hiver");
                return "hiver";
            }
            case 3 -> {
                System.out.printf(messageCoursSession, "d'été");
                return "été";
            }
            default -> {
                System.out.print(choix + " n'est pas un des choix acceptés\nVeuillez choisir parmi les choix 1, 2 ou 3.\n> Choix: ");
                while (!keyboard.hasNextInt()) {
                    System.out.println(keyboard.nextLine() + " n'est pas un des choix acceptés\nVeuillez choisir parmi les choix 1, 2 ou 3.\n> Choix: ");
                }
                return getSessionFromUser(keyboard.nextInt());
            }

        }
    }

}
