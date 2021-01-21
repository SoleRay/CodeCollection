package lamda;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class LamdaDemo2 {

    class Person {

        private int age;

        private String name;

        public Person(int age) {
            this.age = age;
        }

        public Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    class Will<T> {

        private List<T> list;

        public Will(List<T> list) {
            this.list = list;
        }

        public void sort(Compare<T> c){
            System.out.println("...");
        }
    }

    @FunctionalInterface
    interface Compare<T>{

        int compare(T o1,T o2);

        boolean equals(Object obj);

    }

    @Test
    public void test2(){
        List<Person> peoples = Arrays.asList(new Person[]{new Person(5,"box"),new Person(1,"box")});
        Will<Person> will = new Will<>(peoples);

        long c = peoples.stream().filter((person) -> person.age > 3)
                .filter((person) -> person.name.equals("box")).sorted().count();

        System.out.println(c);
    }


    /**简化版*/
    public void test(){
        List<Person> peoples = Arrays.asList(new Person[]{new Person(5),new Person(6)});

        /**1.最原始的匿名内部类*/
        Collections.sort(peoples, new Comparator<>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getAge().compareTo(p2.getAge());
            }
        });

        /**2.将1里的匿名内部类直接翻译成lamda表达式以后的方式*/
        Collections.sort(peoples,(p1,p2)->{ return p1.getAge().compareTo(p2.getAge()); });


        /**3.在2的基础上，将p1.getAge()和p2.getAge()方法提取出来，做成一个函数表达式*/
        Function<Person,Integer> f = p -> p.getAge();
        Collections.sort(peoples,(p1,p2)-> f.apply(p1).compareTo(f.apply(p2)));


        /**4.上面3提取的函数表达式，在Comparator中已经做了封装，故直接调用*/
        Collections.sort(peoples,Comparator.comparing(p -> p.getAge()));


        /**5.上面4的入参可以更加地简化*/
        Collections.sort(peoples,Comparator.comparing(Person::getAge));


        /**6.集合类提供了新的sort方法，可以直接调用*/
        peoples.sort(Comparator.comparing(Person::getAge));
    }




    public static void main(String[] args) {




    }
}
