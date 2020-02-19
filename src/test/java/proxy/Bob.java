package proxy;


public class Bob implements Man,Woman {

    @Override
    public int eat(){
        System.out.println("bob start to eat...");
        return 0;
    }

    @Override
    public void sleep(){
        System.out.println("bob start to sleep");
    }

    @Override
    public void makeUp() {

    }

    @Override
    public Bird findBird() {
        return null;
    }
}
