package client.model;

import java.io.Serializable;

public class Request implements Serializable {
    private final Commande cmd;
    private final String arg;

    public Request(Commande cmd, String arg) {
        this.cmd = cmd;
        this.arg = arg;
    }

    @Override
    public String toString() {
        return cmd.name() + " " + arg;
    }
}