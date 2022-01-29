package com.kcang.service.data;

import com.kcang.service.privateTcpService.PrivateTcpService;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataForwardService {
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    //维护与本地服务建立的链接对象
    private static ConcurrentHashMap<String,ChannelHandlerContext> privateClients = new ConcurrentHashMap<String,ChannelHandlerContext>();

    private static ConcurrentHashMap<String, List<String>> messageFailList = new ConcurrentHashMap<>();

    private static void execute(String id,ChannelHandlerContext ctx){
        if(!privateClients.containsKey(id)) {
            executorService.execute(new PrivateTcpService(id,ctx));
            privateClients.put(id,null);
        }
    }

    public static void sendMsg(String id,String msg, ChannelHandlerContext ctx){
        if(privateClients.containsKey(id)){
            sendMsg(id,msg);
        }else {
            execute(id,ctx);
            addFailMessage(id,msg);
        }
    }
    private static void sendMsg(String id,String msg){
        if(privateClients.get(id) != null){
            ChannelHandlerContext channelHandlerContext = privateClients.get(id);
            channelHandlerContext.writeAndFlush(msg);
        }else {
            addFailMessage(id,msg);
        }
    }
    public static void addClient(String id, ChannelHandlerContext ctx){
        privateClients.put(id,ctx);
        failMessageHandler(id);
    }
    public static void delClient(String id){
        privateClients.remove(id);
    }

    public static void addFailMessage(String id, String msg){
        if(messageFailList.containsKey(id)){
            messageFailList.get(id).add(msg);
        }else {
            ArrayList<String> message = new ArrayList<String>();
            message.add(msg);
            messageFailList.put(id,message);
        }
    }
    private static void failMessageHandler(String id){
        if(messageFailList.containsKey(id)){
            List<String> msgs = messageFailList.get(id);
            for(String msg : msgs){
                sendMsg(id,msg);
            }
            messageFailList.remove(id);
        }
    }
}
