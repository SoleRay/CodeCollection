package reflect.proxy.gun;

public interface Gun {

    default boolean shoot(){
        System.out.println("shoot!!!");
        return false;
    }

}
