package com.kcang.handler.publicTcp;

import com.kcang.config.NettyClientProperties;
import com.kcang.service.HeartBeatService;
import com.kcang.service.data.DataForwardService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublicTcpInboundHandler extends ChannelInboundHandlerAdapter {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());

    /**
     * 收到公网服务端发来的明文消息 DecodeError 标识解码异常 正常消息: id\032msg
     * @param ctx 通道对象
     * @param msg 明文消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String getMsg = (String) msg;
        if(getMsg.equals("DecodeError")){
            myLogger.error("异常解码消息");
        }else {
            String[] msgs = getMsg.split("\032");
            DataForwardService.sendMsg(msgs[0],msgs[1],ctx);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        myLogger.info("公网服务端连接建立成功");
        //super.channelActive(ctx);
        myLogger.info("正在启动心跳线程");
        String beatMsg = NettyClientProperties.getClientName()+"\032kcang";
        Thread beat = new Thread(new HeartBeatService(ctx,beatMsg),"HeartBeatService");
        beat.start();
        myLogger.info("心跳线程启动成功");
    }
}
