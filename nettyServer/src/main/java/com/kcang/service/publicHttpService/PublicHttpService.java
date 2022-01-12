package com.kcang.service.publicHttpService;

import com.kcang.config.NettyServerProperties;
import com.kcang.decode.ServerPartDecode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class PublicHttpService implements Runnable {
    private NettyServerProperties nettyServerProperties;
    public PublicHttpService(NettyServerProperties nettyServerProperties){
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
        com.kcang.model.NettyServerFactory externalHttp = new com.kcang.model.NettyServerFactory(nettyServerProperties.getPublicHttpPort(),channelChannelInitializer);
        try {
            externalHttp.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
