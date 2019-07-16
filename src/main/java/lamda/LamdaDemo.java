package lamda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LamdaDemo {

    private String name ="lamda";

    //有没有注解都无所谓
//    @FunctionalInterface
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

    @FunctionalInterface
    public interface Supplier<T> {
        T get();
    }

    public static void main(String[] args) {
        testFunction();
//        testHello();
//        testLocation();
//        testStringCallBack();
//        testForEach();
//        testNewObject();
    }

    private static void testFunction() {
        Function<Integer, Integer> expression = e -> e * 2;
        System.out.println(expression.apply(3));

        Location l = (c1,c2) -> expression.apply(c1) * expression.apply(c2);
        System.out.println(l.find(5,6));

        Function<Function<Integer, Integer>, Integer> f = e -> e.apply(6) + 3 ;
        System.out.println(f.apply(e->e *5));
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
    private static void testNewObject() {

        Runnable r = ()-> System.out.println("hello");

        Supplier<LamdaDemo> supplier = LamdaDemo::new;
        LamdaDemo demo = supplier.get();
        System.out.println(demo.name);
    }

    
    
}
