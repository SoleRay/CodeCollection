package designmode.singleton;

public class S3 {

    private S3(){}

    private static class S3Inner{
        private static final S3 INSTANCE = new S3();
    }

    public static S3 getInstance(){
        return S3Inner.INSTANCE;
    }
}
