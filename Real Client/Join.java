package Requetes;
import java.util.Vector;
import Utile.Utile;
import classes.Relation;

public class Join {
    static Vector<String> colonnes;
    static  Vector<String> key;
    public static Vector<String> getColonnes() {
        return colonnes;
    }
    public static void setColonnes(Vector<String> colonnes) {
        Join.colonnes = colonnes;
    }

    public static Vector<String> getKey() {
        return key;
    }
    public static void setKey(Vector<String> key) {
        Join.key = key;
    }
    public Join (){
        Vector<String> key = new Vector<>();
        key.add("jointure"); 
        key.add("de"); key.add("table1"); 
        key.add("et"); key.add("table2"); 
        setKey(key);
        Vector<String> colonnes = new Vector<>();
        colonnes.add("toutes");
        colonnes.add("les");colonnes.add("colonnes");
        setColonnes(colonnes);
    }
    public Relation join (String querry) throws Exception{
        Relation result = null ;   querry = Utile.checkEnd(querry);
        try {
            String[] split = Utile.split(querry); // split avec  : 
            Vector<String> q = Utile.stringIntoWords(split[0]); //mot CLE DE LA REQUETE
            Utile.checkSyntax(q, getKey());     // check syntax
            Relation un = new Relation (); un.setNom(q.get(2));
            Relation deux = new Relation (); deux.setNom(q.get(4));
            if (!un.exist()) {
                throw new Exception("LA TABLE \""+ un.getNom().toUpperCase() +"\"  N' EXISTE PAS");
            }
            if (!deux.exist()) {
                throw new Exception("LA TABLE \""+ deux.getNom().toUpperCase() +"\"  N' EXISTE PAS");
            }        
            if (un.exist() && deux.exist()) {
                un = un.getRelation();    deux = deux.getRelation();
                Condition c = new Condition(split[1], un, deux);
                result = c.teta(deux);
                if (split.length == 3) {
                    String[] colonnes = Utile.colonnes(split[2], getColonnes());
                    if (colonnes != null ) {
                        result = result.projection(colonnes);
                    }
                    
                }
                result = result.union(result, "querry");
            }
        } catch (Exception e) {
            throw e;
        }
        return result; 
    }
}
