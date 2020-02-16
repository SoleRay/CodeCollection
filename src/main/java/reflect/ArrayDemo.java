package reflect;

import reflect.bean.Ferrari;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;

public class ArrayDemo {

    public static void main(String[] args) throws Exception{
        BeanInfo beanInfo = Introspector.getBeanInfo(Ferrari.class);
        PropertyDescriptor[] ps = beanInfo.getPropertyDescriptors();
        Class<?> propertyType = ps[3].getPropertyType();
        Class<?> componentType = getComponentType(propertyType);
        Object arrayInstance = createArrayInstance(componentType);
        System.out.println(arrayInstance);
    }

    /**
     *
     * 返回数组的真实类型
     * 例如：String[] a = new String[5];
     * Class realType = a.getClass().getComponentType()
     *
     * realType的值就是String类型
     *
     */
    public static Class<?> getComponentType(Class arrayClass){
        return arrayClass.getComponentType();
    }

    public static Object createArrayInstance(Class componentType){
        return Array.newInstance(componentType,1);
    }
}
