package designmode.singleton;

public class TestMain {

    public static void main(String[] args) {
        try {
            Class.forName("designmode.singleton.S4");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
