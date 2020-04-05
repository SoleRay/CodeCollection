package socket.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

public class ClientOutboundHandler extends ChannelOutboundHandlerAdapter {

    private void print(ChannelHandlerContext ctx){
        String methodName  = Thread.currentThread().getStackTrace()[2].getMethodName();
        System.out.println( methodName+"——"+ctx.channel().localAddress());
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        print(ctx);
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        print(ctx);
        super.write(ctx, msg, promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        print(ctx);
        super.read(ctx);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        print(ctx);
        super.flush(ctx);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        print(ctx);
        super.disconnect(ctx, promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        print(ctx);
        super.close(ctx, promise);
    }
}
