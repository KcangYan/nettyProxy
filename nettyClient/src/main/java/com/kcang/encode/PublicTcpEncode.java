package com.kcang.encode;

import com.kcang.config.NettyClientProperties;
import com.kcang.template.EncodeTemplate;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PublicTcpEncode extends EncodeTemplate {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        myLogger.debug("发送消息: "+msg);
        if(NettyClientProperties.getAesOpen()){
            out.add(buf.writeBytes((super.encryptAES(msg,NettyClientProperties.getAesKey())+"\001").getBytes(CharsetUtil.UTF_8)));
        }else {
            out.add(buf.writeBytes((msg+"\001").getBytes(CharsetUtil.UTF_8)));
        }
    }
}
