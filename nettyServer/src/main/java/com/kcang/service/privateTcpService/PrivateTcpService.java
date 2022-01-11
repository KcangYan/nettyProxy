package com.kcang.service.privateTcpService;

import com.kcang.config.NettyServerProperties;
import com.kcang.decode.ServerPartDecode;
import com.kcang.model.NettyServerModel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class PrivateTcpService implements Runnable {
    private NettyServerProperties nettyServerProperties;
    public PrivateTcpService(NettyServerProperties nettyServerProperties){
        this.nettyServerProperties = nettyServerProperties;
    }
    @Override
    public void run() {
        ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addFirst(new ServerPartDecode());
                //ch.pipeline().addLast("logging",new LoggingHandler(LogLevel.INFO));
            }
        };
        NettyServerModel insideTcpService = new NettyServerModel(nettyServerProperties.getPrivateTcpPort(),channelChannelInitializer);
        try {
            insideTcpService.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
