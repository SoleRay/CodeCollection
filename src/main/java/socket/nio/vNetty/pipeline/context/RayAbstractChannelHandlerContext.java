package socket.nio.vNetty.pipeline.context;

import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.pipeline.RayChannelPipeline;
import socket.nio.vNetty.pipeline.handler.RayChannelHandler;
import socket.nio.vNetty.pipeline.handler.RayChannelInboundHandler;

public abstract class RayAbstractChannelHandlerContext implements RayChannelHandlerContext {

    private RayChannelHandler handler;

    private RayChannelPipeline pipeline;

    RayAbstractChannelHandlerContext prev;

    RayAbstractChannelHandlerContext next;

    public RayAbstractChannelHandlerContext(RayChannelPipeline pipeline,RayChannelHandler handler){
        this.pipeline = pipeline;
        this.handler = handler;
    }

    @Override
    public void fireChannelRead(Object msg){
        if(next != null && next.handler !=null ){
            ((RayChannelInboundHandler) next.handler).channelRead(next,msg);
        }
    }

    @Override
    public RayChannel channel() {
        return pipeline.channel();
    }

    @Override
    public RayChannelPipeline pipeline() {
        return pipeline;
    }

    @Override
    public RayChannelHandler handler() {
        return handler;
    }

    public void setNext(RayAbstractChannelHandlerContext context){
        next = context;
    }

    @Override
    public RayAbstractChannelHandlerContext next(){
        return next;
    }

    public void setPrev(RayAbstractChannelHandlerContext context){
        prev = context;
    }

    @Override
    public RayAbstractChannelHandlerContext prev(){
        return prev;
    }

}
