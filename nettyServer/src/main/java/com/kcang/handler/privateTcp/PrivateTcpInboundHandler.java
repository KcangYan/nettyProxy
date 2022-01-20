package com.kcang.handler.privateTcp;

import com.kcang.service.privateTcpService.UpholdForwardClientsService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class PrivateTcpInboundHandler extends ChannelInboundHandlerAdapter {

    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    //客户端维护服务
    private UpholdForwardClientsService upholdForwardClientsService;
    public PrivateTcpInboundHandler(UpholdForwardClientsService u){
        this.upholdForwardClientsService = u;
    }
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
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        int clientPort = ipSocket.getPort();
        receivedMsgHandler(received,clientIp,clientPort);
        super.channelRead(ctx, msg);
    }

    /**
     * 消息处理
     * @param received 消息
     * @param ip
     * @param port
     */
    private void receivedMsgHandler(String received, String ip, int port){
        if(received.equals("DecodeError")){
            upholdForwardClientsService.shutdownForwardClient(ip,port);
        }else {
            try{
                String[] receiveds = received.split("\003");
                String clientName = receiveds[0];
                String msg = receiveds[1];
                if(msg.equals("kcang")){
                    //心跳
                    upholdForwardClientsService.updateHealthy(ip,port);
                }else {

                }
            }catch (Exception e){
                myLogger.error(e.toString());
            }
        }
    }

}
