package com.zeetaminds.io;
import java.io.*;
import com.zeetaminds.io.Logger.*;
public class PersonSerializable {
     String Filename = "/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/io/person.txt";
    public  FileLogger logger = new FileLogger(Filename);
    // Generalized serialize method using OutputStream
    public void serialize(Person person, OutputStream os) throws IOException {
       ObjectOutputStream out = new ObjectOutputStream(os);
            out.writeObject(person);
            logger.log("serialized");
    }

    // Generalized deserialize method using InputStream
    public Person deserialize(InputStream is) throws IOException, ClassNotFoundException {
        Person person = null;
        ObjectInputStream in = new ObjectInputStream(is);
            person = (Person) in.readObject();
            logger.log("deserialized");
        return person;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Person person = new Person("John Doe", 25, "123 Main St");
        PersonSerializable p = new PersonSerializable();

       FileOutputStream fos = new FileOutputStream("person.ser");
            p.serialize(person, fos);


        // Deserializing from a file
        Person deserializedPerson = null;
        FileInputStream fis = new FileInputStream("person.ser");
            deserializedPerson = p.deserialize(fis);

        p.logger.log(String.valueOf(deserializedPerson));
    }
}
