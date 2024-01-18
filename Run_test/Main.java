package main;
import serveur.*;

public class Main {
    public static void main(String[] args)  {
      try { 
         Serveur serveur = new Serveur(4848);
         while (true) {
            ClientHandler c = new ClientHandler(serveur);
            Thread t = new Thread(c);
            t.start();           
         }
      } catch (Exception e) {
            e.printStackTrace();
      }
    }
}
 