package timer;

public abstract class RayTimerTask implements Runnable {


    long nextExecuteTime;

    long period;

    private String name;

    public RayTimerTask(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public abstract void run();

}
