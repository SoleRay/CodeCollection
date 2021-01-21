package designmode.singleton;

/**
 * 不仅可以解决线程同步，还可以防止反序列化
 */
public enum S4 {

    INSTACNE;
    static {
        System.out.println("hello");
    }
}
