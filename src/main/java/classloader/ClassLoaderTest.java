package classloader;

import reflect.proxy.gun.GunProxy;

public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
//        System.out.println(GunProxy.class.getClassLoader());
        Class<?> aClass = Class.forName("classloader.ClassLoaderTest");
        System.out.println(aClass.getClassLoader());
        Thread.currentThread().getContextClassLoader();
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);
    }
}
