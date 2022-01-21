package com.kcang.service.privateTcpService;

import com.kcang.pojo.ForwardClient;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 维护注册的客户端，允许同一个服务名注册多个客户端
 */
public class UpholdForwardClientsService {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());
    //维护的map，根据客户端的名字标识
    private Map<String,ForwardClient> forwardClients;
    public UpholdForwardClientsService(){
        this.forwardClients = new ConcurrentHashMap<>();
    }

    /**
     * 加入新客户端
     * @param forwardClient 客户端实例
     */
    public void addClient(ForwardClient forwardClient){
        updateHealthy(forwardClient);
        myLogger.info("新客户端加入 "+forwardClient.toString());
    }

    /**
     * 客户端是否已存在
     * @param clientName 客户端命名
     * @return
     */
    public boolean clientContains(String clientName){
        return this.forwardClients.containsKey(clientName);
    }

    /**
     * 更新客户端
     * @param forwardClient 客户端实例
     */
    public void updateHealthy(ForwardClient forwardClient){
        forwardClient.setHealthyTimes(System.currentTimeMillis());
        forwardClient.setClientStatus(true);
        this.forwardClients.put(forwardClient.getClientName(),forwardClient);
        myLogger.debug("心跳维护成功 "+forwardClient.toString());
    }
    public void updateHealthy(String address, int port){
        ForwardClient forwardClient = null;
        for(String key:this.forwardClients.keySet()){
            forwardClient = this.forwardClients.get(key);
            if(forwardClient.forwardClientEquals(address,port)){
                break;
            }
        }
        if(forwardClient != null){
            updateHealthy(forwardClient);
        }
    }
    public void updateHealthy(ChannelHandlerContext ctx){
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        int clientPort = ipSocket.getPort();
        updateHealthy(clientIp,clientPort);
    }
    /**
     * 关闭客户端
     * @param forwardClient 客户端实例
     */
    public void shutdownClient(ForwardClient forwardClient){
        forwardClient.setClientStatus(false);
        forwardClient.getChannelHandlerContext().disconnect();
        this.forwardClients.put(forwardClient.getClientName(),forwardClient);
        myLogger.info("关闭客户端实例: "+forwardClient.toString());
    }
    public void shutdownClient(String address, int port){
        ForwardClient forwardClient = null;
        for(String key:this.forwardClients.keySet()){
            forwardClient = this.forwardClients.get(key);
            if(forwardClient.forwardClientEquals(address,port)){
                break;
            }
        }
        if(forwardClient != null){
            shutdownClient(forwardClient);
        }
    }
    public void shutdownClient(ChannelHandlerContext ctx){
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        int clientPort = ipSocket.getPort();
        shutdownClient(clientIp,clientPort);
    }

    /**
     * 移除客户端
     * @param forwardClient 客户端实例
     */
    public void delClient(ForwardClient forwardClient){
        this.forwardClients.remove(forwardClient.getClientName());
        myLogger.info("客户端移除: "+forwardClient.toString());
    }
    public void delClient(String address, int port){
        ForwardClient forwardClient = null;
        for(String key:this.forwardClients.keySet()){
            forwardClient = this.forwardClients.get(key);
            if(forwardClient.forwardClientEquals(address,port)){
                break;
            }
        }
        if(forwardClient != null){
            delClient(forwardClient);
        }
    }
    public void delClient(ChannelHandlerContext ctx){
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        int clientPort = ipSocket.getPort();
        delClient(clientIp,clientPort);
    }

    /**
     * 获取客户端对象
     * @param clientName 客户端名字
     * @return
     */
    public ForwardClient get(String clientName){
        return this.forwardClients.get(clientName);
    }
    public ForwardClient get(String address, int port){
        String name = null;
        for(String key:this.forwardClients.keySet()){
            if(this.forwardClients.get(key).forwardClientEquals(address,port)){
                name = key;
                break;
            }
        }
        return get(name);
    }
    public ForwardClient get(ChannelHandlerContext ctx){
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        int clientPort = ipSocket.getPort();
        return get(clientIp,clientPort);
    }
}
