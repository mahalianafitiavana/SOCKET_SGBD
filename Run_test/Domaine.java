package classes;
import java.io.Serializable;
import java.util.Vector;
import file.*;
public class Domaine implements Serializable {
    String classes;
    String nom;
    public String getClasses() {
        return classes;
    }
    public void setClasses(String classes) {
        this.classes = classes;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getNom() {
        return nom;
    }  
    public static Domaine getDomaine (String domaine_name) throws Exception{
        Domaine d = new Domaine();
        Vector domaines = Writer.readObject("file/domaine");
        for (Object  domaine: domaines) {
            Domaine a = (Domaine) domaine;
            if (a.getNom().equalsIgnoreCase(domaine_name)) {
                d = a;
                break;
            }
        }
        if (d.getClass() == null || d.getNom() == null) {
            throw new Exception("TYPE \""+domaine_name.toUpperCase()+"\" NON EXCISTANT");
        }
        return d;
    }
}
