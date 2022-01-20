package com.kcang.service.privateTcpService;

import com.kcang.pojo.ForwardClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void addForwardClient(ForwardClient forwardClient){
        updateHealthy(forwardClient);
        myLogger.info("新客户端加入 "+forwardClient.toString());
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

    /**
     * 关闭客户端
     * @param forwardClient 客户端实例
     */
    public void shutdownForwardClient(ForwardClient forwardClient){
        ForwardClient client = this.forwardClients.get(forwardClient.getClientName());
        client.setClientStatus(false);
        client.getChannelHandlerContext().disconnect();
        this.forwardClients.put(forwardClient.getClientName(),forwardClient);
    }
    public void shutdownForwardClient(String address, int port){
        ForwardClient forwardClient = null;
        for(String key:this.forwardClients.keySet()){
            forwardClient = this.forwardClients.get(key);
            if(forwardClient.forwardClientEquals(address,port)){
                break;
            }
        }
        if(forwardClient != null){
            shutdownForwardClient(forwardClient);
        }
    }

    /**
     * 移除客户端
     * @param forwardClient 客户端实例
     */
    public void delForwardClient(ForwardClient forwardClient){
        this.forwardClients.remove(forwardClient.getClientName());
        myLogger.info("客户端移除 "+forwardClient.toString());
    }
    public void delForwardClient(String address, int port){
        ForwardClient forwardClient = null;
        for(String key:this.forwardClients.keySet()){
            forwardClient = this.forwardClients.get(key);
            if(forwardClient.forwardClientEquals(address,port)){
                break;
            }
        }
        if(forwardClient != null){
            delForwardClient(forwardClient);
        }
    }
}
