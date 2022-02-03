package com.kcang.service.data;

import com.kcang.service.privateTcpService.PrivateTcpService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataForwardService {

    private static Logger myLogger = LoggerFactory.getLogger(DataForwardService.class);

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    //维护与本地服务建立的链接对象
    private static ConcurrentHashMap<String,Object> privateClients = new ConcurrentHashMap<String,Object>();

    private static ConcurrentHashMap<String, List<String>> messageList = new ConcurrentHashMap<>();

    private static void execute(String id,ChannelHandlerContext ctx){
        if(!privateClients.containsKey(id)) {
            executorService.execute(new PrivateTcpService(id,ctx));
            privateClients.put(id,"");
        }
    }

    public static void sendMsg(String id,String msg, ChannelHandlerContext ctx){
        if(privateClients.containsKey(id)){
            sendMsg(id,msg);
        }else {
            execute(id,ctx);
            addMessage(id,msg);
        }
    }
    private static void sendMsg(String id,String msg){
        if(privateClients.get(id) != ""){
            ChannelHandlerContext channelHandlerContext = (ChannelHandlerContext) privateClients.get(id);
            channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8)));
            myLogger.debug("发送消息："+msg);
        }else {
            addMessage(id,msg);
        }
    }
    public static void addClient(String id, ChannelHandlerContext ctx){
        privateClients.put(id,ctx);
        myLogger.info("增加转发任务: "+id);
        messageHandler(id);
    }
    public static void delClient(String id){
        privateClients.remove(id);
        myLogger.info("移除转发任务: "+id);
    }

    public static void addMessage(String id, String msg){
        if(messageList.containsKey(id)){
            messageList.get(id).add(msg);
        }else {
            ArrayList<String> message = new ArrayList<String>();
            message.add(msg);
            messageList.put(id,message);
        }
    }
    private static void messageHandler(String id){
        if(messageList.containsKey(id)){
            List<String> msgs = messageList.get(id);
            for(String msg : msgs){
                sendMsg(id,msg);
            }
            messageList.remove(id);
        }
    }
}
