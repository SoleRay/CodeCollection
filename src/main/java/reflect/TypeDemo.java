package reflect;

import collection.CollectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeDemo {

    public static void main(String[] args) {
        Field[] fields = MultipleBean.class.getDeclaredFields();

        List<Map<String,String>> list = new ArrayList<>();


        for(Field field : fields){

            Map<String,String> map = new HashMap<>();
            Type genericType = field.getGenericType();

            if(ParameterizedType.class.isInstance(genericType)){
                ParameterizedType paramType = ParameterizedType.class.cast(genericType);
                processParameterizedType(paramType,map);
            }else {
                String typeName = genericType.getClass().getSimpleName();
                map.put(typeName,typeName);
            }

            list.add(map);
        }

        System.out.println();
    }

    private static void processParameterizedType(ParameterizedType paramType, Map<String, String> map) {

        Type[] actualTypes = paramType.getActualTypeArguments();

        for(Type actualType : actualTypes){
            map.put(paramType.getClass().getSimpleName(),actualType.getClass().getSimpleName());
        }

    }
}
