package reflect.proxy.gun;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class GunProxy implements InvocationHandler {

    private BigGun bigGun;

    public GunProxy(BigGun bigGun) {
        this.bigGun = bigGun;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        locate();
        return method.invoke(bigGun,args);
//        FileOutputStream fos = new FileOutputStream("hello.class");
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(proxy.getClass());
    }

    private void locate(){
        System.out.println("locate enemy！！！");
    }
}
