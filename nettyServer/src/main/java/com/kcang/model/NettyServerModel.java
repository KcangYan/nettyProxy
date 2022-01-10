package com.kcang.model;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

public class NettyServerModel {

    private final Logger myLogger = LoggerFactory.getLogger(NettyServerModel.class);

    private int bindPort;
    private List<ChannelInboundHandler> useChannelInboundHandlers = null;
    private List<ChannelOutboundHandler> useChannelOutboundHandlers = null;

    public NettyServerModel(int port, List<ChannelInboundHandler> channelInboundHandlers, List<ChannelOutboundHandler> channelOutboundHandlers){
        bindPort = port;
        useChannelInboundHandlers = channelInboundHandlers;
        useChannelOutboundHandlers = channelOutboundHandlers;
    }
    public NettyServerModel(List<ChannelInboundHandler> channelInboundHandlers, List<ChannelOutboundHandler> channelOutboundHandlers){
        bindPort = 19191;
        useChannelInboundHandlers = channelInboundHandlers;
        useChannelOutboundHandlers = channelOutboundHandlers;
    }

    public void run() throws InterruptedException {
        myLogger.info("正在启动NettyServer: "+bindPort);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(new InetSocketAddress("0.0.0.0",bindPort));

            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    if(useChannelInboundHandlers != null){
                        for(ChannelInboundHandler channelInboundHandler : useChannelInboundHandlers){
                            socketChannel.pipeline().addLast(channelInboundHandler);//加载Inbound
                        }
                    }
                    if(useChannelOutboundHandlers != null){
                        for(ChannelOutboundHandler channelOutboundHandler : useChannelOutboundHandlers){
                            socketChannel.pipeline().addFirst(channelOutboundHandler);//加载outbound
                        }
                    }
                }
            });

            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            myLogger.info("NettyServer服务启动成功端口: " + bindPort);
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
