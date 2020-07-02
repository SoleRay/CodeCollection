package socket.netty.unsafe;

import io.netty.util.internal.ReflectionUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class UnsafeDemo {

//    private static Unsafe unsafe = getUnsafe();

    private static Unsafe unsafe = Unsafe.getUnsafe();

    public static void main(String[] args) {
        long address = unsafe.allocateMemory(10);

        unsafe.putByte(address, (byte) 0);
        unsafe.putByte(address+1, (byte) 1);
        unsafe.putByte(address+2, (byte) 2);

        System.out.println(unsafe.getByte(address));
        System.out.println(unsafe.getByte(address+1));
        System.out.println(unsafe.getByte(address+2));
        System.out.println(unsafe.getByte(address+3));//没设置过，为0

        unsafe.freeMemory(address);
    }




    private static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return  (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Unsafe getUnsafeDoPrivileged() {
        Object unsafe = AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                    // We always want to try using Unsafe as the access still works on java9 as well and
                    // we need it for out native-transports and many optimizations.
                    Throwable cause = ReflectionUtil.trySetAccessible(unsafeField, false);
                    if (cause != null) {
                        return cause;
                    }
                    // the unsafe instance
                    return unsafeField.get(null);
                } catch (NoSuchFieldException e) {
                    return e;
                } catch (SecurityException e) {
                    return e;
                } catch (IllegalAccessException e) {
                    return e;
                } catch (NoClassDefFoundError e) {
                    // Also catch NoClassDefFoundError in case someone uses for example OSGI and it made
                    // Unsafe unloadable.
                    return e;
                }
            }
        });

        return (Unsafe) unsafe;
    }
}
