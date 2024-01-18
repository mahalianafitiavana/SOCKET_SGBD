package file;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.Vector;
import classes.*;
public class Writer {
      public static void writeObject(String title, Object o) throws Exception {
            File file = new File(title + ".dat");
            FileOutputStream output = new FileOutputStream(file);
            ObjectOutputStream writer = new ObjectOutputStream(output);
            writer.writeObject(o);
            output.close();
            writer.close();
            file.setExecutable(true,false);
            file.setReadable(true,false);
            file.setWritable(true,false);
      }
      public static  Vector readObject(String title) throws Exception{
            title = title.trim();
            File file = new File(title + ".dat");
            if (!file.exists()) {
                  throw new Exception("LE FICHIER "+title+" N' EXISTE PAS");
            }
            FileInputStream input = new FileInputStream(file);
            ObjectInputStream reader = new ObjectInputStream(input);
            Vector v = (Vector) reader.readObject();
            input.close();
            reader.close();
            return v;
      }
      public static  Relation read (String title) throws Exception{
            title = title.trim();
            File file = new File(title + ".dat");
            if (!file.exists()) {
                  throw new Exception("LE FICHIER "+title+" N' EXISTE PAS");
            }
            FileInputStream input = new FileInputStream(file);
            ObjectInputStream reader = new ObjectInputStream(input);
            Relation v = (Relation) reader.readObject();
            input.close();
            reader.close();
            return v;
      }
    
      
}
