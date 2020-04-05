package concurrent.base;

import org.springframework.util.StopWatch;

public class SynchronizedDemo {


    public static void useStringBuffer(StringBuffer buffer){
        buffer.append("a");
    }

    public static void useStringBuilder(StringBuilder builder){
        builder.append("a");
    }

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i=0;i<1000000000;i++){
            useStringBuffer(new StringBuffer());
//            useStringBuilder(new StringBuilder());
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
