package serveur;

import java.net.Socket;
public class ClientHandler implements Runnable {
    Serveur serveur;
    Socket client;
    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public ClientHandler(Serveur serveur) throws Exception {
        setServeur(serveur);
        try {
            setClient(serveur.accept());
        } catch (Exception e) {
            throw e;
        }
    }

    public Serveur getServeur() {
        return serveur;
    }

    public void setServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    @Override
    public void run() {
        try {
            System.out.println("Connecté au client : "+ client.getLocalPort());
            this.getServeur().run(getClient());
        } catch (Exception e) {
            e.printStackTrace();
        }
            
    }


}
