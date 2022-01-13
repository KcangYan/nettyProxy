package com.kcang.service.publicTcpService;

import com.kcang.config.NettyServerProperties;
import com.kcang.decode.ServerPartDecode;
import com.kcang.model.NettyServerTemplate;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublicTcpService implements Runnable {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());
    private NettyServerTemplate nettyServerTemplate;
    public PublicTcpService(NettyServerProperties nettyServerProperties){
        ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addFirst(new ServerPartDecode());
                //ch.pipeline().addLast("logging",new LoggingHandler(LogLevel.INFO));
            }
        };
        this.nettyServerTemplate = new NettyServerTemplate(nettyServerProperties.getPublicPort(),channelChannelInitializer);
    }
    @Override
    public void run() {
        try {
            nettyServerTemplate.run();
        } catch (Exception e) {
            myLogger.error(e.toString());
        }
    }
}
