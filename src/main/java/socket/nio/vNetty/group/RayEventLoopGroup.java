package socket.nio.vNetty.group;

import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.loop.RayEventLoop;

import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class RayEventLoopGroup {

    private RayEventLoop[] children;

    private AtomicInteger idx = new AtomicInteger(0);

    private int nThreads;

    private String eventLoopGroupName = "EventLoopGroup";

    private String eventLoopNamePrefix = "EventLoop";

    public RayEventLoopGroup() throws Exception {
        this(16);
    }

    public RayEventLoopGroup(int nThreads) throws Exception {
        this(nThreads,null,null);
    }

    public RayEventLoopGroup(String eventLoopGroupName) throws Exception {
        this(16,eventLoopGroupName,null);
    }

    public RayEventLoopGroup(String eventLoopGroupName,String eventLoopNamePrefix) throws Exception {
        this(16,eventLoopGroupName,eventLoopNamePrefix);
    }

    public RayEventLoopGroup(int nThreads,String eventLoopGroupName,String eventLoopNamePrefix) throws Exception {
        this.eventLoopGroupName = eventLoopGroupName==null?this.eventLoopGroupName:eventLoopGroupName;
        this.eventLoopNamePrefix = eventLoopNamePrefix==null?this.eventLoopNamePrefix:eventLoopNamePrefix;
        this.nThreads = nThreads;
        children = new RayEventLoop[nThreads];
        for (int i = 0; i < children.length; i++) {
            children[i] = new RayEventLoop(this,eventLoopNamePrefix);
        }
    }

    public RayEventLoop next(){
        return children[idx.getAndIncrement() & children.length-1];
    }

    public void register(RayChannel channel){
        System.out.println(eventLoopGroupName+":正在选取eventLoop对channel进行注册....");
        next().register(channel);
    }
}
