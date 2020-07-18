package socket.nio.vNetty.pipeline;

import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.pipeline.context.RayAbstractChannelHandlerContext;
import socket.nio.vNetty.pipeline.context.RayDefaultChannelHandlerContext;
import socket.nio.vNetty.pipeline.handler.RayChannelHandler;

public class RayDefaultChannelPipeline implements RayChannelPipeline {

    private RayChannel channel;

    RayAbstractChannelHandlerContext head;

    RayAbstractChannelHandlerContext tail;

    public RayDefaultChannelPipeline(RayChannel channel) {
        this.channel = channel;
        this.head = new RayHeadContext(this);
        this.tail = new RayTailContext(this);

        head.setNext(tail);
        tail.setPrev(head);
    }

    @Override
    public void addLast(RayChannelHandler handler){
        RayDefaultChannelHandlerContext handlerContext = new RayDefaultChannelHandlerContext(this, handler);
        RayAbstractChannelHandlerContext prev = tail.prev();
        prev.setNext(handlerContext);
        handlerContext.setPrev(prev);
        handlerContext.setNext(tail);
        tail.setPrev(handlerContext);
    }

    @Override
    public void fireChannelRead(Object msg) {
        head.fireChannelRead(msg);
    }

    class RayHeadContext extends RayAbstractChannelHandlerContext  {

        public RayHeadContext(RayChannelPipeline pipeline) {
            super(pipeline, new RayPipelineChannelHandler());
        }
    }

    class RayTailContext extends RayAbstractChannelHandlerContext {

        public RayTailContext(RayChannelPipeline pipeline) {
            super(pipeline, new RayPipelineChannelHandler());
        }
    }

    class RayPipelineChannelHandler implements RayChannelHandler{

        @Override
        public void channelRead(RayAbstractChannelHandlerContext ctx, Object msg) {
            ctx.fireChannelRead(msg);
        }
    }
}
