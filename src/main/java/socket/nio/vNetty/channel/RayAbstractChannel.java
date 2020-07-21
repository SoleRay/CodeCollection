package socket.nio.vNetty.channel;

import socket.nio.vNetty.loop.RayEventLoop;
import socket.nio.vNetty.pipeline.RayChannelPipeline;
import socket.nio.vNetty.pipeline.RayDefaultChannelPipeline;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class RayAbstractChannel implements RayChannel{

    protected RayEventLoop eventLoop;

    protected SelectableChannel channel;

    protected SelectionKey selectionKey;

    protected int interestOps;

    protected Queue<ByteBuffer> writeQueue;

    private RayChannelPipeline pipeline;

    public RayAbstractChannel(SelectableChannel channel,int interestOps) {
        this.channel = channel;
        this.interestOps = interestOps;
        writeQueue = new LinkedBlockingQueue<>();
        pipeline = new RayDefaultChannelPipeline(this);
    }

    @Override
    public void register(RayEventLoop eventLoop){
        this.eventLoop = eventLoop;
        try {
            selectionKey = channel.register(eventLoop.selector(), interestOps,this);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }

    public SelectableChannel javaChannel(){
        return channel;
    }

    @Override
    public RayEventLoop eventLoop() {
        return eventLoop;
    }

    @Override
    public abstract void read() throws IOException;

    @Override
    public abstract void write() throws IOException;

    @Override
    public RayChannelPipeline pipeline() {
        return pipeline;
    }
}
