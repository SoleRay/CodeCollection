package bridge;

import java.lang.reflect.Method;

public class TestBridge {

    public static void main(String[] args) {

        Method[] declaredMethods = SubClass.class.getDeclaredMethods();
        for(Method method : declaredMethods){
            System.out.println(method.getReturnType());
        }
    }
}
