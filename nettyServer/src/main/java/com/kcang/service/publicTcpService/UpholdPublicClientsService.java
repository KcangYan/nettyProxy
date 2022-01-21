package com.kcang.service.publicTcpService;

import com.kcang.pojo.PublicClient;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UpholdPublicClientsService {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());
    //维护的map，根据连接id标识
    private Map<String, PublicClient> clients;
    public UpholdPublicClientsService(){
        this.clients = new ConcurrentHashMap<>();
    }

    /**
     * 添加公网请求端
     * @param publicClient
     */
    public void addClient(PublicClient publicClient){
        this.clients.put(publicClient.getId(),publicClient);
        myLogger.info("新增公网请求: "+publicClient.toString());
    }
    public void addClient(ChannelHandlerContext ctx){
        addClient(new PublicClient(ctx));
    }

    /**
     * 删除公网请求端
     * @param publicClient
     */
    public void delClient(PublicClient publicClient){
        this.clients.remove(publicClient.getId());
        myLogger.info("移除公网请求: "+publicClient.toString());
    }
    public void delClient(String address,int port){
        String id = null;
        for(String key : this.clients.keySet()){
            if(this.clients.get(key).equals(address,port)){
                id = key;
                break;
            }
        }
        delClient(this.clients.get(id));
    }
    public void delClient(ChannelHandlerContext ctx){
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        int clientPort = ipSocket.getPort();
        delClient(clientIp,clientPort);
    }

    /**
     * 获取公网请求端实例
     * @param id ID
     * @return
     */
    public PublicClient get(String id){
        return this.clients.get(id);
    }
    public PublicClient get(String address,int port){
        String id = null;
        for(String item:this.clients.keySet()){
            if(this.clients.get(item).equals(address,port)){
                id = item;
                break;
            }
        }
        return get(id);
    }
    public PublicClient get(ChannelHandlerContext ctx){
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        int clientPort = ipSocket.getPort();
        return get(clientIp,clientPort);
    }
}
