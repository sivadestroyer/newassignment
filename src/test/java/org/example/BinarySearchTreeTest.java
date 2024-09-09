package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {

    private BinarySearchTree<Integer> bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
        bst.insert(50);
        bst.insert(30);
        bst.insert(20);
        bst.insert(40);
        bst.insert(70);
        bst.insert(60);
        bst.insert(80);
    }

    @Test
    void insert() {
        // Test case for inserting elements into the BST

        // Test case for finding an element in the BST
        assertEquals(bst.find(20), true);
        assertEquals(bst.find(90), false);
    }

    @Test
    void inorder() {
        List<Integer> expected = List.of(20,30,40,50,60,70,80);
    assertEquals(bst.inorder(),expected);
    }

    @Test
    void preorder() {
        List<Integer> expectedPreorder = List.of(50, 30, 20, 40, 70, 60, 80);
        assertEquals(expectedPreorder, bst.preorder());
    }

    @Test
    void postorder() {
         List<Integer> expectedPostorder = List.of(20, 40, 30, 60, 80, 70, 50);
        assertEquals(expectedPostorder, bst.postorder());
    }

    @Test
    void find() {

        assertEquals(bst.find(20), true);
        assertEquals(bst.find(90), false);
    }

    @Test
    void delete() {

        bst.delete(20);
        assertEquals(bst.find(20), false);
    }

    @Test
    void findmin() {
        assertEquals(bst.findmin(), 20);
    }

    @Test
    void findmax() {
        assertEquals(bst.findmax(), 80);
    }

    @Test
    void height() {

        assertEquals(bst.height(), 3);
    }

    @Test
    void balance() {
        assertEquals(bst.balance(), true);
    }

    @Test
    void isBst() {

        assertTrue(bst.isBst());

    }
}