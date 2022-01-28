package com.kcang.service;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartBeatService implements Runnable {
    private String beatMsg;
    private ChannelHandlerContext ctx;
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());
    public HeartBeatService(ChannelHandlerContext ctx, String beatMsg){
        this.ctx = ctx;
        this.beatMsg = beatMsg;
    }
    @Override
    public void run() {
        ctx.writeAndFlush(beatMsg);
        while (true){
            try {
                Thread.sleep(5000);
                ctx.writeAndFlush(beatMsg);
            }catch (Exception e){
                myLogger.error(e.toString());
                myLogger.error("心跳线程结束！");
                break;
            }
        }
    }
}
