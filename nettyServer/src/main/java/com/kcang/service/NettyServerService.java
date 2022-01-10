package com.kcang.service;

import com.kcang.decode.ServerPartDecode;
import com.kcang.model.NettyServerModel;
import io.netty.channel.ChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NettyServerService {

    private final Logger myLogger = LoggerFactory.getLogger(NettyServerService.class);

    public void run(){
        ServerPartDecode testDecode = new ServerPartDecode();
        List<ChannelInboundHandler> channelInboundHandlers = new ArrayList<>();
        channelInboundHandlers.add(testDecode);

        NettyServerModel nettyServer = new NettyServerModel(channelInboundHandlers,null);
        try{
            nettyServer.run();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
