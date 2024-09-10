package com.zeetaminds.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class personSerializable {
    public static void serialize(Person person, String filename) {
        try (FileOutputStream fis = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fis)) {
            out.writeObject(person);
            System.out.println("Serialized");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static Person deserialize(String filename) {
        Person person = null;
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fis)) {
            person = (Person) in.readObject();
            System.out.println("Deserialized");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return person;
    }

    public static void main(String[] args) {
        Person person = new Person("John Doe", 25, "123 Main St");
        serialize(person, "person.ser");
        Person deserializedPerson = deserialize("person.ser");
        System.out.println(deserializedPerson);
    }
}
