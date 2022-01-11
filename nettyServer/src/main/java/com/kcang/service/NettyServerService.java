package com.kcang.service;

import com.kcang.config.NettyServerProperties;
import com.kcang.service.externalHttpService.ExternalHttpService;
import com.kcang.service.insideTcpService.InsideTcpService;

public class NettyServerService {

    private NettyServerProperties nettyServerProperties;
    public NettyServerService(NettyServerProperties nettyServerProperties){
        this.nettyServerProperties = nettyServerProperties;
    }

    public void run(){
        ExternalHttpService externalHttpService = new ExternalHttpService(nettyServerProperties);
        InsideTcpService insideTcpService = new InsideTcpService(nettyServerProperties);
        Thread externalHttp = new Thread(externalHttpService,"externalHttpServer");
        Thread insideTcp = new Thread(insideTcpService,"insideTcpServer");
        externalHttp.start();
        insideTcp.start();
    }
}
