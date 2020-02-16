package spring.classpath;

import java.net.URL;
import java.util.Enumeration;

public class ClassPathDemo {

    public static void main(String[] args) throws Exception{
        ClassPathDemo classPathDemo = new ClassPathDemo();
        classPathDemo.getResource("common/");
    }


    public void getResource(String rootDir) throws Exception{
        Enumeration<URL> resources = this.getClass().getClassLoader().getResources(rootDir);

        while(resources.hasMoreElements()){
            URL url = resources.nextElement();
            System.out.println(url);
        }
    }
}
