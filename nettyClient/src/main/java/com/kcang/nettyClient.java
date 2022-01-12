package com.kcang;

import com.kcang.config.NettyClientProperties;
import com.kcang.service.NettyClientService;

public class nettyClient {
    public static void main(String[] args){
        NettyClientService nettyClientService = new NettyClientService(new NettyClientProperties());
        nettyClientService.run();
    }
}
