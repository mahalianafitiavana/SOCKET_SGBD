package serveur;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import Utile.*;

public class Serveur {
    int port;
    ServerSocket serveur;
    public Socket accept () throws Exception{
        Socket client = null;
        try {
            client = this.getServeur().accept();
        } catch (IOException e1) {
           throw e1;
        }
        return client;
    }
    public Serveur(int port) {
        try {
            ServerSocket serveur = new ServerSocket(port);
            this.setServeur(serveur);;
            this.setPort(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
  
    public ServerSocket getServeur() {
        return serveur;
    }
    public void setServeur(ServerSocket serveur) {
        this.serveur = serveur;
    }
    public String getRequest(ObjectInputStream in) throws Exception {
        String request = null;
        try {
            request = (String) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
    public Object getResponseToSend(String request) throws Exception {
        return  Utile.doIt(request);       
    }
    public void sendResponse(String request, ObjectOutputStream out) throws Exception {
        try {
            Object response = getResponseToSend(request);
            out.writeObject(response);
            out.flush();
        } catch (Exception e) {
            out.writeObject(e.getMessage()); out.flush();
        }
    }

    public void run(Socket client) throws Exception {
        ObjectInputStream in = null; ObjectOutputStream out = null;
        try {
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
            while (true) {
                String message = getRequest(in);
                System.out.println("Requête du Client:"+client.getPort() +"  \""+message+"\"");
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Client :"+client.getPort() +" déconnecté ");
                    sendResponse("exit", out); out.close();in.close();
                    break;
                }
                sendResponse(message, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
        
}