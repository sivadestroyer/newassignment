package org.example;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleLinkedListTest {
    private SingleLinkedList<Integer> sllt;

    @BeforeEach
    void setUp() {
            // setup single linked list for testing
        sllt= new SingleLinkedList<Integer>();
        sllt.add(1);
        sllt.add(2);
        sllt.add(3);
        sllt.add(4);
        sllt.add(5);


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getFirst() {
        assertEquals(1, sllt.getFirst());
    }

    @Test
    void getLast() {
        assertEquals(5, sllt.getLast());
    }

    @Test
    void addFirst() {
        sllt.addFirst(0);
        assertEquals(0, sllt.getFirst());
    }

    @Test
    void addLast() {
        sllt.addLast(6);
        assertEquals(6, sllt.getLast());
    }

    @Test
    void add() {
        sllt.add(8);
        assertEquals(8,sllt.getLast());
    }

    @Test
    void removeFirst() {
        sllt.removeFirst();
        assertEquals(2, sllt.getFirst());
    }

    @Test
    void removeLast() {
        sllt.removeLast();
        assertEquals(4, sllt.getLast());
    }

    @Test
    void size() {
        assertEquals(5, sllt.size());
    }

    @Test
    void printList() {
        List<Integer> result = List.of(1, 2, 3, 4,5);
        assertEquals(sllt.printList(),result);

    }

    @Test
    void reverse() {
        sllt.reverse();
        assertEquals(5, sllt.getFirst());
        assertEquals(1, sllt.getLast());
    }

    @Test
    void findmiddle() {
        assertEquals(3, sllt.findmiddle());  // middle element is 3
    }

    @Test
    void findcycle() {

    }

    @Test
    void contains() {

    }

    @Test
    void sortedmerge() {
    }

    @Test
    void clear() {
        sllt.clear();
        assertEquals(sllt.size(), 0);
    }
}