package com.kcang.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServerPartDecode extends ByteToMessageDecoder {

    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    private String message = "";
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf bufMsg = in.readBytes(in.readableBytes());
        String getMsg = bufMsg.toString(CharsetUtil.UTF_8);
        myLogger.info("当前消息: "+getMsg);
        message = message + getMsg;
        myLogger.info("全部消息: "+message);
    }
}
