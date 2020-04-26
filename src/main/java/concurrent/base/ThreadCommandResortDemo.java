package concurrent.base;

public class ThreadCommandResortDemo {

    private boolean flag;

    private volatile int a=1;

    public void fly(){
        a = 2;
        flag = true;
    }

    public void eat(){

        if(flag && a==1){
            System.out.println("a=" + a);
        }
    }


    public static void main(String[] args) throws Exception {
        ThreadCommandResortDemo demo = new ThreadCommandResortDemo();
        for(int i=0;i<100000;i++){
            Thread t1 = new Thread(() -> {
                demo.fly();
            });

            Thread t2 = new Thread(() -> {
                demo.eat();
            });


            t1.start();
            t2.start();

            t1.join();
            t2.join();
        }

    }
}
