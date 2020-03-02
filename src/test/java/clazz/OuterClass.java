package clazz;

public class OuterClass {

    public void execute(){

        Runnable r1 = new Runnable() {

            @Override
            public void run() {
                System.out.println("r1线程this:"+this);
            }
        };

        Runnable r2 = () -> {
            System.out.println("r2线程this:"+this);
        };

        r1.run();
        r2.run();
        System.out.println("主线程this:"+this);
    }

    public static void main(String[] args) {
        OuterClass outerClass = new OuterClass();
        outerClass.execute();
    }
}
