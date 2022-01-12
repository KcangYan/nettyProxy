package com.kcang.service.privateTcpService;

import com.kcang.config.NettyServerProperties;
import com.kcang.decode.ServerPartDecode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

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
        com.kcang.model.NettyServerFactory insideTcpService = new com.kcang.model.NettyServerFactory(nettyServerProperties.getPrivateTcpPort(),channelChannelInitializer);
        try {
            insideTcpService.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
