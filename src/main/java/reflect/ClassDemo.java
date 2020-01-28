package reflect;

import org.springframework.util.ClassUtils;

import java.net.URI;
import java.net.URL;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClassDemo {

    public static void main(String[] args) {
        /** 临时变量中的泛型类型无法获取 */
        List<String> list = new ArrayList<>();

    }


    /**
     *
     * 返回数组的真实类型
     * 例如：String[] a = new String[5];
     * Class realType = a.getClass().getComponentType()
     *
     * realType的值就是String类型
     *
     */
    public static Class<?> getComponentType(Class arrayClass){
        return arrayClass.getComponentType();
    }

    public static boolean isSimpleValueType(Class<?> type) {
        return (Void.class != type && void.class != type &&
                (ClassUtils.isPrimitiveOrWrapper(type) ||
                        Enum.class.isAssignableFrom(type) ||
                        CharSequence.class.isAssignableFrom(type) ||
                        Number.class.isAssignableFrom(type) ||
                        Date.class.isAssignableFrom(type) ||
                        Temporal.class.isAssignableFrom(type) ||
                        URI.class == type ||
                        URL.class == type ||
                        Locale.class == type ||
                        Class.class == type));
    }
}
