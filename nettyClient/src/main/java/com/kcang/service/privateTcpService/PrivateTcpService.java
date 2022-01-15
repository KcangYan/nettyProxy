package com.kcang.service.privateTcpService;

import com.kcang.config.NettyClientProperties;
import com.kcang.handler.privateHttp.PrivateTcpOutboundHandler;
import com.kcang.template.NettyClientTemplate;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.LoggerFactory;

/**
 * 连接公网转发服务器的客户端类
 */
public class PrivateTcpService extends NettyClientTemplate implements Runnable{

    public PrivateTcpService(NettyClientProperties nettyClientProperties){
        super.myLogger = LoggerFactory.getLogger(this.getClass());
        super.serverAddress = nettyClientProperties.getPrivateAddress();
        super.port = nettyClientProperties.getPrivatePort();
    }

    /**
     * 配置pipeline责任链
     * @param ch socketChannel
     * @throws Exception 异常
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new PrivateTcpOutboundHandler());
    }

    public void run() {
        super.run(this);
    }
}
