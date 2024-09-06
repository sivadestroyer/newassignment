package org.example;

public class SingleLinkedList<T> {

    private class Node {
        T item;
        Node next;

        Node(T item, Node next) {
            this.item = item;
            this.next = next;
        }
        Node(T item){
            this.item=item;
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
         if (first == null){
             return null;
         }
         return first.item;
    }

    public T getLast() {
        if (last==null){
             return null;
        }
        return last.item;
    }

    public void addFirst(T e) {
        Node newNode = new Node(e);
        if (first == null) {
            last = newNode;
        }
        first = newNode;
        size++;

    }

    public void addLast(T e) {
        Node newNode =new Node(e);
        if(last==null){
            first=newNode;
        }else{
            last.next=newNode;

        }
         last=newNode;

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
        return result;

    }
    public T removeLast() {
        if (last==null){
            return null;
        }
        Node traverse = first;
        Node prev = null;

        while(traverse.next != null){
            prev=traverse;
            traverse=traverse.next;
        }
        T result = last.item;
        last=prev;
        last.next=null;
        size--;
        return result;
    }



    public int size() {
        return size;
    }

    public void printList(){
        Node current= first;
        while(current !=null){
            if (current.next != null) {
                System.out.print(current.item + "->");
                ;
            }else {
                System.out.println(current.item);
            }

            current=current.next;
        }
    }
    public void reverse(){
        Node previous=null;
        Node current=first;
        Node next=null;
        while (current !=null){
            next=current.next;
            current.next=previous;
            previous=current;
            current=next;

       }
        last=first;
        first=previous;
        last.next=null;
        printList();
    }
    public T findmiddle(){
        Node fast=first;
        Node slow=first;
        while(fast.next != null && fast.next.next !=null){
            slow=slow.next;
            fast=fast.next.next;
        }
        return slow.item;
    }
    public boolean findcycle(){
        Node fast=first;
        Node slow = first;
        while(fast.next!=null && fast.next.next !=null){
            slow=slow.next;
            fast=fast.next.next;
            if(slow==fast){
                return true;
            }
        }
        return false;

    }
    public boolean contains(T e){
        Node temp = first;
        while(temp != null){
            if (temp.item == e){
                return true;
            }
            temp=temp.next;
        }
        return false;
    }
    public static class NoSuchElementException extends RuntimeException {

        public NoSuchElementException() {
            super();
        }
        public NoSuchElementException(String s) {
            super(s);
}
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