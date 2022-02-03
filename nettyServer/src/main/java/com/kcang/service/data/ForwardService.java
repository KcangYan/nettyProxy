package com.kcang.service.data;

import com.kcang.pojo.ForwardClient;
import com.kcang.service.privateTcpService.UpholdForwardClientsService;
import com.kcang.service.publicTcpService.UpholdPublicClientsService;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForwardService {
    private static Logger myLogger = LoggerFactory.getLogger(ForwardService.class);
    /**
     * 实例化内网转发客户端队列维护服务
     */
    public static UpholdForwardClientsService uForward;
    /**
     * 实例化公网转发连接端队列维护服务
     */
    public static UpholdPublicClientsService uPublic;

    public static void sendToForwardClient(ForwardClient forwardClient,Object msg){
        forwardClient.getChannelHandlerContext().writeAndFlush(msg);
        myLogger.debug("发送消息: "+msg.toString()+" "+forwardClient.toString());
    }
    public static void sendToForwardClient(String clientName,Object msg){
        ForwardClient forwardClient = uForward.get(clientName);
        if(forwardClient != null && forwardClient.getClientStatus()){
            sendToForwardClient(forwardClient,msg);
        }else {
            myLogger.error("转发消息失败 客户端不存在或不在线 "+msg.toString());
        }
    }

    /**
     * 仅限单客户端模式使用
     * @param msg
     */
    public static void sendToForwardClient(Object msg){
        sendToForwardClient(ForwardService.uForward.getFirst(),msg);
    }

    public static void sendToPublicClient(String id, String msg){
        uPublic.get(id).getCtx().writeAndFlush(Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8)));
    }
}
