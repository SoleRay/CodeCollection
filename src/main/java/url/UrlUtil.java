package url;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Arthur on 2016/10/8 0008.
 */
public class UrlUtil {

    protected static final int URI_TYPE_TCP = 0;
    protected static final int URI_TYPE_SSL = 1;
    protected static final int URI_TYPE_LOCAL = 2;

    //if necessary add http
    //protected static final int URI_TYPE_HTTP = 3;


    protected static int validateURI(String srvURI) {
        try {
            URI vURI = new URI(srvURI);
            if (!vURI.getPath().equals("")) {
                throw new IllegalArgumentException(srvURI);
            }
            if (vURI.getScheme().equals("tcp")) {
                return URI_TYPE_TCP;
            }
            else if (vURI.getScheme().equals("ssl")) {
                return URI_TYPE_SSL;
            }
            else if (vURI.getScheme().equals("local")) {
                return URI_TYPE_LOCAL;
            }
            else {
                throw new IllegalArgumentException(srvURI);
            }
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(srvURI);
        }
    }

    /**
     * URI also has the method getPort
     * But that method don't have defaultPort
     */

    private int getPort(String uri, int defaultPort) {
        int port;
        int portIndex = uri.lastIndexOf(':');
        if (portIndex == -1) {
            port = defaultPort;
        }
        else {
            port = Integer.parseInt(uri.substring(portIndex + 1));
        }
        return port;
    }
}
