package socket.nio.vNetty.group;

import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.loop.RayEventLoop;

import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class RayEventLoopGroup {

    private RayEventLoop[] children = new RayEventLoop[16];

    private AtomicInteger idx = new AtomicInteger(0);

    public RayEventLoopGroup() throws Exception {
        for (int i = 0; i < children.length; i++) {
            children[i] = new RayEventLoop(this);
        }
    }

    public RayEventLoop next(){
        return children[idx.getAndIncrement() & children.length-1];
    }

    public void register(RayChannel channel){
        next().register(channel);
    }
}
