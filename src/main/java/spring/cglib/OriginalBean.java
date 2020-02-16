package spring.cglib;

import org.springframework.context.annotation.Bean;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class OriginalBean {

    @Bean
    public void fly(){
        System.out.println("I'm fly...");
    }

    public void eat(){
        System.out.println("I'm eat...");
    }

    public void init(){}

    public Object getObject(){
        return null;
    }

    public static void main(String[] args) throws Exception {
        Object proxy = CglibProxyCreator.createProxy(CglibProxyCreator.newEnhancer(OriginalBean.class, ClassUtils.getDefaultClassLoader()));

        if(CglibProxyCreator.EnhancedConfiguration.class.isInstance(proxy)){
            CglibProxyCreator.EnhancedConfiguration enhancedConfiguration = CglibProxyCreator.EnhancedConfiguration.class.cast(proxy);
            enhancedConfiguration.setName("john");
        }
        Class c = CglibProxyCreator.enhanceClass(CglibProxyCreator.newEnhancer(OriginalBean.class,ClassUtils.getDefaultClassLoader()));
        Field field = c.getField("$$name");
        System.out.println(field.get(proxy));
    }
}
