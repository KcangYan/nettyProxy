package com.kcang.service.privateHttpService;

import com.kcang.config.NettyClientProperties;
import com.kcang.handler.privateHttp.PrivateHttpOutboundHandler;
import com.kcang.model.NettyClientTemplate;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class PrivateHttpService implements Runnable{

    private NettyClientTemplate nettyClientTemplate;

    public PrivateHttpService(NettyClientProperties nettyClientProperties){
        ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new PrivateHttpOutboundHandler());
            }
        };
        this.nettyClientTemplate = new NettyClientTemplate(nettyClientProperties.getPrivateHttpAddress(),nettyClientProperties.getPrivateHttpPort(),
                channelChannelInitializer);
    }

    public void run() {
        nettyClientTemplate.run();
    }
}
