package reflect;

import reflect.bean.CarProcessor;
import reflect.bean.Ferrari;

import java.beans.*;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BeanInfoDemo {

    public static void main(String[] args) throws Exception {
        processMethodDescriptor(CarProcessor.class);

    }

    public static void processMethodDescriptor(Class<?> c) throws Exception{
        BeanInfo beanInfo = Introspector.getBeanInfo(c);
        MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
        Method method = methodDescriptors[1].getMethod();
        Type type = method.getGenericParameterTypes()[0];
        if(ParameterizedType.class.isInstance(type)){
            ParameterizedType pType = ParameterizedType.class.cast(type);
            System.out.println(pType.getActualTypeArguments()[0].getTypeName());
        }
    }

    public BeanInfo getBeanInfo(Class<?> c) throws Exception{
        BeanInfo beanInfo = Introspector.getBeanInfo(c);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
        BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
        return beanInfo;
    }


}
