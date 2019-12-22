package collection.generic;

import java.util.ArrayList;
import java.util.List;



public class GenericDemo {

    /**
     * 给定一个List<? extends Number>的集合
     *
     * 你可以插入什么值？
     * 1.你无法插入一个Integer,因为实际的容器可能是List<Double>
     * 2.你无法插入一个Double,因为实际的容器可能是List<Integer>
     * 3.事实上，你不能往List<? extends Number>中插入任何类型的对象，
     *
     * 你可以获取什么值？
     * 1.你无法得到一个Integer,因为实际的容器可能是List<Double>
     * 2.你无法得到一个Double,因为实际的容器可能是List<Integer>
     * 3.你可以确定地得到一个Number类型的值,因为List<? extends Number>中存储的，要么是Number类型的值，要么是Number子类的值
     *
     * 所以，如果你要求这个集合只能获取值，而不能插入值，那么请使用<? extends T>
     *
     */

    public void extendsDemo(){
        List<? extends Number> list = new ArrayList<>();

        //list.add(Integer.valueOf(1)); //Error
        //list.add(Double.valueOf(1.0));//Error

        Number number = list.get(0);//Ok
    }

    /**
     * 给定一个List<? super Integer>的集合
     *
     * 你可以插入什么值？
     * 1.你可以插入一个Integer,因为实际的容器至少是List<Integer>
     * 2.你可以插入一个Integer的子类,因为Integer的子类也属于Integer
     * 3.你无法插入一个Double,因为实际的容器可能是List<Integer>
     *
     *
     * 你可以获取什么值？
     * 1.你无法得到一个Integer,因为实际的容器可能是List<Number>
     * 2.你无法得到一个Number,因为实际的容器可能是List<Object>
     * 3.事实上，你只能得到一个Object类型的对象。因为任何类型都是Object的子类
     *
     * 所以，如果你要求这个集合只能插入值，而不能获取值，那么请使用<? super T>
     *
     */
    public void superDemo(){
        List<? super Integer> list = new ArrayList<>();

        //list.add(Integer.valueOf(1)); //Error
        //list.add(Double.valueOf(1.0));//Error

        //Integer value = list.get(0);//Error
        //Number number = list.get(0);//Error
        Object object = list.get(0);//OK
    }

    /**
     * 如果你希望这个集合，既能获取值，又能插入值，那么请不要使用泛型的上下限界定符
     */
    public void normal(){
        List<Integer> list = new ArrayList<>();
        list.add(1);//OK
        list.get(0);//Ok
    }
}
