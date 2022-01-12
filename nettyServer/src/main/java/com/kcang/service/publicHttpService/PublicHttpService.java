package com.kcang.service.publicHttpService;

import com.kcang.config.NettyServerProperties;
import com.kcang.decode.ServerPartDecode;
import com.kcang.model.NettyServerTemplate;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class PublicHttpService implements Runnable {
    private NettyServerTemplate nettyServerTemplate;
    public PublicHttpService(NettyServerProperties nettyServerProperties){
        ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addFirst(new ServerPartDecode());
                //ch.pipeline().addLast("logging",new LoggingHandler(LogLevel.INFO));
            }
        };
        this.nettyServerTemplate = new NettyServerTemplate(nettyServerProperties.getPublicHttpPort(),channelChannelInitializer);
    }
    @Override
    public void run() {
        try {
            nettyServerTemplate.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
