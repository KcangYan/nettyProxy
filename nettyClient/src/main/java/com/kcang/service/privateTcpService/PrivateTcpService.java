package com.kcang.service.privateHttpService;

import com.kcang.config.NettyClientProperties;
import com.kcang.model.NettyClientTemplate;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class PrivateTcpService implements Runnable{

    private NettyClientTemplate nettyClientTemplate;

    public PrivateTcpService(NettyClientProperties nettyClientProperties){
        ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new com.kcang.handler.privateHttp.PrivateTcpOutboundHandler());
            }
        };
        this.nettyClientTemplate = new NettyClientTemplate(nettyClientProperties.getPrivateAddress(),nettyClientProperties.getPrivatePort(),
                channelChannelInitializer);
    }

    public void run() {
        nettyClientTemplate.run();
    }
}
