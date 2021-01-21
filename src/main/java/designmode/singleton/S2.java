package designmode.singleton;

public class S2 {

    private static volatile S2 INSTANCE;

    private S2(){}

    public static S2 getInstance(){
        if (INSTANCE == null) {
            synchronized (S2.class){
                if (INSTANCE == null) {
                    INSTANCE = new S2();
                }
            }
        }
        return INSTANCE;
    }
}
