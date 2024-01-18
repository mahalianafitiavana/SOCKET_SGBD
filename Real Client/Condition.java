package Requetes;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import Utile.Utile;
import classes.*;
public class Condition {
    String operation;
    Object values;      /*  anle attribut  */
    Attribut attribut; /* anle relation */
    Relation relation;
    public Attribut getAttribut() {
        return attribut;
    }
    public void setAttribut(Attribut attribut) {
        this.attribut = attribut;
    }
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public Object getValues() {
        return values;
    }
    public void setValues(Object values) {
        this.values = values;
    }
   
    public Relation getRelation() {
        return relation;
    }
    public void setRelation(Relation relation) {
        this.relation = relation;
    }
    public Condition (String condition,Relation r) throws Exception{
        String[] split = condition.trim().split("\\s+");
        String[] i = new String[1] ; i[0] = split[0];
        setRelation(r); 
        r.isrealColonneOf(i);             // throw exception if colonne does not existe
        Attribut a = r.getAttributs().get(r.getColonne().indexOf(split[0].toLowerCase()));    ///indice 0 no misy ny colonne
        System.out.println(r.getColonne().indexOf(split[0].toLowerCase()));
        
        setAttribut(a);
        Object o;
        setOperation(split[1]); 
        /* valeur = colonne (ex :nom = nom) */
        if (split[2].charAt(0) != '\'' && split[2].charAt(split[2].length() -1) != '\'' )  {
            String[] c = new String[1]; c[0] = split[2]; 
            getRelation().isrealColonneOf(c); 
            setValues(getRelation().getAttributs().get(r.getColonne().indexOf(c[0].toLowerCase())));
        }
        /* simple valeur */
        if (split[2].charAt(0) == '\'' && split[2].charAt(split[2].length() -1) == '\'' )  {
            split[2] = split[2].substring(1, split[2].length() - 1);
            try {
                Class maClass = Class.forName(a.getDomaine().getClasses().trim());
                if (a.getDomaine().getClasses().equalsIgnoreCase("java.util.Date")) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = format.parse(split[2]);
                    o = d;
                }else{
                    o = maClass.getConstructor(String.class).newInstance(split[2]);
                }
                if ((o instanceof java.lang.String) && !getOperation().equals("=") ) {
                    throw new Exception("OPERATION \"" + getOperation() + "\" INVALIDE AVEC LA COLONNE " + getAttribut().getNom());
                }
                setValues(o);
            }
            catch (InvocationTargetException e){
                throw new Exception("INSERTION INVALIDE: \""+split[2]+"\" en tant que "+ a.getDomaine().getNom());
            }
        }
    }
    public Condition (String condition,Relation un ,Relation deux) throws Exception{
        Vector <String> split = Utile.stringIntoWords(condition);
        setRelation(un);
        String[] att_1 = new String[1] ; att_1[0] = split.firstElement();

        String[] att_2 = new String[1] ; att_2[0] = split.lastElement();
        un.isrealColonneOf(att_1);             // verifie si la colonne demande existe vraiment
        deux.isrealColonneOf(att_2);             // verifie si la colonne demande existe vraiment        
        Attribut a = un.getAttributs().get(un.getColonne().indexOf(att_1[0].toLowerCase()));    ///indice 0 no misy ny colonne
        setAttribut(a);
        Object o = deux.getAttributs().get(deux.getColonne().indexOf(att_2[0].toLowerCase()));
        setValues(o);
        setOperation(split.get(1));
    }
    /* teta jointure  colonne1 compares to colonnes2  */
    public Relation teta (Relation deux) throws Exception{
        Relation un = this.getRelation();
        Relation resultat = new Relation();
        resultat.setAttributs(un.getAttributs());
        resultat.getAttributs().addAll(deux.getAttributs());
        Attribut att_1 = this.getAttribut();/* attribut dans la relation un */
        Attribut att_2 = (Attribut) this.getValues();/* attribut dans la relation deux */
        for (Element element1 : un.getElements()) {
            for (Element element2 : deux.getElements()) {
                Comparable<Object> attributeValue = (Comparable) element1.get(att_1);
                Comparable<Object> conditionValue  = (Comparable) element2.get(att_2);
                    int i = attributeValue.compareTo(conditionValue);
                    if ( i == 0 && (getOperation().equals(">=") || getOperation().equals("<=") || getOperation().equals("=") )) {
                        Element newElement = new Element(); newElement.setInit(resultat.getAttributs());
                        newElement.putAll(element1);
                        newElement.putAll(element2);// Ajouter les valeurs de la deuxième relation                
                        resultat.getElements().add(newElement);
                    }
                    if ( i == 1 && (getOperation().equals(">") || getOperation().equals(">=") ) ) {
                        Element newElement = new Element();newElement.setInit(resultat.getAttributs());
                        newElement.putAll(element1);
                        newElement.putAll(element2);// Ajouter les valeurs de la deuxième relation                
                        resultat.getElements().add(newElement);
                    }     
                    if ( i == -1 && (getOperation().equals("<") || getOperation().equals("<=") )) {
                        Element newElement = new Element();newElement.setInit(resultat.getAttributs()); 
                        newElement.putAll(element1);
                        newElement.putAll(element2);// Ajouter les valeurs de la deuxième relation                
                        resultat.getElements().add(newElement);
                    } 
                    if ( i != 0 && getOperation().equals("<>")) {
                        Element newElement = new Element();newElement.setInit(resultat.getAttributs());
                        newElement.putAll(element1);
                        newElement.putAll(element2);// Ajouter les valeurs de la deuxième relation                
                        resultat.getElements().add(newElement);
                    }
                }   
            }
        return resultat;
    }
    /* selection colone compares to value  */
    public Relation selection () throws Exception{
        Relation r = this.getRelation();
        Relation resultat = new Relation();
        resultat.setAttributs(r.getAttributs());
        resultat.setNom(r.getNom());
        Vector<Element> elements = r.getElements();
        for (Element element : elements) {
            Comparable<Object> attributeValue = (Comparable) element.get(getAttribut());
            Comparable<Object> conditionValue   = null;
            if (!(getValues() instanceof Attribut)) {
                conditionValue = (Comparable) getValues();
            }
            else{
                conditionValue = (Comparable) element.get( (Attribut)  getValues());
            }
                int i = attributeValue.compareTo(conditionValue);
                if ( i == 0 && (getOperation().equals(">=") || getOperation().equals("<=") || getOperation().equals("=") )) {
                    resultat.getElements().add(element); 
                }
                if ( i == 1 && (getOperation().equals(">") || getOperation().equals(">=") ) ) {
                    resultat.getElements().add(element);
                }     
                if ( i == -1 && (getOperation().equals("<") || getOperation().equals("<=") )) {
                    resultat.getElements().add(element);
                } 
                if ( i != 0 && getOperation().equals("<>")) {
                    resultat.getElements().add(element);
                }
            }
        return resultat;
    }

}
