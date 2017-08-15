package com.yuan.nettyinaction.chapter2;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Listing 2.3  of <i>Netty in Action</i>
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
public class EchoServer {

    private final int port;
        
    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(group)
//             .channel(NioServerSocketChannel.class)
//             .localAddress(new InetSocketAddress(port))
//             .childHandler(new ChannelInitializer<SocketChannel>() {
//                 @Override
//                 public void initChannel(SocketChannel ch)
//                     throws Exception {
//                     ch.pipeline().addLast(
//                             new EchoServerHandler());
//                 }
//             });
//
//            ChannelFuture f = b.bind().sync();
//            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
//            f.channel().closeFuture().sync();

            ServerBootstrap b = new ServerBootstrap();
            b.group(group);
            b = b.channel(NioServerSocketChannel.class);
            b = b.localAddress(new InetSocketAddress(port));
            ChannelHandler childHandler = new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch)
                        throws Exception {
//                    ch.pipeline().addLast(
//                            new EchoServerHandler());
                    ChannelPipeline channelPipeline = ch.pipeline();
                    channelPipeline.addLast(new EchoServerHandler());
                }
            };
            b.childHandler(childHandler);
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println(
//                    "Usage: " + EchoServer.class.getSimpleName() +
//                    " <port>");
//            return;
//        }
//        int port = Integer.parseInt(args[0]);
        int port = Integer.parseInt("8888");
        new EchoServer(port).start();
    }
}

