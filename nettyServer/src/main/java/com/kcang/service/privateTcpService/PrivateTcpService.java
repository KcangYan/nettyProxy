package com.kcang.service.privateTcpService;

import com.kcang.config.NettyServerProperties;
import com.kcang.decode.ServerDecode;
import com.kcang.encode.ServerEncode;
import com.kcang.handler.privateTcp.TestChannelInboundHandler;
import com.kcang.handler.privateTcp.TestChannelOutboundHandler;
import com.kcang.template.NettyServerTemplate;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 面向局域网内客户端通信服务器实例
 */
public class PrivateTcpService extends NettyServerTemplate implements Runnable {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    public PrivateTcpService(NettyServerProperties nettyServerProperties){
        super.myLogger = this.myLogger;
        super.port = nettyServerProperties.getPrivatePort();
    }

    /**
     * 将handler加入pipeline责任链中
     * @param ch socketChannel
     * @throws Exception 异常
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addFirst(new ServerDecode());
        ch.pipeline().addFirst(new TestChannelInboundHandler());
        //ch.pipeline().addFirst(new HeartBeatHandler());
        //ch.pipeline().addLast("logging",new LoggingHandler(LogLevel.INFO));

        ch.pipeline().addLast(new ServerEncode());
        ch.pipeline().addLast(new TestChannelOutboundHandler());
    }

    /**
     * 异步Runnable接口，执行模板中父类的run方法运行服务器，方便异步开启多个服务
     */
    @Override
    public void run() {
        try {
            super.run(this);
        } catch (InterruptedException e) {
            myLogger.error(e.toString());
        }
    }
}
