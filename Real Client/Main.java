package main;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import client.*;

public class Main {
    public static void main(String[] args)  {
      
      try { 
         Client client = new Client();
         client.run();
      } catch (Exception e) {
         System.out.println("Serveur non Disponible");
      } 
    }
}
 