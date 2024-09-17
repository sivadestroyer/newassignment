package com.zeetaminds.serialize;
import java.io.*;
public class SerializeDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SerializableImp<Person> service = new SerializableImp<>();
        Person person = new Person("siva",24,"2/122 rasipuram namakkal");
        OutputStream os = new FileOutputStream("/home/sivabala/IdeaProjects/newassignment/src/main/java/com/zeetaminds/serialize/serialize.ser");
        service.serialize(person, os);
        InputStream is = new FileInputStream("serialized.txt");
            Person deserializedObj = service.deserialize(is);
            System.out.println("Deserialized object: " + deserializedObj);

    }
}
