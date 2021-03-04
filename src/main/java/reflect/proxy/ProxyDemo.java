package reflect.proxy;

import reflect.proxy.box.Box;
import reflect.proxy.common.Steal;
import reflect.proxy.gun.BigGun;
import reflect.proxy.gun.Gun;
import reflect.proxy.gun.GunProxy;

import java.lang.reflect.Proxy;

public class ProxyDemo {

    public static void main(String[] args) {
//        testGun();
//        testBox();

        testTinyBox();
    }

    private static void testTinyBox() {
        TinyBox tinyBox = new TinyBox();
        tinyBox.taste();
    }

    private static void testBox() {
        Box box = new Box();
        box.createProxy();
//        Steal proxy =
//        proxy.make();
    }

    private static void testGun() {
        BigGun bigGun = new BigGun(2);
        Object o = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{Gun.class}, new GunProxy(bigGun));
        Gun gun = (Gun) o;
        gun.shoot();
    }
}
