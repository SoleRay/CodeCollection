package timer;

class RayTaskQueue {

    private RayTimerTask[] queue = new RayTimerTask[16];

    private int size;

    public RayTimerTask getMin() {
        return queue[1];
    }

    public void add(RayTimerTask task) {
        queue[++size] = task;
        fixUp(size);
    }


    public void reschedule(long newTime) {
        queue[1].nextExecuteTime = newTime;
        fixDown(1);
    }

    public void removeMin() {
        queue[1] = queue[size];
        queue[size--] = null;
        fixDown(1);
    }

    private void fixUp(int k) {
        for (int j = k >> 1; j > 0; j = j >> 1) {
            if (queue[k].nextExecuteTime >= queue[j].nextExecuteTime) {
                break;
            }
            swap(k, j);
            k = j;
        }

    }



    private void fixDown(int k) {
        int j;
        while ((j = k << 1) <= size) {
            if (j < size && queue[j].nextExecuteTime > queue[j + 1].nextExecuteTime) {
                j++;
            }
            if (queue[k].nextExecuteTime <= queue[j].nextExecuteTime) {
                break;
            }
            swap(k, j);
            k = j;
        }


    }

    private void swap(int k, int j) {
        RayTimerTask tmp = queue[j];
        queue[j] = queue[k];
        queue[k] = tmp;
    }



    public boolean isEmpty() {
        return size == 0;
    }


}