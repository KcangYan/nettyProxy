package com.kcang.model;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyServerFactory {

    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    private int port;
    private ChannelInitializer channelInitializer;

    public NettyServerFactory(int port, ChannelInitializer<SocketChannel> channelInitializer){
        this.port = port;
        this.channelInitializer = channelInitializer;
    }

    public NettyServerFactory(ChannelInitializer<SocketChannel> channelInitializer) {
        this.channelInitializer = channelInitializer;
        this.port = 19191;
    }

    public void run() throws InterruptedException {
        myLogger.info("正在启动NettyServer: "+port);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(new InetSocketAddress("0.0.0.0",port));

            serverBootstrap.childHandler(channelInitializer);//填充channelHandler

            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            myLogger.info("NettyServer服务启动成功端口: " + port);
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            myLogger.error("NettyServer启动失败: "+e.getMessage());
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();
            myLogger.error("NettyServer服务终止！");
            myLogger.info("正在重启NettyServer服务器");
            Thread.sleep(5000);
            this.run();
        }
    }
}
