package concurrent;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {

    public static void main(String[] args) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
//        map.put("1","one");

//        String s = map.computeIfAbsent("1", (key) -> {
//            return "dog";
//        });
//
//        System.out.println(s);


        String box = map.putIfAbsent("1", "box");



    }


}
