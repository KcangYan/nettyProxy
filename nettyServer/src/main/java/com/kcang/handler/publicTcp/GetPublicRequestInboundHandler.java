package com.kcang.handler.publicTcp;

import com.kcang.config.NettyServerProperties;
import com.kcang.service.data.DataForwardService;
import com.kcang.service.publicTcpService.UpholdPublicClientsService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.DefaultHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetPublicRequestInboundHandler extends ChannelInboundHandlerAdapter {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(NettyServerProperties.isHttpProxy()){
            try {
                if(msg.getClass().equals(DefaultHttpRequest.class)){
                    HttpRequest header = (HttpRequest) msg;
                    String clientName = header.uri().split("/")[1];
                    header.setUri(header.uri().replaceAll("/"+clientName,""));
                    DataForwardService.sendToPublicClient();
                    //ctx.writeAndFlush(header);
                }else {
                    //ctx.writeAndFlush(msg);
                }
            }catch (Exception e){
                myLogger.error("异常http请求: "+e.toString());
                ctx.disconnect();
            }
        }else {
            //非http多客户端模式
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DataForwardService.uPublic.addClient(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        DataForwardService.uPublic.delClient(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
