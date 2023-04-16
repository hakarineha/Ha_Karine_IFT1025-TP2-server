package server.models;

import java.io.Serializable;

/**
 * Le modèle d'un formulaire d'inscription à un cours.
 */

public class RegistrationForm implements Serializable {
    private String prenom;
    private String nom;
    private String email;
    private String matricule;
    private Course course;

    /**
     * Constructeur du formulaire d'inscription.
     * @param prenom Le prenom de l'étudiant qui s'inscrit.
     * @param nom Le nom de l'étudiant qui s'inscrit.
     * @param email Le email de l'étudiant qui s'inscrit.
     * @param matricule La matricule de 8 chiffres de l'étudiant qui s'inscrit.
     * @param course Le cours auquel l'étudiant désire d'inscrire.
     */

    public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.course = course;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    public String getInscription(){
        return getCourse().getSession() + "\t" + getCourse().getCode()
                + "\t" + getMatricule() + "\t" + getPrenom()
                + "\t" + getNom() + "\t" + getEmail() + "\n";
    }

    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}
