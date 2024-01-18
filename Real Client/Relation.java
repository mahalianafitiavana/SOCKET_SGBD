package classes;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import file.Writer;
public class Relation implements Serializable  {
    String nom;
    Vector<Attribut> attributs;
    Vector<Element> elements;
    
    public void setAttributs(Vector<Attribut> attributs) {
        this.attributs = attributs;
    }
    public Vector<Attribut> getAttributs() {
        return this.attributs;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getNom() {
        return this.nom;
    }
    public void setElements(Vector<Element> elements) {
        this.elements = elements;
    }
    public Vector<Element> getElements() {

        return this.elements;
    }
    public  Relation() {
        setAttributs(new Vector<Attribut>());
        setElements(new Vector<Element>());
    }
    /* produit cartésien  */
    public Relation cartesien (Relation r){
        Relation result  = new Relation();
        Vector <Attribut>   new_att =  this.getAttributs();
        new_att.addAll(r.getAttributs() );
        result.setAttributs(new_att);
        for (Element element1 : this.getElements()) {
            for (Element element2 : r.getElements()) {
                Element newElement = element1;  newElement.putAll(element2);
                result.getElements().add(newElement);
            }
        }
        return result;
    }
    // function qui verifier si les domaines de la relation est identique a ceux de l element
    public boolean compare (Element e){
        boolean t = true;
        for (int i = 0; i < e.getAttributs(null).size(); i++) {
            String class1 = (((Attribut) e.getAttributs(null).get(i)).getDomaine().getClass().toString());
            String class2 = (((Attribut) this.getAttributs().get(i)).getDomaine().getClass().toString());
            if (!class1.equals(class2)) {
                t = false;
                break;
            }
        }
        return t;
    }
    // compare class of both relation if they have the same instance
    public boolean compare (Relation r) throws Exception{
        boolean t = true;
        for (int i = 0; i < r.getAttributs().size(); i++) {
            String class1 = (((Attribut) r.getAttributs().get(i)).getDomaine().getClass().toString());
            String class2 = (((Attribut) this.getAttributs().get(i)).getDomaine().getClass().toString());
            if (!class1.equals(class2)) {
                t = false;
                throw new Exception (r.getAttributs().get(i).getNom()+" INCOMPATIBLE AVEC "+ this.getAttributs().get(i).getNom());
            }
        }
        return t;
    }
    public void addElement(Element e) throws Exception{
        if (!this.compare(e)) {
            Exception ex = new Exception("INSERTION INVALIDE");
            throw ex;
        }else{
            this.getElements().add(e);
        }
    }
    public Relation union (Relation r,String nom) throws Exception{
        Relation result = new Relation();
        result.setNom(nom);
        if (r.getAttributs().size() != getAttributs().size()) {
            Exception e = new Exception("NOMBRE DE COLONNES INCOHÉRENT");
            throw e;
        }
        if(!this.compare(r)){
            Exception e = new Exception("DOMAINES INCOMPATIBLES");
            throw e;
        }else{
            result.setAttributs(this.getAttributs());
            for (int i = 0; i < this.getElements().size(); i++) {
                if (!this.getElements().get(i).isIn(result)) {
                    result.getElements().add(this.getElements().get(i));
                }
            }
            for (int i = 0; i < r.getElements().size(); i++) {
                if (!r.getElements().get(i).isIn(result)) {
                    Element e_new = new Element(); e_new.setInit(attributs);
                    Vector values =  r.getElements().get(i).getValues(r.getElements().get(i).getInit());
                    for (int k = 0 ;k < result.getAttributs().size(); k++) {
                        e_new.put(result.getAttributs().get(k), values.get(k));
                    }
                    result.getElements().add(e_new); 
                }
            }
        }
        return result;
    }
    public Relation difference(Relation r,String nom) throws Exception{
        Relation result = new Relation();
        result.setNom(nom); 
        Relation union = this.union(r, nom);
        Relation intersection = this.intersection(r, nom);
        result.setAttributs(this.getAttributs());
        Vector union_elements = union.getElements();
        for (int i = 0; i < union_elements.size(); i++) {
            Element e = (Element) union_elements.get(i);
            if (!e.isIn(intersection) ) {
                result.getElements().add(e);
            }
        }
        return result.union(result, nom);
    }
    public Relation intersection(Relation r,String nom) throws Exception{
        Relation result = new Relation();
        result.setNom(nom); 
        if (r.getAttributs().size() != getAttributs().size()) {
            Exception e = new Exception("NOMBRE DE COLONNES INCOHÉRENT");
            throw e;
        }
        if(!this.compare(r)){
            Exception e = new Exception("DOMAINES INCOMPATIBLES");
            throw e;
        }
        if(this.compare(r)){
            result.setAttributs(this.getAttributs());
            Vector this_elements = this.getElements();
            for (int i = 0; i < this_elements.size(); i++) {
                Element e = (Element) this_elements.get(i);
                if (e.isIn(r) ) {
                    result.getElements().add(e);
                }
            }
        }       
        return result.union(result, nom);
    }
    // Affiche les valeurs avec les noms d'attributs comme colonnes
    public void display () {
        Vector<Element> data = getElements();
        if (data.size() == 0) {
            System.out.println("0 element");
            return;
        }
        String[] columns = new String[this.getAttributs().size()];
        Vector att = this.getAttributs();
        for (int i = 0; i < columns.length; i++) {
            columns[i] = ((Attribut) att.get(i)).getNom();
        }
        int columnWidth = 15;
        String horizontalLine = "+";
        for (int i = 0; i < columns.length; i++) {
            horizontalLine += "-".repeat(columnWidth + 2) + "+";
        }
        System.out.println(horizontalLine);
        // Affiche les en-têtes des colonnes
        System.out.print("|");
        for (String column : columns) {
            System.out.printf(" %-"+columnWidth+"s |", column);
        }
        System.out.println("\n" + horizontalLine);
        // Affiche les données
        for (Element element : data) {
            System.out.print("|");
            Object value = null;
            for (int i = 0; i < att.size(); i++) {
                value = element.get(att.get(i));
                if (value instanceof java.util.Date) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    System.out.printf(" %"+columnWidth+"s |", format.format(value));
                }else{
                    System.out.printf(" %"+columnWidth+"s |", value);
                }
            }
            System.out.println("\n" + horizontalLine);
        }
        if (data.size() == 1) {
            System.out.println("1 ligne");
        }
        if (1 < data.size() ) {
            System.out.println(data.size()+" lignes");
        }
    }
    //check si la relation existe deja
    public  boolean exist() throws Exception{
        String nom = this.getNom().toLowerCase();
        File file = new File("src/file/relation/"+nom+".dat");
        boolean b = false;
        if (file.exists()) {
            b = true;
        }
        return b;
    }
    public Vector<String> getColonne (){
        Vector<String> liste = new Vector<>();
        Vector<Attribut> attributs = this.getAttributs();
        for (Object attribut : attributs) {
            Attribut a = (Attribut) attribut;
            liste.add(a.getNom().toLowerCase());
        }
        return liste;
    }
    public void isrealColonneOf (String[] inputs) throws Exception{
        Vector<String> colonne = this.getColonne();
        for (int i = 0; i < inputs.length; i++) {
            if(!colonne.contains(inputs[i].toLowerCase())){
                throw new Exception("LA COLONNE '"+inputs[i]+"' N EXISTE PAS ");
            }
        }
    }
    public Relation getRelation () throws Exception {
        Relation relations;
        try {
            relations = Writer.read("src/file/relation/"+this.getNom().toLowerCase());
        } catch (InvocationTargetException e) {
            throw new Exception("LA RELATION '"+this.getNom().toUpperCase()+"'' N' EXISTE PAS ");
        }
        return relations;
    }
    public Relation projection (String[] colonnes) throws Exception{
        Relation result = new Relation();
        Vector  old_attributs = this.getColonne();          //colonne original
        Vector <Attribut> new_attributs = new Vector<>();     //nouvelle colonne
        for (int j =  0; j < colonnes.length ; j ++) {
            if (old_attributs.contains(colonnes[j].toLowerCase())) {
                // ajouter les nouvelles colonnes dans la nouvelle si le nom correspond
                new_attributs.add(this.getAttributs().get(old_attributs.indexOf(colonnes[j].toLowerCase())));
            }    
        }
        result.setAttributs(new_attributs);
        // verifier si les colonnes demande appartient a resultat
        result.isrealColonneOf(colonnes) ; //verfier si les colonnes ne sont pas dupliquées
        result.setNom(this.getNom());
        Vector <Element> old_elements = this.getElements();
        Vector <Element> new_elements = new Vector<>();
        for (Element element : old_elements) {
            Element e = new Element();
            for (int j =  0; j < colonnes.length ; j ++) {
                if (old_attributs.contains(colonnes[j].toLowerCase())) {
                    e.put(this.getAttributs().get(old_attributs.indexOf(colonnes[j].toLowerCase())), element.get(this.getAttributs().get(old_attributs.indexOf(colonnes[j].toLowerCase()))));
                }
            }
            new_elements.add(e);
        }
        result.setElements(new_elements);
        return result;
    }
    
}
