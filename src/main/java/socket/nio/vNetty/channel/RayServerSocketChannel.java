package socket.nio.vNetty.channel;

import socket.nio.vNetty.loop.RayEventLoop;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

public class RayServerSocketChannel extends RayAbstractChannel {

    public RayServerSocketChannel(SelectableChannel channel, int ops) {
        super(channel,ops);
    }

    @Override
    public void read() throws IOException {

    }

    @Override
    public void write() throws IOException {

    }
}
