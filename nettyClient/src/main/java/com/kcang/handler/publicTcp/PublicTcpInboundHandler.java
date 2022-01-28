package com.kcang.handler.publicTcp;

import com.kcang.config.NettyClientProperties;
import com.kcang.service.HeartBeatService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublicTcpInboundHandler extends ChannelInboundHandlerAdapter {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        myLogger.info("channelRegistered "+ctx.name());
        //myLogger.info(ctx.toString());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        myLogger.info("channelUnregistered "+ctx.name());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        myLogger.info("channelInactive "+ctx.name());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        myLogger.info("channelRead "+ctx.name());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        myLogger.info("channelReadComplete "+ctx.name());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        myLogger.info("userEventTriggered "+ctx.name());
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
        myLogger.info("channelWritabilityChanged "+ctx.name());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        myLogger.info("exceptionCaught "+ctx.name());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
        myLogger.info("启动心跳线程");
        String beatMsg = NettyClientProperties.getClientName()+"\032kcang";
        Thread beat = new Thread(new HeartBeatService(ctx,beatMsg),"HeartBeatService");
        beat.start();
        myLogger.info("心跳线程启动成功");
    }
}
