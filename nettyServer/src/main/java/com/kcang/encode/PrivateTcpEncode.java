package com.kcang.encode;

import com.kcang.config.NettyServerProperties;
import com.kcang.template.EncodeTemplate;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class PrivateTcpEncode extends EncodeTemplate {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        if(NettyServerProperties.isAesOpen()){
            out.add(super.encryptAES(msg.toString(), NettyServerProperties.getAesKey())+"\001");
        }else {
            out.add(msg.toString()+"\001");
        }
    }
}
