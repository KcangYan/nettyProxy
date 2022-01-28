package com.kcang.handler.publicTcp;

import com.kcang.config.NettyServerProperties;
import com.kcang.service.data.DataForwardService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetPublicRequestInboundHandler extends ChannelInboundHandlerAdapter {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将收到的公网请求转发给局域网客户端，id\032msg
     */
    private String clientName;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String id = DataForwardService.uPublic.getId(ctx);//标识是哪个公网客户端转来的消息，到时候回复给对应的
        if(NettyServerProperties.isHttpProxy()){
            try {
                if(msg.getClass().equals(DefaultHttpRequest.class)){
                    HttpRequest header = (HttpRequest) msg;

                    HttpHeaders headers = getClientName(header);

                    DataForwardService.sendToForwardClient(clientName, id+"\032"+getHeadersStr(headers));
                }else if(msg.getClass().equals(DefaultHttpContent.class)){
                    HttpContent content = (HttpContent) msg;
                    ByteBuf in = content.content();
                    ByteBuf bufMsg = in.readBytes(in.readableBytes());

                    DataForwardService.sendToForwardClient(clientName, id+"\032"+bufMsg.toString(CharsetUtil.UTF_8));
                }else {
                    DataForwardService.sendToForwardClient(clientName, id+"\032"+msg);
                }
            }catch (Exception e){
                myLogger.error("异常http请求: "+e.toString());
                ctx.disconnect();
            }
        }else {
            //非http多客户端模式，所以只要转发就行不需要额外处理
        }

    }
    private HttpHeaders getClientName(HttpRequest header){
        clientName = header.uri().split("/")[1];
        header.setUri(header.uri().replaceAll("/"+clientName,""));
        return header.headers();
    }
    private String getHeadersStr(HttpHeaders headers){
        String[] hs = headers.toString().split("\n");
        String headerStr = "";
        for(int i=1;i<hs.length;i++){
            if(i == hs.length-1){
                headerStr = headerStr + hs[i];
            }else {
                headerStr = headerStr + hs[i] + "\n";
            }
        }
        return headerStr+"\r\n";
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
        DataForwardService.uPublic.delClient(ctx);
    }
}
