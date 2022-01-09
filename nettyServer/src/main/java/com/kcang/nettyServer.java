package com.kcang;

import com.kcang.service.NettyServerService;

public class nettyServer {
    public static void main(String[] args){
        NettyServerService nettyServerService = new NettyServerService();
        nettyServerService.run();
    }
}
