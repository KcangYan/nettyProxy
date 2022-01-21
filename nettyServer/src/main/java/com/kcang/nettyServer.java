package com.kcang;

import com.kcang.config.NettyServerProperties;
import com.kcang.service.NettyServerService;


public class nettyServer {
    public static void main(String[] args){
        NettyServerProperties.init();
        NettyServerService nettyServerService = new NettyServerService();
        nettyServerService.run();
    }
}
