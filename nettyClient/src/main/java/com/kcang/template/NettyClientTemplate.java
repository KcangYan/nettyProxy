package com.kcang.template;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * netty客户端连接模板类
 */
public abstract class NettyClientTemplate extends ChannelInitializer<SocketChannel>{
    protected String serverAddress;
    protected int port;
    protected Logger myLogger = LoggerFactory.getLogger(NettyClientTemplate.class);

    protected void run(ChannelInitializer<SocketChannel> channelChannelInitializer){
        myLogger.info("正在连接主机 "+serverAddress+":"+port);
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
            clientBootstrap.handler(channelChannelInitializer);
            clientBootstrap.remoteAddress(new InetSocketAddress(serverAddress, port));
            ChannelFuture channelFuture = clientBootstrap.connect().sync();
            myLogger.info("连接成功: "+serverAddress+":"+port);
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            myLogger.error("服务器主机连接失败: "+e.toString());
        }finally {
            group.shutdownGracefully();
            myLogger.info("连接结束: "+serverAddress+":"+port);
        }
    }

}
