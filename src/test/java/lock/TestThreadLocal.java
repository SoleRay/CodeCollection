package lock;

public class TestThreadLocal {

    private final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public void test(){
        threadLocal.set("box");
        threadLocal.set("name");
        threadLocal.set("dog");
        threadLocal.set("json");
    }

    public static void main(String[] args) {
        TestThreadLocal t = new TestThreadLocal();
        t.test();
    }
}
