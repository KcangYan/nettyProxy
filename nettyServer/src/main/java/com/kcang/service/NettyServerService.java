package com.kcang.service;

import com.kcang.config.NettyServerProperties;
import com.kcang.service.privateTcpService.PrivateTcpService;
import com.kcang.service.publicHttpService.PublicHttpService;

public class NettyServerService {

    private NettyServerProperties nettyServerProperties;
    public NettyServerService(NettyServerProperties nettyServerProperties){
        this.nettyServerProperties = nettyServerProperties;
    }

    public void run(){
        PublicHttpService publicHttpService = new PublicHttpService(nettyServerProperties);
        PrivateTcpService privateTcpService = new PrivateTcpService(nettyServerProperties);
        Thread externalHttp = new Thread(publicHttpService,"publicHttpService");
        Thread insideTcp = new Thread(privateTcpService,"privateTcpService");
        externalHttp.start();
        insideTcp.start();
    }
}
