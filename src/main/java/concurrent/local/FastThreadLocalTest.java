package concurrent.local;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

public class FastThreadLocalTest {

    InnerOne innerOne = new InnerOne();
    InnerTwo innerTwo = new InnerTwo();

    public void test(){

        new FastThreadLocalThread(()->{
           innerOne.hello();
           innerTwo.hello();
        });
    }

    public static void main(String[] args) {
        FastThreadLocalTest test = new FastThreadLocalTest();
//        test.test();
    }

    class InnerOne {
        FastThreadLocal<String> threadLocal = new FastThreadLocal<>();

        public void hello(){
            threadLocal.set("box");
        }
    }

    class InnerTwo {
        FastThreadLocal<String> threadLocal = new FastThreadLocal<>();

        public void hello(){
            threadLocal.set("box");
        }
    }

}
