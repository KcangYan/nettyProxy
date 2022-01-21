package com.kcang.service.publicTcpService;

import com.kcang.config.NettyServerProperties;
import com.kcang.decode.PublicTcpDecode;
import com.kcang.handler.publicTcp.GetPublicRequestInboundHandler;
import com.kcang.template.NettyServerTemplate;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 面向公网获取需要转发的通信服务器实例
 */
public class PublicTcpService extends NettyServerTemplate implements Runnable {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    public PublicTcpService(){
        super.myLogger = this.myLogger;
        super.port = NettyServerProperties.getPublicPort();
    }

    /**
     * 将handler加入pipeline责任链中
     * @param ch socketChannel
     * @throws Exception 异常
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new PublicTcpDecode());
        ch.pipeline().addLast(new GetPublicRequestInboundHandler());

        //ch.pipeline().addFirst("t1",new TestOutboundHandler());//1
        //ch.pipeline().addFirst("t2",new TestOutboundHandler());//2
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
