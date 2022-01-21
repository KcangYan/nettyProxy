package com.kcang.service;

import com.kcang.service.data.DataForwardService;
import com.kcang.service.privateTcpService.PrivateTcpService;
import com.kcang.service.publicTcpService.PublicTcpService;

public class NettyServerService {

    public void run(){
        DataForwardService.init();

        PublicTcpService publicTcpServer = new PublicTcpService();
        PrivateTcpService privateTcpServer = new PrivateTcpService();

        Thread externalTcp = new Thread(publicTcpServer,"publicTcpServer");
        Thread insideTcp = new Thread(privateTcpServer,"privateTcpServer");
        externalTcp.start();
        insideTcp.start();
    }
}
