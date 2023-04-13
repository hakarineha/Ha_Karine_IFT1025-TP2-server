package client.view.cli;

import client.controller.Client;
import server.models.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliLauncher {
    public final static int PORT = 1337;
    private final static Scanner keyboard = new Scanner(System.in);
    private static List<Course> coursesForRegistration;

    public static void main(String[] args) {

        Client client;
        try {
            client = new Client(PORT);
//            client.run();

            System.out.println("Client is running...");

            System.out.println("*** Bienvenue au portail d'inscription du cours de l'UDEM ***");

            coursesForRegistration = doLoadCourses(client);
            for (int i = 0; i < coursesForRegistration.size(); i++) {
                System.out.println((i + 1) + ". " + coursesForRegistration.get(i).getCode() + "\t" + coursesForRegistration.get(i).getName());
            }
            getOperationFromUser(client);

            client.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void getOperationFromUser(Client client) {
        askOperationFromUser();

        while (!keyboard.hasNextInt()) {
            System.out.print(keyboard.nextLine() + " n'est pas un des choix acceptés.\nVeuillez choisir 1 ou 2.\n");
            getOperationFromUser(client);
        }

        int choix = keyboard.nextInt();

        switch (choix) {
            case 1 -> {
                coursesForRegistration = doLoadCourses(client);
                for (int i = 0; i < coursesForRegistration.size(); i++) {
                    System.out.println((i + 1) + ". " + coursesForRegistration.get(i).getCode() + "\t" + coursesForRegistration.get(i).getName());
                }
                getOperationFromUser(client);
            }
            case 2 -> {
                doRegister(client);

            }
            default -> {
                System.out.print(choix + " n'est pas un des choix acceptés.\nVeuillez choisir 1 ou 2.\n");
//                while (!keyboard.hasNextLine()) {
//                    System.out.print(keyboard.nextLine() + " n'est pas un des choix acceptés.\nVeuillez choisir 1 ou 2.\n> Choix: ");
//                }
                getOperationFromUser(client);
            }

        }

    }

    private static void askOperationFromUser() {
        System.out.println("> Choix: ");
        System.out.println("1. Consulter les cours offerts pour une autre session");
        System.out.println("2. Inscription à un cours");
        System.out.print("> Choix: ");
    }

    private static String askEmail(Client client) {
        System.out.print("Veuillez saisir votre email: ");
        String email = keyboard.next(); //f@jj.com
        while (!client.isEmailValid(email)) { //valide
            System.out.println("Email invalide, essayez à nouveau.");
            email = askEmail(client);
        }
        return email;
    }

    private static String askMatricule(Client client) {
        System.out.print("Veuillez saisir votre matricule: ");
        String matricule = keyboard.next();
        while (!client.isMatriculeValid(matricule)) {
            System.out.println("Votre matricule doit contenir 8 chiffres.");
            matricule = askMatricule(client);
        }
        return matricule;
    }

    private static String askCodeCours(Client client) {
        System.out.print("Veuillez saisir votre code de cours: ");
        String codeCours = keyboard.next().toUpperCase();
        while (!client.isCodeCoursValid(codeCours, coursesForRegistration)) { //valide
            System.out.println("Code de cours invalide, essayez à nouveau.");
            codeCours = askCodeCours(client);
        }
        return codeCours;
    }

    private static void doRegister(Client client) {
        System.out.print("Veuillez saisir votre prénom: ");
        String prenom = keyboard.next();
        System.out.print("Veuillez saisir votre nom: ");
        String nom = keyboard.next();
        String email = askEmail(client);
        String matricule = askMatricule(client);
        String codeCours = askCodeCours(client);
        boolean isSuccesfullyRegistered = client.registerStudent(prenom, nom, email, matricule, codeCours, coursesForRegistration);
        if (isSuccesfullyRegistered) {
            System.out.println("Félicitations! Inscription réussie de " + prenom + " au cours " + codeCours + ".");
        } else {
            System.out.println("L'inscription a échoué.'");
        }

    }

    /**
     * Affiche la liste de cours disponibles pour une session donnée.
     * Cette session est un choix de l'utilisateur.
     *
     * @param client Client connecté au serveur. Je mens peut-être... à voir.
     */
    private static ArrayList<Course> doLoadCourses(Client client) {
        System.out.print("Veuillez choisir la session pour laquelle vous voulez consulter la liste de cours:" + "\n1. Automne\n2. Hiver\n3. Ete\n> Choix: ");
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
                return "ete";
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
