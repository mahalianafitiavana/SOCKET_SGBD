package Utile;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Requetes.*;
import classes.*;

public class Utile {
    public static Object doIt (String querry) throws Exception{
        Object o = null; Relation r;
        try {
            String b = querry.trim().substring(0,1);
            if (b.equalsIgnoreCase("c") ) {
                Create c = new Create();
                o = c.create(querry);
            }
            if (b.equalsIgnoreCase("i")) {
                Insert i = new Insert();
                o =  i.insert(querry);
            }
            if (b.equalsIgnoreCase("t")) {
                Select  select = new Select();
                r =   select.select(querry);
                o = r;
            }
            if (b.equalsIgnoreCase("j")) {
                Join j = new Join();
                o = j.join(querry);
            }
            if (b.equalsIgnoreCase("u")) {
                Union u = new Union();
                o = u.union(querry);
            }   
          
        } catch (Exception e) {
             throw e;
        }
        return o;
    }
    // verifier si les conditions 
    public static int checkAndOr  (String condition){
        int i = 0;
        String ou = "ou";
        String et = "et";
        Pattern ou_pattern = Pattern.compile("\\b" + ou +"\\b", Pattern.CASE_INSENSITIVE);
        Pattern et_pattern = Pattern.compile("\\b" + et +"\\b", Pattern.CASE_INSENSITIVE);
        Matcher ou_matcher = ou_pattern.matcher(condition);        
        Matcher et_matcher = et_pattern.matcher(condition);        
        if (ou_matcher.find()) {
            i = 1;
        }
        if (et_matcher.find()) {
            i = 2;
        }
        return i;
    }
    public static  String[] condition (String condition){
        // verfier l operation a faire si 0 aucune si 1 union si 2 intersection
        int i = checkAndOr(condition);
        String[] split = null;
        if (i == 1) {
            split = condition.split("(?i)ou");
        }
        if (i == 2) {
            split = condition.split("(?i)et");
        }
        return split;
    }
    
    // deleteb the commat at the end of the sentence
    public static String deletept (String querry){
        querry = querry.trim();
        if (querry.endsWith(".")) {
            return querry.substring(0,querry.length()-1);
        }
        return querry;
    }
    public static String checkEnd (String querry) throws Exception {
        if (!querry.trim().endsWith(".")) {
            throw new Exception("REQUÊTE MAL TERMINÉE!!!");
        }else{
            return deletept(querry);
        }
    }
    /* cle valeur pour les insertion et creation de table ex: nom // mahaliana || nom //texte */
    public static HashMap<String, String>  getKeyValue (String input) throws Exception {
        input = input.trim();
        String[] parts = input.split(",");
        HashMap<String, String> keyValuePairs = new HashMap<>();
        for (String part : parts) {
            part = part.trim();     //enlever les epace
            if (!part.contains("//")) {
                throw new Exception ("ERREUR // SUR: "+ part);
            }
            else{
                String[] paire = part.split("//");
                if (keyValuePairs.keySet().contains(paire[0].trim().toUpperCase())) {
                    throw new Exception("DUPLICATION DE LA COLONNE \""+paire[0].trim().toUpperCase()+"\"");
                }if(!keyValuePairs.keySet().contains(paire[0].trim().toUpperCase())){
                    keyValuePairs.put(paire[0].trim().toUpperCase(),paire[1].trim());
                }
            }
        }
        return keyValuePairs;           
    }
    /* get les colonnes por les projections  */
    public static String[] colonnes (String querry, Vector<String> mots_cles) throws Exception{
        Vector <String> keys = Utile.stringIntoWords(querry);
        String[] r = querry.trim().split(",");
        String[] re = new String[r.length];
        if (!Utile.compare2(keys, mots_cles)) {
            for (int i = 0; i < re.length; i++) {
                re[i] = r[i].trim().toLowerCase();
            }
        }
        else{
            re = null;
        }
        return re ;
    }
    /* verification de syntax pour jointure et union  */
    public static void checkSyntax(Vector<String> vector1, Vector<String> keys) throws Exception {
        int minSize = Math.min(vector1.size(), keys.size()); // Utilisation de la taille minimale des deux vecteurs
        
        System.out.println(minSize + "--------------------");
        
        for (int i = 0; i < minSize; i++) {
            if (i != 2 && i != 4) {
                if (!keys.get(i).equalsIgnoreCase(vector1.get(i))) {
                    System.out.println("error");
                    // throw new Exception("ERREUR DE SYNTAX SUR \""+vector1.get(i).toString()+"\":   "+vector1.get(i)+" ----> "+keys.get(i));
                }
            }
        }
    }
    /* VERFIER SI LES MOTS CLÉS DE LA REQUETE SONT JUSTES */
    public static boolean compare(Vector<String> vector1, Vector<String> vector2) throws Exception {
        boolean r = false;
        if (vector1.size() < vector2.size()) {
            throw new Exception("MOTS CLÉS MANQUANTS!\n");
        }
        if (vector2.size() < vector2.size()) {
            throw new Exception("MOTS CLÉS EN TROP!\n");
        }
        for (int i = 0; i < vector1.size()-1; i++) {
            if (!vector1.get(i).equalsIgnoreCase(vector2.get(i))) {
                throw new Exception("ERREUR DE SYNTAX SUR \""+vector1.get(i).toString()+"\":   "+vector1.get(i)+" ----> "+vector2.get(i));
            }
        }
        r = true;
        return r; // Les vecteurs sont égaux en ignorant les clés spécifiées.
    }
    public static boolean compare2 (Vector<String> vector1, Vector<String> vector2) throws Exception {
        boolean r = false;
        if (vector1.size() != vector2.size()) {
            return r;
        }
        for (int i = 0; i < vector1.size(); i++) {
            if (!vector1.get(i).equalsIgnoreCase(vector2.get(i))) {
                return r;
            }
        }
        r = true;
        return r; // Les vecteurs sont égaux en ignorant les clés spécifiées.
    }
    public static String[] split (String querry) throws Exception{
        if (!querry.contains(":")) {
            throw new Exception("AUCUNE COLONNE SELECTIONNÉE \":\"");
        }else{
            return querry.split(":");
        }
    }
    public static boolean isNumeric(Object obj) {
        return obj instanceof Number;
    }        
    // POUR LES MOTS CLE DE LA REQUETE efa tsy misy point
    public static Vector<String> stringIntoWords (String querry) throws Exception {
        String[] mots = querry.trim().split("\\s+");
        Vector<String> vecteurDeMots = new Vector<>();
        for (String mot : mots) {
            vecteurDeMots.add(mot);
        }
        return vecteurDeMots;
    }
}
