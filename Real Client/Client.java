package client;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import classes.*;

public class Client {
    String host;
    int port;
    Socket client;
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public Socket getClient() {
        return client;
    }
    public void setClient(Socket client) {
        this.client = client;
    }
    
    public  Client () throws Exception {
        ClientManager manager = new ClientManager();
        String host = (String) manager.getServer()[0];
        int port = (int) manager.getServer()[1];
        if (port != -1 && !host.equals("IP")) 
            try {
                Socket socket = new Socket(host, port);
                setClient(socket); 
                setHost(host);setPort(port);
            } catch (Exception e) {
                throw e;   
            }          
    }
    public void sendRequest(String request, ObjectOutputStream objectOutputStream) throws Exception {
        try {
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public Object getResponse(ObjectInputStream objectInputStream) throws Exception{
        try {
            Object response = objectInputStream.readObject();
            return response;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public void run() throws Exception {
        try {
            Socket client = this.getClient();
            if (client.isConnected()) {
                System.out.println("Connecté au Serveur");
                ObjectOutputStream out  = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in  = new ObjectInputStream(client.getInputStream());
                while (true) {                
                    System.out.print("SQL>");
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
                    String request = bufferedreader.readLine();
                    if (request.equalsIgnoreCase("exit")) {
                        sendRequest("exit", out);
                        System.out.println("Au Revoir ...");
                        break; 
                    }
                    sendRequest(request, out);
                    Object answer =  getResponse(in);
                    if (answer instanceof java.lang.String) {
                        System.out.println(answer);
                    }
                    if (answer instanceof Relation) {
                       ((Relation)  answer).display();
                    }
                } 
                // Fermeture des flux et du socket
                in.close();
                out.close();
                client.close();
            }else{
                throw new Exception("Connexion non établie");
            }
        } catch (Exception e) {
            throw e;
        }
    }  
    
}