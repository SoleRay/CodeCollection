package socket.nio.vNetty.channel;

import socket.nio.vNetty.loop.RayEventLoop;
import socket.nio.vNetty.pipeline.RayChannelPipeline;

import java.io.IOException;

public interface RayChannel {

    void read() throws IOException;

    void write() throws IOException;

    void register(RayEventLoop eventLoop);

    RayEventLoop eventLoop();

    RayChannelPipeline pipeline();
}
