package com.kcang.handler.privateTcp;

import com.kcang.service.privateTcpService.UpholdForwardClientsService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class PrivateTcpInboundHandler extends ChannelInboundHandlerAdapter {

    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    //客户端维护服务
    private UpholdForwardClientsService upholdForwardClientsService;
    public PrivateTcpInboundHandler(UpholdForwardClientsService u){
        this.upholdForwardClientsService = u;
    }
    /**
     * 获得消息时触发的方法
     * 当模式为多客户端时，收到的第一条消息
     * @param ctx 可使用的客户端连接对象
     * @param msg 获得的消息，前置解码器解码后的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String received = (String) msg;
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        int clientPort = ipSocket.getPort();
        myLogger.info("客户端地址和端口: "+clientIp+":"+clientPort);
        super.channelRead(ctx, msg);
    }

    /**
     * 通道建立成功的首次调用的方法
     * @param ctx 已连接成功的客户端对象 可以读写数据
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    /**
     * 客户端注册通道时触发的方法
     * @param ctx 客户端连接通道封装，该方法触发是 ctx未连接就绪无法读写
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    /**
     * 客户端连接结束是触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }
}
