package server;

/**
 * La classe ServerLauncher crée une instance de Server et l'implémente.
 */

public class ServerLauncher {
    /**
     * Le port est égal à 1337.
     */
    public final static int PORT = 1337;

    /**
     * Cette méthode crée une instance server qui écoute sur le port = 1337 et affiche un message de confirmation après le démarrage.
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}