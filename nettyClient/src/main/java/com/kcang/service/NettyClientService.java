package com.kcang.service;

import com.kcang.config.NettyClientProperties;
import com.kcang.service.publicTcpService.PublicTcpService;

public class NettyClientService {

    private NettyClientProperties nettyClientProperties;
    public NettyClientService(NettyClientProperties nettyClientProperties){
        this.nettyClientProperties = nettyClientProperties;
    }

    public void run(){
        PublicTcpService publicTcpService = new PublicTcpService(nettyClientProperties);
        publicTcpService.run();
    }
}
