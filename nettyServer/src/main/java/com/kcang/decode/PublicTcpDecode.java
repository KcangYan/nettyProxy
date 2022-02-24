package com.kcang.decode;

import com.kcang.config.NettyServerProperties;
import com.kcang.template.MyHttpRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PublicTcpDecode extends HttpRequestDecoder {

    private Logger myLogger = LoggerFactory.getLogger(this.getClass());
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(NettyServerProperties.isHttpProxy()){
            super.decode(ctx,in,out);
        }else {
            //只转发，不处理
            ByteBuf bufMsg = in.readBytes(in.readableBytes());
            String getMsg = bufMsg.toString(CharsetUtil.UTF_8);
            out.add(getMsg);
        }
    }

    @Override
    protected HttpMessage createMessage(String[] initialLine) throws Exception {
        return new MyHttpRequest(
                HttpVersion.valueOf(initialLine[2]),
                HttpMethod.valueOf(initialLine[0]), initialLine[1], validateHeaders);
    }
}
