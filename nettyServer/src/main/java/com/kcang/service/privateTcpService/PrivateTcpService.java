package com.kcang.service.privateTcpService;

import com.kcang.config.NettyServerProperties;
import com.kcang.decode.ServerPartDecode;
import com.kcang.model.NettyServerTemplate;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class PrivateTcpService implements Runnable {

    private NettyServerTemplate nettyServerTemplate;

    public PrivateTcpService(NettyServerProperties nettyServerProperties){
        ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addFirst(new ServerPartDecode());
                //ch.pipeline().addLast("logging",new LoggingHandler(LogLevel.INFO));
            }
        };
        this.nettyServerTemplate = new NettyServerTemplate(nettyServerProperties.getPrivateTcpPort(),channelChannelInitializer);
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
