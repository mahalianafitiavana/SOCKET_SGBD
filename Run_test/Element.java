package classes;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import Utile.Utile;
public class Element extends HashMap<Attribut,Object>   implements Serializable{
    Vector<Attribut>  init;    
    public Vector<Attribut>  getInit() {
        return init;
    }
    public void setInit(Vector<Attribut>  init) {
        this.init = init;
        setAttributs(init);
    }
    /* if null {suffle } else {par ordre d attribut donnes} */
    public Vector<Object> getValues (Vector<Attribut> attributs ){
        Vector<Object> v = new Vector<>();
        if (attributs == null) {
            for (Attribut attribut : this.getInit()) {
                v.add(this.get(attribut));
            }
        }else{
            for (Attribut attribut  : attributs) {
                v.add(this.get(attribut));
            }
        }
        return v;
    }
    public Vector getAttributs (String nom){
        Vector liste = null ;
        if (nom == null) {
            //vector attributs
            liste = new Vector<>(this.keySet());
            return liste;
        }else{
            // vector String
            for (Attribut a : this.keySet()) {
                liste.add(a.getNom().trim().toUpperCase());
            }
        }
        return liste;
    }
    public void setAttributs (Vector<Attribut> attribus){
        for (Attribut attribut : attribus) {
            this.put(attribut, "NULL");
        }
    }
                      // pour la comparaison // pour l' ordre des attributs
    public boolean isIn (Relation r){
        Vector<Element> elements = r.getElements();
        boolean t = false;
        for (Element element : elements) {
            if (element.getValues(r.getAttributs()).equals(this.getValues(this.getInit()))) {
                t = true ;
            }
        }
        return t;
    } 
    public void setValues(String querry, Relation r) throws Exception{
            //nom colonne //value
        HashMap<String, String> keyValuePairs = Utile.getKeyValue(querry);
        Collection<String> valuesCollection = keyValuePairs.keySet();
        // Convertir la Collection en tableau de String
        String[] colonnes  = valuesCollection.toArray(new String[0]);
        r.isrealColonneOf(colonnes);    // tester si les colonnes demadés sont réel
        for (Attribut a : this.getInit()) {
            String value = keyValuePairs.get(a.getNom().toUpperCase()); // valeur a inserer dans la colonne
            if (value != null) {
                String c = a.getDomaine().getClasses().trim();
                try {
                    Class maclass = Class.forName(c);
                    Object object = null;
                    if (c.equalsIgnoreCase("java.util.Date")) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = format.parse(value);
                        object = d;
                    }else{
                        object = maclass.getConstructor(String.class).newInstance(value);
                    }
                    this.put(a, object);
                }catch (InvocationTargetException e){
                    throw new Exception("INSERTION INVALIDE: \""+value+"\" en tant que "+ a.getDomaine().getNom());
                }
            }
        }
    }
}
