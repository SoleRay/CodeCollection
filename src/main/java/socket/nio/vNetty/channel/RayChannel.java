package socket.nio.vNetty.channel;

import socket.nio.vNetty.loop.RayEventLoop;

import java.io.IOException;

public interface RayChannel {

    void read() throws IOException;

    void write() throws IOException;

    void register(RayEventLoop eventLoop);
}
