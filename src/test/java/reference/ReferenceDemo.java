package reference;

import org.junit.jupiter.api.Test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceDemo {



    public void StrongReference(){
        String str = "abc";

        str=null;
    }

    @Test
    public void softReference(){

        String str = "abc";
        ReferenceQueue<Object> queue = new ReferenceQueue<>();

        SoftReference<String> reference = new SoftReference<>(str,queue);

        str = null;

        System.out.println(reference.get());
        Reference<?> ref = queue.poll();
        System.out.println(ref);

    }

    @Test
    public void weakReference(){
        byte[][] bytes = new byte[100][];
        String str = "abc";
        WeakReference<String> weakReference = new WeakReference<>(str);
        str = null;

        System.gc();

        for(int i =0;i<100;i++){
            byte[] b = new byte[1024*1024*1024];

            if(weakReference.get()==null){
                System.out.println("recycled....");
            }
        }
    }


}
