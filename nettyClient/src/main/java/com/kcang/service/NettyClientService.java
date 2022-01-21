package com.kcang.service;

import com.kcang.config.NettyClientProperties;
import com.kcang.service.publicTcpService.PublicTcpService;

public class NettyClientService {

    public void run(){
        PublicTcpService publicTcpService = new PublicTcpService();
        publicTcpService.run();
    }
}
