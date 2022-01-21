package com.kcang.encode;

import com.kcang.config.NettyServerProperties;
import com.kcang.template.EncodeTemplate;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class PrivateTcpEncode extends EncodeTemplate {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        if(NettyServerProperties.isAesOpen()){
            out.add("\001"+super.encryptAES(msg.toString(), NettyServerProperties.getAesKey())+"\002");
        }else {
            out.add("\001"+msg.toString()+"\002");
        }
    }
}
