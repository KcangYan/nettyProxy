package com.kcang.handler.privateTcp;

import com.kcang.pojo.ForwardClient;
import com.kcang.service.data.DataForwardService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleClientPrivateTcpInboundHandler extends ChannelInboundHandlerAdapter {

    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获得消息时触发的方法
     * 当模式为多客户端时，收到的心跳信息 为 clientName\003kcang 转发信息为 clientName\003httpMessage
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
            DataForwardService.uForward.shutdownClient(ctx);
        }else {
            try{
                String[] receiveds = received.split("\003");
                String clientName = receiveds[0];
                String msg = receiveds[1];
                if(msg.equals("kcang")){
                    //心跳
                    if(DataForwardService.uForward.clientContains(clientName)){
                        DataForwardService.uForward.updateHealthy(ctx);
                    }else {
                        DataForwardService.uForward.addClient(new ForwardClient(clientName,ctx));
                    }
                }else {
                    //收到客户端转发的response
                }
            }catch (Exception e){
                myLogger.error(e.toString());
            }
        }
    }

}
