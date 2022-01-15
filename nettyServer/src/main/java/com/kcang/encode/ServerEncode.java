package com.kcang.encode;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServerEncode extends MessageToMessageEncoder {

    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        myLogger.info("出口编码器收到消息: " + msg.toString());
        out.add(msg);
    }
}
