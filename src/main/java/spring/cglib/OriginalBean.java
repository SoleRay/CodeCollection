package spring.cglib;

import org.springframework.context.annotation.Bean;

public class OriginalBean {

    @Bean
    public void fly(){
        System.out.println("I'm fly...");
    }

    public void eat(){
        System.out.println("I'm eat...");
    }

    public static void main(String[] args) {
        Object proxy = CglibProxyCreator.createProxy(CglibProxyCreator.newEnhancer(OriginalBean.class));
        OriginalBean bean = OriginalBean.class.cast(proxy);
        bean.eat();
    }
}
