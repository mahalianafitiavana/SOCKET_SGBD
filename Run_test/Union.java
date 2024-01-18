package Requetes;
import java.util.Vector;
import Utile.*;
import classes.*;

public class Union {
    static Vector<String> key = new Vector<>();
    static Vector<String> colonnes = new Vector<>();
    public static Vector<String> getColonnes() {
        return colonnes;
    }
    public static void setColonnes(Vector<String> colonnes) {
        Union.colonnes = colonnes;
    }
    public static Vector<String> getKey() {
        return key;
    }
    public static void setKey(Vector<String> key) {
        Join.key = key;
    }
    public Union (){
        Vector<String> key = new Vector<>();
        key.add("union"); 
        key.add("de"); key.add("table1"); 
        key.add("et"); key.add("table2"); 
        setKey(key);
        Vector<String> colonnes = new Vector<>();
        colonnes.add("toutes");
        colonnes.add("les");colonnes.add("colonnes");
        setColonnes(colonnes);
    }
    public Relation union (String sql) throws Exception{
        Relation r ;
        sql =  Utile.checkEnd(sql); // verifie si la fin est un point
        try {
            String[] split = Utile.split(sql);
            Vector<String> mots = Utile.stringIntoWords(split[0]);
            Utile.checkSyntax(mots, Union.getKey());                 //verifier syntax
            Relation un = new Relation(); un.setNom(mots.get(2)); un = un.getRelation();  // verifier en meme temps si la relation existe vraiment
            Relation deux = new Relation(); deux.setNom(mots.get(4)); deux = deux.getRelation();    // verifier en meme temps si la relation existe vraiment            
            r = un.union(deux, "sql");   // union table1 et table2 : tous les colonnes ;
            String[] colonnes = Utile.colonnes(split[1],getColonnes()); /* jerena we misy condition ve sa tsia if (true) { get do projection } */
            if (colonnes != null) {
                r = r.projection(colonnes);
            }           
        } catch (Exception e) {
            throw e;
        }
        return r;
    }

}
