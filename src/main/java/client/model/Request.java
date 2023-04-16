package client.model;

import java.io.Serializable;

/**
 * La classe Request est la requête qui contient la commande et les arguments.
 * Request est utilisé par le client et peut être transmis au serveur par un flux.
 */
public class Request implements Serializable {
    private final Commande cmd;
    private final String arg;

    /**
     * Constructeur de la classe Request.
     * @param cmd La commande de la classe Commande pouvant être "INSCRIRE" ou "CHARGER".
     * @param arg Les arguments de la requête, soit un String vide, soit la session choisie, c'est-à-dire "hiver", "ete" ou "automne".
     */
    public Request(Commande cmd, String arg) {
        this.cmd = cmd;
        this.arg = arg;
    }

    /**
     * @return Le String de l'objet Commande.
     */
    @Override
    public String toString() {
        return cmd.name() + " " + arg;
    }
}