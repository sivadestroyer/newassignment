import org.example.MyHashtable;
public class MyHashtableTest {
    public static void main(String[] args) {
        MyHashtable htable = new MyHashtable(5);

        htable.put("1", "ONE");
        htable.put("2", "TWO");
        htable.put("3", "THREE");
        htable.put("4", "FOUR");
        htable.put("5", "FIVE");

        System.out.println(htable.get("1"));
        System.out.println(htable.get("2"));
        System.out.println(htable.get("3"));
        System.out.println(htable.get("4"));
        System.out.println(htable.get("5"));
        System.out.println(htable.get("6"));

        htable.remove("2");
        System.out.println(htable.get("2"));
        System.out.println(htable.get("3"));
    }
}
