package com.kcang.service.publicTcpService;

import com.kcang.config.NettyClientProperties;
import com.kcang.handler.PublicTcpOutboundHandler;
import com.kcang.model.NettyClientTemplate;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class PublicTcpService implements Runnable {

    private NettyClientTemplate nettyClientTemplate;

    public PublicTcpService(NettyClientProperties nettyClientProperties){
        ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS));
                ch.pipeline().addLast(new PublicTcpOutboundHandler());
            }
        };
        this.nettyClientTemplate = new NettyClientTemplate(nettyClientProperties.getPublicTcpAddress(),nettyClientProperties.getPublicTcpPort(),
                channelChannelInitializer);
    }

    public void run() {
        nettyClientTemplate.run();
    }
}
