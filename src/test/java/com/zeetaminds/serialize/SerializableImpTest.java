package com.zeetaminds.serialize;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class SerializableImpTest {

    @Test
    void serialize() throws IOException {
        Person person = new Person("siva", 24, "rasipuram");
        SerializableImp<Person> service = new SerializableImp<>();

        // Use ByteArrayOutputStream to capture the serialized data
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        service.serialize(person, baos);

        // Check if the ByteArrayOutputStream is not empty (i.e., object is serialized)
        assertTrue(baos.size() > 0);
    }

    @Test
    void deserialize() throws IOException, ClassNotFoundException {
        Person person = new Person("siva", 24, "rasipuram");
        SerializableImp<Person> service = new SerializableImp<>();

        // Serialize the person object to ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        service.serialize(person, baos);

        // Convert the serialized data to ByteArrayInputStream for deserialization
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        // Deserialize the person object
        Person deserializedPerson = service.deserialize(bais);

        // Verify that the original and deserialized objects are the same
        assertEquals(person.getName(), deserializedPerson.getName());
        assertEquals(person.getAge(), deserializedPerson.getAge());
        assertEquals(person.getAddress(), deserializedPerson.getAddress());
    }
}
