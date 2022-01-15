package com.kcang.service.publicTcpService;

import com.kcang.config.NettyServerProperties;
import com.kcang.template.NettyServerTemplate;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 面向公网获取需要转发的通信服务器实例
 */
public class PublicTcpService extends NettyServerTemplate implements Runnable {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    public PublicTcpService(NettyServerProperties nettyServerProperties){
        super.myLogger = this.myLogger;
        super.port = nettyServerProperties.getPublicPort();
    }

    /**
     * 将handler加入pipeline责任链中
     * @param ch socketChannel
     * @throws Exception 异常
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //ch.pipeline().addFirst(new ServerDecode());
        //ch.pipeline().addLast("logging",new LoggingHandler(LogLevel.INFO));
    }

    /**
     * 异步Runnable接口，执行模板中父类的run方法运行服务器，方便异步开启多个服务
     */
    @Override
    public void run() {
        try {
            super.run(this);
        } catch (Exception e) {
            myLogger.error(e.toString());
        }
    }
}
