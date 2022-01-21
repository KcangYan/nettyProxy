package com.kcang.service.publicTcpService;

import com.kcang.config.NettyClientProperties;
import com.kcang.handler.publicTcp.PublicTcpInboundHandler;
import com.kcang.handler.publicTcp.PublicTcpOutboundHandler;
import com.kcang.handler.publicTcp.Test;
import com.kcang.template.NettyClientTemplate;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.LoggerFactory;

/**
 * 连接公网转发服务器的客户端类
 */
public class PublicTcpService extends NettyClientTemplate implements Runnable {

    public PublicTcpService(){
        super.myLogger = LoggerFactory.getLogger(this.getClass());
        super.serverAddress = NettyClientProperties.getPublicAddress();
        super.port = NettyClientProperties.getPublicPort();
    }

    /**
     * 配置pipeline责任链
     * @param ch socketChannel
     * @throws Exception 异常
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addFirst(new PublicTcpInboundHandler());
        ch.pipeline().addFirst(new PublicTcpInboundHandler());

        ch.pipeline().addLast(new Test());
        ch.pipeline().addLast(new PublicTcpOutboundHandler());
    }

    @Override
    public void run() {
        super.run(this);
    }

}
