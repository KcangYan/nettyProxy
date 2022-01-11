package com.kcang.service;

import com.kcang.decode.ServerPartDecode;
import com.kcang.model.NettyServerModel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerService {

    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    public void run(){

        ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addFirst(new ServerPartDecode());
                ch.pipeline().addLast("logging",new LoggingHandler(LogLevel.INFO));
            }
        };

        NettyServerModel nettyServer = new NettyServerModel(channelChannelInitializer);
        try{
            nettyServer.run();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
