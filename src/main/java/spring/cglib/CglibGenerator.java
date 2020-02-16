package spring.cglib;

import org.springframework.cglib.beans.BeanGenerator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CglibGenerator {


    private Object generateBean(Map propertyMap) {
        BeanGenerator generator = new BeanGenerator();
        Set keySet = propertyMap.keySet();
        for (Iterator i = keySet.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            generator.addProperty(key, (Class) propertyMap.get(key));
        }
        return generator.create();
    }

    public static void main(String[] args) throws Exception{
        HashMap propertyMap = new HashMap();

        propertyMap.put("id", Class.forName("java.lang.Integer"));

        propertyMap.put("name", Class.forName("java.lang.String"));

        propertyMap.put("address", Class.forName("java.lang.String"));

        CglibGenerator c = new CglibGenerator();
        Object o = c.generateBean(propertyMap);
        Class clazz = o.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            System.out.println(methods[i].getName());
        }


    }
}
