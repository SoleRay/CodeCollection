package socket.nio.vNetty.pipeline.context;

import socket.nio.vNetty.pipeline.RayChannelPipeline;
import socket.nio.vNetty.pipeline.handler.RayChannelHandler;

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
            next.handler.channelRead(next,msg);
        }
    }

    public void setNext(RayAbstractChannelHandlerContext context){
        next = context;
    }

    public RayAbstractChannelHandlerContext next(){
        return next;
    }

    public void setPrev(RayAbstractChannelHandlerContext context){
        prev = context;
    }

    public RayAbstractChannelHandlerContext prev(){
        return prev;
    }

}
