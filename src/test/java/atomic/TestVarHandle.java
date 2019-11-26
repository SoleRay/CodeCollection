package atomic;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class TestVarHandle {

    private int score;

    class Point{

        int x;

        int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public void test(){
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            Point p = new Point();

            VarHandle x = lookup.findVarHandle(Point.class, "x", int.class);
            VarHandle y = lookup.findVarHandle(Point.class, "y", int.class);

            VarHandle s = lookup.findVarHandle(TestVarHandle.class, "score", int.class);

//            if(s.compareAndSet(this, 0, 1)){
//                System.out.println("score="+score);
//            }

            x.compareAndSet(p,0,1);
            System.out.println(p.x);


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestVarHandle t = new TestVarHandle();
        t.test();

    }
}
