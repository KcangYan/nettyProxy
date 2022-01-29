package com.kcang.decode;

import com.kcang.template.DecodeTemplate;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.util.List;

public class PrivateTcpDecode extends DecodeTemplate {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf bufMsg = in.readBytes(in.readableBytes());
        out.add(bufMsg.toString(CharsetUtil.UTF_8));
    }
}
