package lamda;

import java.util.ArrayList;
import java.util.List;

public class LamdaDemo {

    @FunctionalInterface
    public interface Hello {
        String msg(String info);
    }

    @FunctionalInterface
    public interface Location {
        int find(int x,int y);
    }

    @FunctionalInterface
    public interface StringCallBack {
        void doWithString(String str);
    }

    public static void main(String[] args) {
        testHello();
        testLocation();
        testStringCallBack();
        testForEach();
    }

    private static void testHello() {
        Hello  hello = param -> param + "world";
        System.out.println(hello.msg("hello,"));
    }

    private static void testLocation() {

        Location l1 = (x, y) -> x+y;
        System.out.println(l1.find(5,6));

        Location l2 = (x, y) -> x*y;
        System.out.println(l2.find(5,6));

        Location l3 = (x, y) -> {

            int z = x+y;

            return z*z;
        };
        System.out.println(l3.find(5,6));
    }

    private static void testStringCallBack() {

        List<String> list = new ArrayList<>();

        StringCallBack scb = list::add;
        scb.doWithString("box");
        scb.doWithString("cup");
        System.out.println(list.get(0));

        scb = list::remove;
        scb.doWithString("box");
        System.out.println(list.get(0));
    }

    private static void testForEach() {
        List<String> list = new ArrayList<>();
        list.add("Bob");
        list.add("John");
        list.add("Mike");
        list.forEach(System.out::println);

        list.forEach(x ->{
            System.out.println("start...");
            System.out.println(x);
        });
    }


    
    
}
