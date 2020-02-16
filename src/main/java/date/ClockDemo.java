package date;

import math.Sum;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.time.Clock;
import java.time.ZoneId;

public class ClockDemo {

    @Test
    public void calTimeCost(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Sum.sumRange(1,500000000);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeSeconds());
    }
}
