package classes;

import java.io.Serializable;
import java.util.Vector;

public class Attribut implements Serializable {
    String nom;
    Domaine domaine;
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Domaine getDomaine() {
        return domaine;
    }
    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }
    public Attribut (String nom,Domaine domaine){
        setDomaine(domaine);setNom(nom);
    }
    public Attribut(){}

    public static Attribut get (String attribu_name,Vector<Attribut> attribus){
        Attribut a = new Attribut();
        for (Attribut attribut : attribus) {
            if (attribut.getNom().equalsIgnoreCase(attribu_name)) {
                a = attribut;
                break;
            }
        }
        return a;
    }
}
