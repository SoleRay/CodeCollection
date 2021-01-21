package designmode.singleton;

public class S1 {

    private static final S1 INSTANCE = new S1();



    private S1(){

    }

    public static S1 getInstance(){
        return INSTANCE;
    }
}
