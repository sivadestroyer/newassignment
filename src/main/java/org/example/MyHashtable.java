package org.example;

import java.util.NoSuchElementException;
import java.util.Hashtable;
import java.util.LinkedList;

public class MyHashtable<K, V> {

    private class Entry {
        int hash;
        K key;
        V value;
        Entry next;

        protected Entry(int hash, K key, V value, Entry next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private LinkedList<Entry>[] table;
    private int count;


    public MyHashtable(int capacity) {
        // Use an Object array and cast it to LinkedList<Entry>[]
        table = (LinkedList<Entry>[]) new LinkedList[capacity];
        count = 0;
        // Initialize each bucket in the array
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
    }

    public MyHashtable() {
        this(10);
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Key is null");
        }
        int hash = key.hashCode();
        int index = hash % table.length;

        for (Entry e : table[index]) {
            if (e.hash == hash && e.key.equals(key)) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }

        Entry newEntry = new Entry(hash, key, value, null);
        table[index].add(newEntry);
        count++;

        if ((float) count / table.length > 0.75) {
            resize();
        }

        return null;
    }

    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("Key is null");
        }
        int hash = key.hashCode();
        int index = hash % table.length;
        for (Entry e : table[index]) {
            if (e.hash == hash && e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public void clear() {
        table = (LinkedList<Entry>[]) new LinkedList[table.length];
        count = 0;
        // Initialize each bucket in the array
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
    }

    public boolean containsKey(K key) {
        if (key == null) {
            throw new NullPointerException("Key is null");
        }
        int hash = key.hashCode();
        int index = hash % table.length;
        for (Entry e : table[index]) {
            if (e.hash == hash && e.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException("Key is null");
        }
        int hash = key.hashCode();
        int index = hash % table.length;
        LinkedList<Entry> bucket = table[index];
        for (Entry e : bucket) {
            if (e.hash == hash && e.key.equals(key)) {
                bucket.remove(e);
                count--;
                return e.value;
            }
        }
        return null;
    }

    private void resize() {
        LinkedList<Entry>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity * 2;
        table = (LinkedList<Entry>[]) new LinkedList[newCapacity];
        count = 0;

        // Initialize each bucket in the array
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }

        for (LinkedList<Entry> bucket : oldTable) {
            for (Entry entry : bucket) {
                put(entry.key, entry.value);
            }
        }
    }
    // Additional methods for iterating over the keys and values


    public static void main(String[] args) {
        MyHashtable<String, String> htable = new MyHashtable<>();

        htable.put("1", "ONE");
        htable.put("2", "TWO");

        System.out.println("Value for key '1': " + htable.get("1"));
        System.out.println("Size: " + htable.size());
    }
}
