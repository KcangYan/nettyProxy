package com.kcang;

import com.kcang.config.NettyClientProperties;
import com.kcang.service.NettyClientService;

public class nettyClient {
    public static void main(String[] args){
        NettyClientProperties.init();
        NettyClientService nettyClientService = new NettyClientService();
        nettyClientService.run();
    }
}
