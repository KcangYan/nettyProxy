package com.kcang.service.publicTcpService;

import com.kcang.config.NettyClientProperties;
import com.kcang.handler.publicTcp.PublicTcpInboundHandler;
import com.kcang.handler.publicTcp.PublicTcpOutboundHandler;
import com.kcang.handler.publicTcp.Test;
import com.kcang.model.NettyClientTemplate;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class PublicTcpService implements Runnable {

    private NettyClientTemplate nettyClientTemplate;

    public PublicTcpService(NettyClientProperties nettyClientProperties){
        ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                //ch.pipeline().addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS));
                ch.pipeline().addFirst(new PublicTcpInboundHandler());

                ch.pipeline().addLast(new Test());
                ch.pipeline().addLast(new PublicTcpOutboundHandler());
            }
        };
        this.nettyClientTemplate = new NettyClientTemplate(nettyClientProperties.getPublicAddress(),nettyClientProperties.getPublicPort(),
                channelChannelInitializer);
    }

    public void run() {
        nettyClientTemplate.run();
    }
}
