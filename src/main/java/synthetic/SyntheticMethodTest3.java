package synthetic;

import java.lang.reflect.Method;
import java.util.LinkedList;

public class SyntheticMethodTest3 {
 
    public static class MyLink extends LinkedList<String> {
        @Override
        public String get(int i) {
            return "";
        }
    }
 
    public static void main(String[] args) {
 
        for (Method m : MyLink.class.getDeclaredMethods()) {
            System.out.println(String.format("%08X", m.getModifiers()) + " " + m.getReturnType().getSimpleName() + " " + m.getName());
        }
    }
}