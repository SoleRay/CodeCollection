package concurrent.base;

import org.junit.jupiter.api.Test;

public class ThreadDeamon {

    public static void doRun(int num){

        for(int i=0;i<num;i++){

            System.out.println(Thread.currentThread().getName()+":running....");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        T1 t1 = new T1();
        T2 t2 = new T2();
        TDeamon tDeamon = new TDeamon();
        t1.setName("T1");
        t2.setName("T2");
        tDeamon.setName("TDeamon");
        tDeamon.setDaemon(true);

        t1.start();
        t2.start();
        tDeamon.start();

        System.out.println("main:over....");
    }




    static class T1 extends Thread{

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+":over....");
        }
    }

    static class T2 extends Thread{

        @Override
        public void run() {
            doRun(5);
        }
    }

    static class TDeamon extends Thread{

        @Override
        public void run() {
            doRun(9999);
        }
    }
}
