package lamda;

import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;


/**
 *
 * 什么是函数式接口？
 *     1.必须是接口interface
 *     2.函数式接口和普通接口一样，允许定义一个或者多个默认方法(default)
 *     3.函数式接口和普通接口一样，允许定义一个或者多个静态方法(static)
 *     4.函数式接口里允许定义java.lang.Object里的public方法
 *     5.除以上2-4点外，函数式接口只能定义一个抽象方法，这种类型的接口也称为SAM接口，即Single Abstract Method interfaces。
 *     6.符合以上5点，就是一个函数式接口，注解不是必须的选项。但是加了注解的接口，如果不满足以上5点，编译时会提示报错。
 *
 * 常见的几个默认的函数式接口：
 *     1.Funtion<T,R>   接受一个参数T，返回一个值R   默认方法：R apply(T t)
 *     2.Consumer<T>    接受一个参数T，无返回值      默认方法：void accpet(T t)
 *     3.Predicate<T>   接受一个参数T，返回布尔值    默认方法：void test(T t)
 *     4.Supplier<T>    不接受参数，返回一个值T      默认方法：T get()
 *
 * 自定义函数式接口：
 *     1.以上的几个常用默认接口并不能满足全部的需要，比如：函数需要接受多个参数
 *     2.出于业务的需要，往往需要明确的接口名称和接口函数名称，来明确某种业务场景或者需求
 *
 *
 * */
public class LamdaDemo {

    private String name ="lamda";


    @FunctionalInterface
    public interface Location {
        int find(int x,int y);

        public static Location multiply(Function<Integer,Integer> expression){
            return (c1,c2) -> expression.apply(c1) * expression.apply(c2);
        }
    }

    @Test
    public void testBase() {

        Runnable r = ()-> System.out.println("hello");
        Thread t = new Thread(r);
        t.start();
    }

    @Test
    public void testFunction() {
        //基本使用
        Function<Integer, Integer> expression = e -> e * 2;
        System.out.println(expression.apply(3));

        //函数套函数
        Function<Function<Integer, Integer>, Integer> f = e -> e.apply(6) + 3 ;
        System.out.println(f.apply(e->e *5));

        //复合使用
        Location l = (c1,c2) -> expression.apply(c1) * expression.apply(c2);
        System.out.println(l.find(5,6));

        Location l2 = Location.multiply(e -> e * 2);
        System.out.println(l2.find(7,8));
        /**
         *
         * andThen方法：先执行andThen方法调用者的函数，然后将调用者函数的结果，作为参数，传给andThen方法里的函数
         * 举例：A.andThen(B).apply(3);
         * 先把3作为参数传给函数A，执行完函数A的结果，作为参数，传递给函数B。得到最终结果
         *
         * andThen的定义，本身也是一种函数套函数的体现。
         * 我们这里需要区分，调用和定义。
         *
         * andThen的定义 属于 函数套函数的定义
         * adnThen的调用 属于 函数套函数的调用，也属于普通方法的调用
         */
        Integer afterValue = expression.andThen(e -> e + 2).apply(5);
        System.out.println(afterValue);


        /**
         * compose：先执行compose里的函数，然后将compose里的函数的结果，作为参数，传给调用者函数
         * 举例：A.compose(B).apply(3);
         * 先把3作为参数传给函数B，执行完函数B的结果，作为参数，传递给函数A。得到最终结果
         *
         * 与andThen函数除了逻辑的差异外，还有使用上的差异
         * andThen时，B方法可以直接写表达式，不用预定义
         *
         * 但使用compose时，B方法必须先定义，再调用
         */
        Function<Integer, Integer> before = e -> e * 2;
        Integer beforeValue = expression.compose(before).apply(3);
        System.out.println(beforeValue);

        /**
         * identity，输入参数是几，返回参数就是几
         */
        Function<Integer, Integer> self = Function.identity();
        System.out.println(self.apply(5));


    }


