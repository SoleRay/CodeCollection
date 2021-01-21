package base;

import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NormalTest {

    private static final Logger log = LoggerFactory.getLogger(NormalTest.class);

//    private static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Test
    public void testInteger(){
        log.info("hello {}","world");
        Integer integer = Integer.valueOf(5);
        System.out.println(integer.equals(5));
    }
}
