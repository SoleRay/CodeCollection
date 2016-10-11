package xml;

import org.apache.catalina.Server;
import org.apache.tomcat.util.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.MalformedURLException;

/**
 * Created by Arthur on 2016/10/10 0010.
 */
public class DigesterDemo {

    private Server server;

    public static void main(String[] args) throws FileNotFoundException, MalformedURLException {
        DigesterDemo digesterDemo = new DigesterDemo();
        digesterDemo.testDigester();
    }

    private void testDigester(){

        try {
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.setRulesValidation(true);
            digester.addObjectCreate("Server",
                    "org.apache.catalina.core.StandardServer",
                    "className");
            digester.addSetNext("Server",
                    "setServer",
                    "org.apache.catalina.Server");
            File file = new File("E:\\Servers\\apache-tomcat-9.0.0.M10\\conf\\server.xml");
            InputStream inputStream = new FileInputStream(file);
            InputSource inputSource = new InputSource(file.toURI().toURL().toString());
            inputSource.setByteStream(inputStream);
            digester.push(this);
            digester.parse(inputSource);
            System.out.println(this.server.getPort());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
