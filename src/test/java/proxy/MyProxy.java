package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyProxy implements InvocationHandler {

    private Object target;

    public MyProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals("eat")){
            System.out.println("will cookie sth");
            System.out.println("wash the bowl");
        }else if(method.getName().equals("sleep")){
            System.out.println("tidy the bedroom");
            System.out.println("make love...");
            method.invoke(target,args);
        }

        return null;
    }
}
