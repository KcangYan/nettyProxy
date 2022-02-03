package com.kcang.handler.privateTcp;

import com.kcang.pojo.ForwardClient;
import com.kcang.service.data.ForwardService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleClientPrivateInboundHandler extends ChannelInboundHandlerAdapter {

    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获得消息时触发的方法
     * 当模式为多客户端时，收到的心跳信息 为 clientName\032kcang 转发信息为 clientName\032messageId\032msg
     * @param ctx 可使用的客户端连接对象
     * @param msg 获得的消息，前置解码器解码后的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String received = (String) msg;
        receivedMsgHandler(received,ctx);
        super.channelRead(ctx, msg);
    }

    /**
     * 消息处理
     * @param received 消息
     */
    private void receivedMsgHandler(String received, ChannelHandlerContext ctx){
        if(received.equals("DecodeError")){
            ForwardService.uForward.shutdownClient(ctx);
        }else {
            try{
                String[] receiveds = received.split("\032");
                String clientName = receiveds[0];
                String tag = receiveds[1];
                if(tag.equals("kcang")){
                    //心跳
                    if(ForwardService.uForward.clientContains(clientName)){
                        ForwardService.uForward.updateHealthy(ctx);
                    }else {
                        ForwardService.uForward.addClient(new ForwardClient(clientName,ctx));
                    }
                }else {
                    //收到客户端转发的response
                    String msg = receiveds[2];
                    ForwardService.sendToPublicClient(tag,msg);
                    myLogger.debug("获取待转发消息id： "+tag);
                    myLogger.debug("获取待转发消息： \n"+msg);
                }
            }catch (Exception e){
                //e.printStackTrace();
                myLogger.error(e.toString());
            }
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ForwardService.uForward.delClient(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ForwardService.uForward.delClient(ctx);
    }

}
