package com.kcang.handler.publicTcp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class PublicTcpOutboundHandler extends ChannelOutboundHandlerAdapter {
    private Logger myLogger = LoggerFactory.getLogger(PublicTcpOutboundHandler.class);

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        super.connect(ctx,remoteAddress,localAddress,promise);
        myLogger.info("connect "+ctx.name());
        //myLogger.info(ctx.toString() + remoteAddress+localAddress);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.deregister(ctx,promise);
        myLogger.info("deregister "+ctx.name());
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        super.bind(ctx, localAddress, promise);
        myLogger.info("bind "+ctx.name());
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.disconnect(ctx, promise);
        myLogger.info("disconnect "+ctx.name());
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);
        myLogger.info("close "+ctx.name());
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        super.read(ctx);
        myLogger.info("read "+ctx.name());
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello server", CharsetUtil.UTF_8));
                //ctx.write(Unpooled.copiedBuffer("hello server", CharsetUtil.UTF_8));
                myLogger.info("发送成功");
            }
        };
        t.start();
        //myLogger.info(ctx.toString());
        //ctx.writeAndFlush(Unpooled.copiedBuffer("hello server", CharsetUtil.UTF_8));
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        super.flush(ctx);
        myLogger.info("flush "+ctx.name());
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg,promise);
        myLogger.info("write "+ctx.name());
    }
}
