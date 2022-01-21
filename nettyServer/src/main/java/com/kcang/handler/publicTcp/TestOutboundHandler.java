package com.kcang.handler.publicTcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class TestOutboundHandler extends ChannelOutboundHandlerAdapter {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg,promise);
        myLogger.info("write "+ctx.name());
        System.out.print(msg.toString());
    }
}
