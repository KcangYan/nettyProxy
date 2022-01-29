package com.kcang.service.privateTcpService;

import com.kcang.config.NettyClientProperties;
import com.kcang.handler.privateTcp.PrivateTcpInboundHandler;
import com.kcang.template.NettyClientTemplate;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.LoggerFactory;

/**
 * 连接公网转发服务器的客户端类
 */
public class PrivateTcpService extends NettyClientTemplate implements Runnable{

    //标记不同公网消息通道之间的客户端转发
    private String id;
    private ChannelHandlerContext ctx;
    public PrivateTcpService(String id, ChannelHandlerContext ctx){
        super.myLogger = LoggerFactory.getLogger(this.getClass());
        super.serverAddress = NettyClientProperties.getPrivateAddress();
        super.port = NettyClientProperties.getPrivatePort();
        this.id = id;
        this.ctx = ctx;
    }

    /**
     * 配置pipeline责任链
     * @param ch socketChannel
     * @throws Exception 异常
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new PrivateTcpInboundHandler(this.id,this.ctx));
    }

    public void run() {
        super.run(this);
    }
}
