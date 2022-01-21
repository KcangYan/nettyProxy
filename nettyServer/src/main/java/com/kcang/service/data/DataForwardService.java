package com.kcang.service.data;

import com.kcang.service.privateTcpService.UpholdForwardClientsService;
import com.kcang.service.publicTcpService.UpholdPublicClientsService;

public class DataForwardService {
    /**
     * 实例化内网转发客户端队列维护服务
     */
    public static UpholdForwardClientsService uForward;
    /**
     * 实例化公网转发连接端队列维护服务
     */
    public static UpholdPublicClientsService uPublic;

    static {
        uForward = new UpholdForwardClientsService();
        uPublic  = new UpholdPublicClientsService();
    }

    public static void init(){}

    public static void sendToForwardClient(String clientName,String message){

    }

    public static void sendToPublicClient(){

    }
}
