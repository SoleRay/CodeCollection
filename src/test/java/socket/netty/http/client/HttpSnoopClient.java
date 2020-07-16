/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package socket.netty.http.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.util.Scanner;

/**
 * A simple HTTP client that prints out the content of the HTTP response to
 * {@link System#out} to test {@link }.
 */
public final class HttpSnoopClient {

    static final String URL = System.getProperty("url", "http://127.0.0.1:8080/?apple=green&girl=nice");

    public static void main(String[] args) throws Exception {
        URI uri = new URI(URL);
        String scheme = uri.getScheme() == null? "http" : uri.getScheme();
        String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            if ("http".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("https".equalsIgnoreCase(scheme)) {
                port = 443;
            }
        }

        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            System.err.println("Only HTTP(S) is supported.");
            return;
        }

        // Configure SSL context if necessary.
        final boolean ssl = "https".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new HttpSnoopClientInitializer(sslCtx));

            // Make the connection attempt.
            Channel ch = b.connect(host, port).sync().channel();

            // Prepare the HTTP request.
            HttpMethod httpMethod = HttpMethod.POST;

//
            int smallThreshold = 750;
            int largeThreshold = 500;
            String smallContent = createContent(smallThreshold);
            String largeContent = createContent(largeThreshold);
            send(uri, host, ch, httpMethod, smallContent);
//            send(uri, host, ch, httpMethod, largeContent);
//            for(int i=0;i<10;i++){
//
//                if(i%2==0){
//                    httpMethod = HttpMethod.POST;
//                }else {
//                    httpMethod = HttpMethod.GET;
//                }
//                send(uri, host, ch, httpMethod, i+"");
//            }
//            for(;;){
//                System.out.println("请输入：");
//                Scanner scanner = new Scanner(System.in);
//                String s = scanner.nextLine();
//                if("close".equals(s)){
//                    scanner.close();
//                    ch.close();
//                    break;
//                }else {
//                    send(uri, host, ch, httpMethod, s);
//                }
//            }

            // Send the HTTP request.

            // Wait for the server to close the connection.
            ch.closeFuture().sync();
        } finally {
            // Shut down executor threads to exit.
            group.shutdownGracefully();
        }
    }

    private static String createContent(int smallThreshold) {
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<smallThreshold;i++){
            builder.append("a");
        }
        return builder.toString();
    }

    private static void send(URI uri, String host, Channel ch, HttpMethod httpMethod, String str) {
        ByteBuf buf = convertContentToByteBuf(str);

        HttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, httpMethod, uri.getPath()+"?"+uri.getQuery(), buf);
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH,str.length());

        // Set some example cookies.
        request.headers().set(
                HttpHeaderNames.COOKIE,
                ClientCookieEncoder.STRICT.encode(
                        new DefaultCookie("my-cookie", "foo"),
                        new DefaultCookie("another-cookie", "bar")));

        if(ch.isOpen()){
            ch.writeAndFlush(request);
        }else {
            System.out.println("closed...");
        }

    }

    private static ByteBuf convertContentToByteBuf(String str) {
        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeBytes(str.getBytes(CharsetUtil.UTF_8));
        return byteBuf;
    }
}
