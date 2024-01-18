package Requetes;

import java.util.HashMap;
import java.util.Vector;
import Utile.*;
import classes.*;
import file.*;

public class Create {
    Vector<String> keys;

    public Vector<String> getKeys() {
        return keys;
    }
    public void setKeys(Vector<String> keys) {
        this.keys = keys;
    }
    public Create (){
        Vector<String> keys = new Vector<>();
        keys.add("creer");keys.add("la");keys.add("table"); keys.add("table_name");
        setKeys(keys);
    }  
    public String create (String querry) throws Exception{
        querry =  Utile.checkEnd(querry);
        String[] split = Utile.split(querry); // split avec  : 
        Relation r = new Relation();
        Vector<String> q = Utile.stringIntoWords(split[0]); //mot CLE DE LA REQUETE
        r.setNom(((String) q.lastElement()).toUpperCase());
        if (Utile.compare(q, getKeys())) {  // VERIFIER QUE LES MOTS CLES SONT CORRECTS
            if (!r.exist()) {           // VERFIIER SI AU CAS OU LA TABLE EXISTE DEJA
                setAttribute(split[1], r);
            }else{
                throw new Exception("CETTE TABLE EXISTE DEJA");
            }
        }
        String s = "TABLE CRÉÉE";
        return s;
    }
    public void setAttribute (String  querry,Relation r) throws Exception{
        try {
            HashMap<String, String> map = Utile.getKeyValue(querry);
            Vector<Attribut> attributs = new Vector<>(); 
            for (String key : map.keySet()) {
                Attribut a = new Attribut();
                a.setNom(key);
                a.setDomaine(Domaine.getDomaine(map.get(key)));
                attributs.add(a);
            } 
            r.setAttributs(attributs); 
            Writer.writeObject("file/relation/"+r.getNom().toLowerCase(), r);
        } catch (Exception e) {
            throw e;
        }
    }
}
