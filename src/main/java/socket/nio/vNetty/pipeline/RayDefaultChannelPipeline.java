package socket.nio.vNetty.pipeline;

import socket.nio.vNetty.channel.RayChannel;
import socket.nio.vNetty.pipeline.context.RayAbstractChannelHandlerContext;
import socket.nio.vNetty.pipeline.context.RayChannelHandlerContext;
import socket.nio.vNetty.pipeline.context.RayDefaultChannelHandlerContext;
import socket.nio.vNetty.pipeline.handler.RayChannelHandler;
import socket.nio.vNetty.pipeline.handler.RayChannelInboundHandlerAdapter;

public class RayDefaultChannelPipeline implements RayChannelPipeline {

    private RayChannel channel;

    RayAbstractChannelHandlerContext head;

    RayAbstractChannelHandlerContext tail;

    private boolean registered;

    private PendingHandlerAddTask pendingHandlerAddTaskHead;

    public RayDefaultChannelPipeline(RayChannel channel) {
        this.channel = channel;
        this.head = new RayHeadContext(this);
        this.tail = new RayTailContext(this);

        head.setNext(tail);
        tail.setPrev(head);
    }

    @Override
    public void addLast(RayChannelHandler ...handler){
        for (RayChannelHandler channelHandler : handler) {
            addLast(channelHandler);
        }
    }

    private void addLast(RayChannelHandler handler){
        if (handler == null) {
            return;
        }
        RayDefaultChannelHandlerContext context = new RayDefaultChannelHandlerContext(this, handler);
        RayAbstractChannelHandlerContext prev = tail.prev();
        prev.setNext(context);
        context.setPrev(prev);
        context.setNext(tail);
        tail.setPrev(context);

        if(registered){
            context.handler().handlerAdded(context);
        }else {
            wrapToTaskAndCallLater(context);
        }
    }

    private void wrapToTaskAndCallLater(RayDefaultChannelHandlerContext handlerContext) {

        PendingHandlerAddTask pendingHandlerAddTask = new PendingHandlerAddTask(handlerContext);
        if (pendingHandlerAddTaskHead == null) {
            pendingHandlerAddTaskHead = pendingHandlerAddTask;
        }else {
            PendingHandlerTask task = this.pendingHandlerAddTaskHead;
            while(task.next!=null){
                task = task.next;
            }
            task.next = pendingHandlerAddTask;
        }
    }

    @Override
    public void remove(RayChannelHandler handler) {
        remove(context(handler));
    }

    public void remove(RayAbstractChannelHandlerContext context) {
        RayAbstractChannelHandlerContext prev = context.prev();
        RayAbstractChannelHandlerContext next = context.next();
        prev.setNext(next);
        next.setPrev(prev);
    }

    @Override
    public void fireChannelRead(Object msg) {
        head.fireChannelRead(msg);
    }

    @Override
    public void invokeHandlerAddedIfNeeded() {

        PendingHandlerTask task = null;

        synchronized (this) {
            task = this.pendingHandlerAddTaskHead;

            registered = true;

            //clear for GC
            pendingHandlerAddTaskHead = null;
        }

        while(task!=null){
            task.run();
            task = task.next;
        }
    }


    @Override
    public RayChannel channel() {
        return channel;
    }

    private RayAbstractChannelHandlerContext context(RayChannelHandler handler) {
        RayAbstractChannelHandlerContext context = head.next();

        while (context!=null){

            if(context.handler()==handler){
                return context;
            }

            context = context.next();
        }

        return null;
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

    class RayPipelineChannelHandler extends RayChannelInboundHandlerAdapter {

        @Override
        public void channelRead(RayChannelHandlerContext ctx, Object msg) {
            ctx.fireChannelRead(msg);
        }
    }

    abstract class PendingHandlerTask{

        protected RayAbstractChannelHandlerContext context;

        PendingHandlerTask next;

        public PendingHandlerTask(RayAbstractChannelHandlerContext context) {
            this.context = context;
        }

        abstract protected void run();
    }

    class PendingHandlerAddTask extends PendingHandlerTask {

        public PendingHandlerAddTask(RayAbstractChannelHandlerContext context) {
            super(context);
        }

        @Override
        protected void run() {
            context.handler().handlerAdded(context);
        }
    }


}
