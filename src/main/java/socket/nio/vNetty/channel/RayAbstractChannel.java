package socket.nio.vNetty.channel;

import socket.nio.vNetty.loop.RayEventLoop;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class RayAbstractChannel implements RayChannel{

    private RayEventLoop eventLoop;

    private SelectableChannel channel;

    protected SelectionKey selectionKey;

    protected int interestOps;

    protected Queue<ByteBuffer> writeQueue;

    public RayAbstractChannel(SelectableChannel channel,int interestOps) {
        this.channel = channel;
        this.interestOps = interestOps;
        writeQueue = new LinkedBlockingQueue<>();
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

    public RayEventLoop eventLoop() {
        return eventLoop;
    }

    @Override
    public abstract void read() throws IOException;

    @Override
    public abstract void write() throws IOException;
}
