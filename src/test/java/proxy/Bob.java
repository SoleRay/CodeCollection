package proxy;


public class Bob implements Man {

    @Override
    public void eat(){
        System.out.println("bob start to eat...");
    }

    @Override
    public void sleep(){
        System.out.println("bob start to sleep");
    }
}
