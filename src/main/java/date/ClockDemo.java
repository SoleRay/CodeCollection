package date;

import java.time.Clock;
import java.time.ZoneId;

public class ClockDemo {

    public void calTimeCost(){
        Clock clock = Clock.tickSeconds(ZoneId.systemDefault());
        clock.millis();
    }
}
