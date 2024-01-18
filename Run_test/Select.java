package Requetes;

import java.util.Vector;
import Utile.Utile;
import classes.Relation;
public class Select {
    static  Vector<String> keys;
    static Vector<String> colonnes;
    Vector conditions;
    public Vector getConditions() {
        return conditions;
    }
    public void setConditions(Vector conditions) {
        this.conditions = conditions;
    }
    public void setKeys(Vector <String>keys) {
        this.keys = keys;
    }
    public Vector<String> getKeys() {
        return keys;
    }
    public Vector<String> getColonnes() {
        return colonnes;
    }
    public void setColonnes(Vector<String> colonnes) {
        this.colonnes = colonnes;
    }
    public Select (){
        Vector<String> liste = new Vector<>();
        liste.add("tous");        liste.add("les");
        liste.add("elements");         liste.add("dans");
        liste.add("table_name");
        setKeys(liste);
        Vector<String> table = new Vector<>();
        table.add("toutes");
        table.add("les");table.add("colonnes");
        setColonnes(table);
    }
    
    public Relation  select (String querry) throws Exception{
        querry =  Utile.checkEnd(querry); // verifie si la fin est un point
        String[] split = Utile.split(querry);       // split pour les mots cles de la requete et les conditions
        Vector<String> q = Utile.stringIntoWords(Utile.deletept(split[0])); //mot requete
        Relation r = new Relation(); r.setNom((String) q.lastElement());
        if (Utile.compare(q, getKeys())) {
            String[] colonnes = Utile.colonnes(split[1],this.getColonnes());
            r = r.getRelation(); // throw exception if not existe 
            if (colonnes != null) {
                // retourn seulement les colonnes demandes
                r = r.projection(colonnes);
                return r;
            }
            if (split.length == 3) {  
                Condition c = new Condition(split[2],   r);
                String[] conditions = Utile.condition(split[2]);
                if (conditions == null) {   // si il n y pas de 'ou' || 'et'
                    r = c.selection();
                }
                else{
                    Relation un = c.selection();
                    Relation deux = c.selection();
                    if (Utile.checkAndOr(split[2] ) == 1) { //union
                        r = un.union(deux, "resulat");
                    }
                    if (Utile.checkAndOr(split[2] ) == 2) { //intersection
                        r = un.intersection(deux, "resulat");
                    }
                }
            }
            
        }
        return r;
    }
}
