package synthetic;

public class Main {

    private static class Inner {
    }
    static void checkSynthetic (String name) {
        try {
            System.out.println (name + " : " + Class.forName (name).isSynthetic ());
        } catch (ClassNotFoundException exc) {
            exc.printStackTrace (System.out);
        }
    }
    public static void main(String[] args) throws Exception
    {
        new Inner ();
        checkSynthetic ("synthetic.Main");
        checkSynthetic ("synthetic.Main$Inner");
        checkSynthetic ("synthetic.Main$1");
    }
}

class Main$1 {
}