package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyHashtableTest {
    private MyHashtable<Integer, String> hashtable;
    @BeforeEach
    void setUp() {
    hashtable = new MyHashtable<>();
    hashtable.put(1, "One");
    hashtable.put(2, "Two");
    hashtable.put(3, "Three");
    hashtable.put(4, "Four");

    }

    @Test
    void put() {
     // Test case for inserting elements into the hashtable
     assertEquals(hashtable.get(1), "One");
     assertEquals(hashtable.get(2), "Two");
     assertEquals(hashtable.get(3), "Three");
     assertEquals(hashtable.get(4), "Four");
    }

    @Test
    void get() {
        // Test case for finding an element in the hashtable
        assertEquals(hashtable.get(2), "Two");
        assertEquals(hashtable.get(9), null);
    }

    @Test
    void size() {
        // Test case for checking the size of the hashtable
        assertEquals(hashtable.size(), 4);

    }

    @Test
    void isEmpty() {
        // Test case for checking if the hashtable is empty
        assertFalse(hashtable.isEmpty());
    }

    @Test
    void clear() {
        hashtable.clear();
        assertEquals(hashtable.size(),0);
    }

    @Test
    void containsKey() {
        // Test case for checking if a key exists in the hashtable
        assertTrue(hashtable.containsKey(1));
        assertFalse(hashtable.containsKey(5));
    }

    @Test
    void remove() {
        hashtable.remove(1);
        assertEquals(hashtable.size(), 3);
        assertEquals(hashtable.get(1), null);
        assertEquals(hashtable.containsKey(1),false);
    }
}