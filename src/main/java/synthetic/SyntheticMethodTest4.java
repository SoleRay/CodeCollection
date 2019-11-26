package synthetic;

import java.util.LinkedList;
import java.util.List;

public class SyntheticMethodTest4 {
 
    public static class MyLink extends LinkedList<String> {
        @Override
        public boolean add(String s) {
            return true;
        }
    }
 
    public static void main(String[] args) {
        List a = new MyLink();
        a.add("");
        a.add(13);
    }
} 