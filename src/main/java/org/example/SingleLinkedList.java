package org.example;
import java.util.List;
import java.util.ArrayList;

public class SingleLinkedList<T extends Comparable<T>> {

    private class Node {
        T item;
        Node next;
        Node prev;
        Node(T item, Node next) {
            this.item = item;
            this.next = next;
        }
        Node(T item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
        Node(T item) {
            this.item = item;
            this.next = null;
            this.prev = null;
        }
    }


    int size;    // number of nodes in the list
    Node first; // head pointer
    Node last;  // tail pointer

    public SingleLinkedList() {
        size = 0;
        first = null;
        last = null;
    }

    public T getFirst() {
        if (first == null) {
            return null;
        }
        return first.item;
    }

    public T getLast() {
        if (last == null) {
            return null;
        }
        return last.item;
    }

    public void addFirst(T e) {
        Node newNode = new Node(e);
        if (first == null) {
            last = newNode;
        }
        Node temp = first;
        first = newNode;
        first.next=temp;
        temp.prev=first;
        size++;

    }

    public void addLast(T e) {
        Node newNode = new Node(e);
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;

        }
        Node temp =last;
        last = newNode;
        last.prev=temp;

        size++;

    }

    public boolean add(T e) {
        addLast(e);
        return true;
    }


    public T removeFirst() {
        if (first == null) {
            return null;
        }
        T result = first.item;
        first = first.next;
        first.prev = null;
        return result;

    }

    public T removeLast() {
        if (last == null) {
            return null;
        }
//        Node traverse = first;
//        Node prev = null;
//
//        while (traverse.next != null) {
//            prev = traverse;
//            traverse = traverse.next;
//        }
//        T result = last.item;
//        last = prev;
//        last.next = null;
//        size--;
//        return result;
        T result = last.item;
        last = last.prev;
        if (last!=null) {
            last.next = null;
        } else {
            first = null;
        }
        size--;
        return result; 
    }


    public int size() {
        return size;
    }

    public List<T> printList() {
        List<T> result = new ArrayList<>();
        Node current = first;
        while (current != null) {

            result.add(current.item);
            current = current.next;
        }
        return result;
    }

    public void reverse() {
        Node previous = null;
        Node current = first;
        Node next = null;
        while (current != null) {
            next = current.next;
            current.next = previous;
            current.prev = next;
            previous = current;
            current = next;

        }
        last = first;
        first = previous;
        last.next = null;
        first.prev = null;
        printList();
    }

    public T findmiddle() {
        Node fast = first;
        Node slow = first;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow.item;
    }

    public boolean findcycle() {
        Node fast = first;
        Node slow = first;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }
        return false;

    }

    public boolean contains(T e) {
        Node temp = first;
        while (temp != null) {
            if (temp.item == e) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public void sortedmerge(SingleLinkedList<T> t, SingleLinkedList<T> k) {
        SingleLinkedList<T> newList = new SingleLinkedList<>();
        if (t.first == null) {
            t.first = k.first;
            t.last = k.last;
        } else if (k.first == null) {
            return;
        }
        Node current = t.first;
        Node current2 = k.first;

        while (current != null && current2 != null) {
            if (current.item.compareTo(current2.item) <= 0) {
                newList.addLast(current.item);
                current = current.next;
            } else {
                newList.addLast(current2.item);
                current2 = current2.next;
            }
        }
        while (current != null) {
            newList.addLast(current.item);
            current = current.next;

        }
        while (current2 != null) {
            newList.addLast(current2.item);
            current2 = current2.next;
        }
        t.first = newList.first;
        t.last = newList.last;

        printList();

    }


    public static class NoSuchElementException extends RuntimeException {

        public NoSuchElementException() {
            super();
        }

        public NoSuchElementException(String s) {
            super(s);
        }
    }

    public void clear() {
        first = null;
        last = null;
        size = 0;
    }


    public static void main(String[] args) {

        SingleLinkedList<String> list = new SingleLinkedList<>();

        list.add("one");
        list.add("two");
        list.add("three");
        list.add("four");
        list.add("five");
        System.out.println(list.size());
        list.printList();
        list.reverse();
        System.out.println(list.findmiddle());
        System.out.println(list.findcycle());
        System.out.println(list.contains("two"));
    }

}