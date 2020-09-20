package concurrent.queue;


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayBean<T> implements Delayed {

    private String id;

    private String name;

    private T data;

    private long activeTime;

    public DelayBean(String id, String name, T data, long period) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.activeTime = System.nanoTime() +
                TimeUnit.NANOSECONDS.convert(period, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(activeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long d = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return (d == 0) ? 0 : d > 0 ? 1 : -1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }
}
