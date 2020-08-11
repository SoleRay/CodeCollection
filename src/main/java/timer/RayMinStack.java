package timer;

public class RayMinStack {

    private Integer[] queue = new Integer[16];

    private int size;

    public void add(int num) {
        queue[++size] = Integer.valueOf(num);
        fixUp(size);
    }

    private void fixUp(int k) {
        for (int j = k >> 1; j > 0; j = j >> 1) {
            if (queue[k].intValue() > queue[j].intValue()) {
                break;
            }
            swap(k, j);
            k = j;
        }
    }

    public void removeMin() {
        queue[1] = queue[size];
        queue[size--] = null;
        fixDown(1);
    }

    private void fixDown(int k) {
        int j;
        while (( j = k << 1) <= size) {
            if(queue[j].intValue() > queue[j+1].intValue()){
                j++;
            }
            if(queue[k].intValue() <= queue[j].intValue()){
                break;
            }
            swap(k, j);
            k = j;
        }


    }

    private void swap(int k, int j) {
        Integer tmp = queue[j];
        queue[j] = queue[k];
        queue[k] = tmp;
    }

    public Integer getMin() {
        return queue[1];
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public static void main(String[] args) {
        RayMinStack minStack = new RayMinStack();
        init(minStack);
        minStack.add(4);
//        recursivePrint(minStack,1);
        while (!minStack.isEmpty()){
            minStack.removeMin();
            System.out.println(minStack.getMin().intValue());
//            recursivePrint(minStack,1);
        }
    }

    private static void recursivePrint(RayMinStack minStack, int loop) {

        int nextLoop = loop << 1;
        for(int i = loop; i< nextLoop; i++){
            System.out.print(minStack.queue[i]);
            if(i== nextLoop -1){
                System.out.println();
            }else if(i==minStack.size){
                System.out.println();
                break;
            }else {
                System.out.print(",");
            }
        }
        if(nextLoop<=minStack.size){
            recursivePrint(minStack, nextLoop);
        }

    }

    private static void init(RayMinStack minStack) {
        minStack.add(45);
        minStack.add(30);
        minStack.add(10);
        minStack.add(56);
        minStack.add(13);
        minStack.add(35);
        minStack.add(21);
        minStack.add(22);
        minStack.add(8);
        minStack.add(18);
        minStack.add(5);
    }

//    private static void init(RayMinStack minStack) {
//        minStack.add(5);
//        minStack.add(8);
//        minStack.add(10);
//        minStack.add(13);
//        minStack.add(45);
//        minStack.add(22);
//        minStack.add(30);
//        minStack.add(18);
//    }
}
