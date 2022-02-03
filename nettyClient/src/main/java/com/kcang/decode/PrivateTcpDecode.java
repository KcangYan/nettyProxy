package com.kcang.decode;

import com.kcang.template.DecodeTemplate;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PrivateTcpDecode extends DecodeTemplate {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf bufMsg = in.readBytes(in.readableBytes());
        out.add(bufMsg.toString(CharsetUtil.UTF_8));
        myLogger.debug("收到本地服务的回复："+bufMsg.toString(CharsetUtil.UTF_8));
    }
}
