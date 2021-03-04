package random;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

    public static LocalDateTime randomLocalDataTime(){
        return LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(20000));
    }
}
