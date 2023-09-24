package unsafe;

import io.netty.util.internal.ReflectionUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class OperMain {


    public static void main(String[] args) throws Exception{
        int javaLevel = 11;

        SmallBox smallBox = new SmallBox();
        ArraySet arraySet = new ArraySet();
        Class<SmallBox> smallBoxClass = SmallBox.class;

        Field keysField = smallBoxClass.getDeclaredField("keys");
        keysField.setAccessible(true);


        if(javaLevel > 9){
            //java9以上的版本，使用unsafe来修改对象属性
            Unsafe unsafe = getUnsafe();
            long keysFieldOffset = unsafe.objectFieldOffset(keysField);
            unsafe.putObject(smallBox,keysFieldOffset,arraySet);
        }else {
            //java9之前的版本，使用set来修改对象属性
            keysField.set(smallBox,arraySet);
        }

        smallBox.getKeys().add("1");
        smallBox.getKeys().add("2");
        smallBox.getKeys().add("3");
    }

    public static Unsafe getUnsafe(){
        final Object maybeUnsafe = AccessController.doPrivileged(new PrivilegedAction<Object>() {
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

        return ((Unsafe) maybeUnsafe);
    }
}
