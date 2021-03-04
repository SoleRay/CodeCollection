package reflect.proxy;

import reflect.proxy.box.Box;

public class TinyBox extends Box {

    public void taste(){
        Wood wood = createProxy();
        wood.book();
    }
}