    @Test
    public void testConsumer() {
        List<String> list = new ArrayList<>();

        //最基本的调用
        Consumer<String> scb = list::add;
        scb.accept("box");
        scb.accept("cup");
        System.out.println(list.get(0));

        scb = list::remove;
        scb.accept("box");
        System.out.println(list.get(0));

        //forEach本身就是Consumer的使用
        List<String> fe = new ArrayList<>();
        fe.add("Bob");
        fe.add("John");
        fe.add("Mike");
        fe.forEach(System.out::println);

        list.forEach(x ->{
            System.out.println("start...");
            System.out.println(x);
        });

        //andThen的调用
        List<String> arrayList = new ArrayList<>();
        Consumer<String> add = arrayList::add;

        add.andThen(add).accept("hello");
        System.out.println(arrayList.size());
    }

    /**
     * 更实在的用法，参考LinkedList的removeIf
     *
     * 原理：循环LinkedList中的每个节点，调用Predicate的test方法
     * 如果符合，就remove掉
     *
     */
    @Test
    public void testPredicate() {
        //test的使用
        Predicate<Integer> p = x -> x > 0;
        boolean b0 = p.test(5);
        System.out.println("b0="+b0);

        //isEqual的使用
        boolean b1 = Predicate.isEqual("456").test("456");
        boolean b2 = Predicate.isEqual("456").test("123");
        System.out.println("b1="+b1+",b2="+b2);

        //and的使用
        Predicate<Integer> p1 = x -> x > 5;
        Predicate<Integer> p2 = x -> x < 10;
        boolean b3 = p1.and(p2).test(6);
        boolean b4 = p1.and(p2).test(4);
        boolean b5 = p1.and(p2).test(12);
        System.out.println("b3="+b3+",b4="+b4+",b5="+b5);

        //or的使用
        Predicate<Integer> p3 = x -> x < 5;
        Predicate<Integer> p4 = x -> x > 10;
        boolean b6 = p3.or(p4).test(4);
        boolean b7 = p3.or(p4).test(11);
        boolean b8 = p3.or(p4).test(6);
        System.out.println("b6="+b6+",b7="+b7+",b8="+b8);

        //negate的使用
        Predicate<Integer> p5 = x -> x < 5;
        boolean b9 = p5.negate().test(6);
        System.out.println("b9="+b9);

        //not的使用,这个是jdk11才有的方法，注意Java Complier的设置
        Predicate<Integer> p6 = x -> x>5 && x<10;
        Predicate<Integer> p7 = Predicate.not(p6);

        boolean b10 = p7.test(4);
        boolean b11 = p7.test(11);
        boolean b12 = p7.test(6);
        System.out.println("b10="+b10+",b11="+b11+",b12="+b12);

        List<String> list = new ArrayList<>();
        list.add("box");
        list.add("dog");
        list.add("jackson");
        list.removeIf((string)-> string!=null && string.equals("dog"));
        System.out.println(list);
    }

    @Test
    public void testSupplier() {
        Supplier<LamdaDemo> supplier = LamdaDemo::new;
        Supplier<String> stringSupplier = ()-> "box";
        LamdaDemo demo = supplier.get();
        System.out.println(demo.name);
        System.out.println(stringSupplier.get());
    }


    //自定义多参数
    @Test
    public void testFunctionalInterface() {
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

    /**
     * limit 主要用来限制显示的量
     *
     * map 主要用来对传入的参数进行逻辑处理
     *
     * Filter 用来过滤所需要的数据
     *
     * collect 生成集合，一般配合Collectors
     */

    @Test
    public void testStream(){
        List<String> list = new ArrayList<>();
        list.add("box");
        list.add("dog");
        list.add("jackson");

        List<Bird> birds = list.stream().map(Bird::new).collect(Collectors.toList());

        Random random = new Random();
        random.ints().limit(10).forEach(System.out::println);
    }

    public static class Bird {

        private String name;

        public Bird(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
