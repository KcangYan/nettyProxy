package com.kcang.service;

import com.kcang.service.data.ForwardService;
import com.kcang.service.privateTcpService.PrivateTcpService;
import com.kcang.service.privateTcpService.UpholdForwardClientsService;
import com.kcang.service.publicTcpService.PublicTcpService;
import com.kcang.service.publicTcpService.UpholdPublicClientsService;

public class NettyServerService {

    public void run(){
        UpholdForwardClientsService uForward = new UpholdForwardClientsService();
        UpholdPublicClientsService uPublic = new UpholdPublicClientsService();

        ForwardService.uForward = uForward;
        ForwardService.uPublic = uPublic;

        PublicTcpService publicTcpServer = new PublicTcpService();
        PrivateTcpService privateTcpServer = new PrivateTcpService();

        Thread externalTcp = new Thread(publicTcpServer,"publicTcpServer");
        Thread insideTcp = new Thread(privateTcpServer,"privateTcpServer");
        externalTcp.start();
        insideTcp.start();
    }
}
