package socket.netty.demo.dn;

import socket.netty.demo.dn.pool.NioSelectorRunnablePool;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
/**
 * 启动函数
 * @author -琴兽-
 *
 */
public class Start {

	public static void main(String[] args) {

		
		//初始化线程
		NioSelectorRunnablePool nioSelectorRunnablePool = new NioSelectorRunnablePool(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		
		//获取服务类
		NettyServerBootstrap bootstrap = new NettyServerBootstrap(nioSelectorRunnablePool);
		
		//绑定端口
		bootstrap.bind(new InetSocketAddress(8000));
		
		System.out.println("start");
	}

}
