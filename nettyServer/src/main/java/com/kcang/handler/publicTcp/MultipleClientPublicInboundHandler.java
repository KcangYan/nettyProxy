package com.kcang.handler.publicTcp;

import com.kcang.config.NettyServerProperties;
import com.kcang.service.data.ForwardService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MultipleClientPublicInboundHandler extends ChannelInboundHandlerAdapter {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将收到的公网请求转发给局域网客户端，id\032msg
     */
    private String clientName;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String id = ForwardService.uPublic.getId(ctx);//标识是哪个公网客户端转来的消息，到时候回复给对应的
        try {
            if(!NettyServerProperties.isHttpProxy()){
                if(msg != null){
                    ForwardService.sendToForwardClient(id+"\032"+msg);
                }
            }else {
                if(msg instanceof HttpRequest){
                    HttpRequest header = (HttpRequest) msg;
                    header = getClientName(header);
                    //System.out.println(header.toString());
                    //String headerM = getHeadersStr(header);
                    String headerM = header.toString();
                    myLogger.info(headerM);
                    ForwardService.sendToForwardClient(clientName, id+"\032"+headerM);
                }else if(msg instanceof HttpContent){
                    HttpContent content = (HttpContent) msg;
                    ByteBuf in = content.content();
                    ByteBuf bufMsg = in.readBytes(in.readableBytes());
                    //myLogger.info(bufMsg.toString(CharsetUtil.UTF_8));
                    String contentStr = bufMsg.toString(CharsetUtil.UTF_8);
                    if(contentStr != null) {
                        ForwardService.sendToForwardClient(clientName, id+"\032"+contentStr);
                    }
                }else {
                    if(msg != null){
                        ForwardService.sendToForwardClient(clientName, id+"\032"+msg);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            myLogger.error("异常http请求: "+e.toString());
            ctx.disconnect();
        }

    }
    private HttpRequest getClientName(HttpRequest header){
        clientName = header.uri().split("/")[1];
        header.setUri(header.uri().substring(clientName.length()+1));
        if(header.uri().equals("")){
            header.setUri("/");
        }
        return header;
    }
    private String getHeadersStr(HttpRequest header){
        String headerToString = header.toString();
        String[] hs = headerToString.split("\r\n");
        StringBuilder headerStr = new StringBuilder();
        for(int i=1;i<hs.length;i++){
            if(i == hs.length-1){
                headerStr.append(hs[i]);
            }else {
                headerStr.append(hs[i]).append("\r\n");
            }
        }
        return headerStr+"\r\n\r\n";
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ForwardService.uPublic.addClient(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ForwardService.uPublic.delClient(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ForwardService.uPublic.delClient(ctx);
    }
}
