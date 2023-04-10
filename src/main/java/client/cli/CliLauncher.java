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

            System.out.println("*** Bienvenue au portail d'inscription du cours de l'UDEM ***" + "" +
                    "\nVeuillez choisir la session pour laquelle vous voulez consulter la liste de cours:" +
                    "\n1. Automne\n2. Hiver\n3. Ete" + "\n> Choix : ");

            doLoadCourses(client);

            System.out.println("1. Consulter les cours offerts pour une autre session");
            System.out.println("2. Inscription à un cours");
            System.out.print("> Choix:");

            client.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static String getOperationFromUser(int choix) {

    }

    private static void doRegister(Client client) {
        System.out.print("prénom:");
        String prenom = keyboard.nextLine();
        System.out.print("nom:");
        String nom = keyboard.nextLine();
        System.out.print("email:");
        String email = keyboard.nextLine();
        System.out.print("matricule:");
        String matricule = keyboard.nextLine();
        System.out.print("code du cours:");
        String codeCours = keyboard.nextLine();
        boolean isSuccesfullyRegistered = client.registerStudent(prenom, nom, email, matricule, codeCours);


    }

    /**
     * Affiche la liste de cours disponibles pour une session donnée.
     * Cette session est un choix de l'utilisateur.
     * @param client
     */
    private static void doLoadCourses(Client client) {
        while (!keyboard.hasNextInt()) {
            System.out.println(keyboard.nextLine() + " n'est pas un des choix acceptés\nVeuillez choisir 1, 2 ou 3.");
        }
        int choix = keyboard.nextInt();
        ArrayList<Course> courses = client.getCourseSession(getSessionFromUser(choix));
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getCode() + "\t" + courses.get(i).getName());
        }
    }

    /**
     *
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
                System.out.println(choix + " n'est pas un des choix acceptés\nVeuillez choisir 1, 2 ou 3.");
                while (!keyboard.hasNextInt()) {
                    System.out.println(keyboard.nextLine() + " n'est pas un des choix acceptés\nVeuillez choisir 1, 2 ou 3.");
                }
                return getSessionFromUser(keyboard.nextInt());
            }

        }
    }

}
