package com.yuan.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class nettyServer {


    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(nettyServer.class);

        int port = 9090;

        EventLoopGroup bossGroup = new NioEventLoopGroup();// 连接线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();// 处理线程组
        ServerBootstrap bootstrap = new ServerBootstrap();

        try{

            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childHandler(new ChildChannelHandler());
            // 绑定端口，同步等待成功
            ChannelFuture future = bootstrap.bind(port).sync();
            // 等待服务端监听端口关闭，等待服务端链路关闭之后main函数才退出
            future.channel().closeFuture().sync();
        }catch (Exception ex){

            logger.error(ex.getMessage(),ex);
        }
        finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
