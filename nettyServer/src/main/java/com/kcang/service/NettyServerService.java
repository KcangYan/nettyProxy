package com.kcang.service;

import com.kcang.config.NettyServerProperties;
import com.kcang.service.privateTcpService.PrivateTcpService;
import com.kcang.service.privateTcpService.UpholdForwardClientsService;
import com.kcang.service.publicTcpService.PublicTcpService;

public class NettyServerService {

    private NettyServerProperties nettyServerProperties;
    public NettyServerService(NettyServerProperties nettyServerProperties){
        this.nettyServerProperties = nettyServerProperties;
    }

    public void run(){
        UpholdForwardClientsService upholdForwardClientsService = new UpholdForwardClientsService();
        PublicTcpService publicTcpService = new PublicTcpService(nettyServerProperties);
        PrivateTcpService privateTcpService = new PrivateTcpService(nettyServerProperties,upholdForwardClientsService);
        Thread externalTcp = new Thread(publicTcpService,"publicTcpService");
        Thread insideTcp = new Thread(privateTcpService,"privateTcpService");
        externalTcp.start();
        insideTcp.start();
    }
}
