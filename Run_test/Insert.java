package Requetes;
import java.util.Vector;
import Utile.*;
import classes.*;
import file.*;

public class Insert {
    static Vector<String> keys ;
    public static Vector<String> getKeys() {
        return keys;
    }
    public static void setKeys(Vector<String> keys) {
        Insert.keys = keys;
    }
    public Insert (){
        Vector<String> k = new Vector<>();
        k.add("inserer");
        k.add("un");        k.add("element");
        k.add("dans");      
        k.add("table_name");
        setKeys(k);
    }
    public String insert (String querry) throws Exception{
        querry =  Utile.checkEnd(querry);
        String[] split = Utile.split(querry);
        Vector<String> q = Utile.stringIntoWords(split[0]); //mot requete
        Relation r = new Relation();
        r.setNom((String) q.lastElement());
        String retour = null;
        if (Utile.compare(q, getKeys())) {
            r = r.getRelation(); // thrw exception if la relation n existe pas 
            retour = inserts(split[1],r);
              
        }
        return retour;
    }
    public String inserts (String querry,Relation  relation ) throws Exception{
        Element e = new Element();
        // setter le attribut du nouvel element
        e.setInit(relation.getAttributs());
       // // set the values of the element
        e.setValues(Utile.deletept(querry),relation);
       // // ajouter le nouvelle element dans la relation */
        relation.getElements().add(e);
       // // remplacer la relation dans le ficher par la relation avec l element additionnel
        Writer.writeObject("file/relation/"+relation.getNom().toLowerCase(), relation);
        String succes ="insertion avec succès";   
        return succes;
    }
}
