package reflect.proxy.box;

import reflect.proxy.common.Steal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Box {

    protected interface Wood{

        void book();
    }

    private class NewWood implements Wood, Steal {

        @Override
        public void book() {
            System.out.println("book wood");
        }

        @Override
        public void make() {
            System.out.println("make steal");
        }
    }

    private class WoodProxy implements InvocationHandler{

        private NewWood newWood;

        public WoodProxy(NewWood newWood) {
            this.newWood = newWood;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            cleanWood();
            return method.invoke(newWood,args);
        }

        private void cleanWood(){
            System.out.println("clean wood!!!!");
        }
    }

    public Wood createProxy(){
        InvocationHandler h = new WoodProxy(new NewWood());
        Object o = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{Wood.class,Steal.class}, h);
        Wood wood = (Wood) o;
        return wood;
    }

}
