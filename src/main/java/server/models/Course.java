package server.models;

import java.io.Serializable;

/**
 * Modèle d'un cours, plus précisément les caractéristiques définissant un cours.
 */

public class Course implements Serializable {

    private String name;
    private String code;
    private String session;

    /**
     * Constructeur d'un cours.
     * @param name le nom du cours
     * @param code le code de cours
     * @param session la session durant laquelle le cours a lieu
     */

    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}
