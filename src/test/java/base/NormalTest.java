package base;

import org.junit.jupiter.api.Test;

public class NormalTest {

    @Test
    public void testInteger(){
        Integer integer = Integer.valueOf(5);
        System.out.println(integer.equals(5));
    }
}
