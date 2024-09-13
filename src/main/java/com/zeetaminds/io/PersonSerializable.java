package com.zeetaminds.io;
import java.io.*;
import com.zeetaminds.io.Logger.*;
public class PersonSerializable {
     String Filename = "/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/io/person.txt";
    public  FileLogger logger = new FileLogger(Filename);
    // Generalized serialize method using OutputStream
    public void serialize(Object obj, OutputStream os) throws IOException {
       ObjectOutputStream out = new ObjectOutputStream(os);
            out.writeObject(obj);
            logger.log("serialized");
    }

    // Generalized deserialize method using InputStream
    public Object deserialize(InputStream is) throws IOException, ClassNotFoundException {
        Object obj;
        ObjectInputStream in = new ObjectInputStream(is);
            obj = in.readObject();
            logger.log("deserialized");
        return obj;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Person person = new Person("John Doe", 25, "123 Main St");
        PersonSerializable p = new PersonSerializable();

       FileOutputStream fos = new FileOutputStream("person.ser");
            p.serialize(person, fos);


        // Deserializing from a file
        Person deserializedPerson = null;
        FileInputStream fis = new FileInputStream("person.ser");
            deserializedPerson = (Person)p.deserialize(fis);

        p.logger.log(String.valueOf(deserializedPerson));

        p.serialize(new Test("giri"), new ByteArrayOutputStream());
    }

    static class Test {
        private final String name;

        Test(String name) { this.name = name; }
    }
}
