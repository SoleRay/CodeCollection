package proxy;

import java.lang.reflect.Proxy;

public class ProxyTest {

    public static void main(String[] args) {
        Man man = (Man) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), Bob.class.getInterfaces(), new MyProxy(new Bob()));
        man.eat();
        man.sleep();
    }
}
