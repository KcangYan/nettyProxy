package com.kcang.handler.privateTcp;

import com.kcang.config.NettyClientProperties;
import com.kcang.service.data.DataForwardService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateTcpInboundHandler extends ChannelInboundHandlerAdapter {

    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    private String id;//公网端对象的消息id
    private ChannelHandlerContext publicCtx;//需要转发到的公网连接对象
    public PrivateTcpInboundHandler(String id, ChannelHandlerContext ctx){
        this.id = id;
        this.publicCtx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg != null){
            publicCtx.writeAndFlush(NettyClientProperties.getClientName()+"\032"+id+"\032"+(String) msg);
        }else {
            myLogger.error("客户端收到内网转发端的空消息 "+ctx.toString());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        myLogger.info("转发连接建立成功");
        DataForwardService.addClient(id,ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        DataForwardService.delClient(id);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        DataForwardService.delClient(id);
    }
}
