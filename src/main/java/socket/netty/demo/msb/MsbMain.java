package socket.netty.demo.msb;

import socket.netty.demo.msb.server.MsbServer;

public class MsbMain {

    public static void main(String[] args) {
        MsbServer msbServer = new MsbServer();
        msbServer.childHandler(new MsbInHandler());
        msbServer.bind("192.168.0.187",9090);
    }
}
