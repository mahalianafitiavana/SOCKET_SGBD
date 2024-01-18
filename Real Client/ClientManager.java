package client;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class ClientManager {
    public Object[] getServer() throws Exception{
        Object[] server = new Object[2];
        server[0] = "IP";server[1] = -1;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("server.xml");
            Element serverElement = (Element) document.getElementsByTagName("server").item(0);
            server[0] = serverElement.getElementsByTagName("ip").item(0).getTextContent();
            server[1] = Integer.parseInt(serverElement.getElementsByTagName("port").item(0).getTextContent());
        } catch (Exception e) {
            throw e;
        }
        return server;
    }
    
}
