package org.example;

public class MyHashtable {

    private class Entry  {
        int hash;
        Object key;
        Object value;
        Entry next;

        protected Entry(int hash, Object key, Object value, Entry next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }


    private Entry[] table;
    private int count;

    public MyHashtable(int capacity) {
        table = new Entry[capacity];
        count = 0;
    }

    public MyHashtable() {
        this(10);
        count = 0;

    }

    public Object put(Object key, Object value) {
        if (key ==null){
            System.out.println("key is null");
        }
            int hash =key.hashCode();
            int index = hash % table.length;

            for (Entry e = table[index]; e != null;e=e.next){
                if(e.hash==hash && e.key.equals(key)){
                    Object oldValue = e.value;
                    e.value = value;
                    return oldValue;
                }
            }
        Entry newentry = new Entry(hash,key,value,table[index]);
            table[index] = newentry;
            count++;
            return null;

    }


    public Object get(Object key) {
        if (key ==null){
            System.out.println("key is null");
        }
        int hash=key.hashCode();
        int index=hash%table.length;
        for (Entry e=table[index];e !=null;e=e.next){
            if (e.key== key){
                return e.value;
            }
        }
        return null;
    }

    public Object remove(Object key) {
                if (key == null){
                    System.out.println("key is null");
                }
                int hash=key.hashCode();
                int index =hash % table.length;
                Entry prev=null;
                for (Entry e=table[index];e !=null; prev=e,e=e.next){
                        if(e.hash==hash && e.key.equals(key)){
                            if (prev !=null){
                                prev.next=e.next;
                            }else{
                                table[index]=e.next;
                            }
                            count --;
                            return e.value;
                        }
                }
                return null;
    }



    public static void main(String[] args) {

        MyHashtable htable = new MyHashtable();

        htable.put("1", "ONE");
        htable.put("2", "TWO");

        System.out.println(htable.get("1"));

    }

}
