package com.kcang.service.privateTcpService;

import com.kcang.pojo.ForwardClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 维护注册的客户端，允许同一个服务名注册多个客户端
 */
public class UpholdForwardClientsService {
    //维护的map，根据客户端的名字标识
    private Map<String,List<ForwardClient>> forwardClients;
    public UpholdForwardClientsService(){
        this.forwardClients = new ConcurrentHashMap<>();
    }

    /**
     * 加入新客户端
     * @param forwardClient 客户端实例
     */
    public void setForwardClient(ForwardClient forwardClient){
        if(this.forwardClients.containsKey(forwardClient.getClientName())){
            List<ForwardClient> pass = this.forwardClients.get(forwardClient.getClientName());
            pass.add(forwardClient);
            this.forwardClients.put(forwardClient.getClientName(),pass);
        }else {
            List<ForwardClient> pass = new Vector<>();
            pass.add(forwardClient);
            this.forwardClients.put(forwardClient.getClientName(),pass);
        }
    }

    public void updateHealthy(String clientName,String address, int port, Integer healthyTime){
        List<ForwardClient> pass = this.forwardClients.get(clientName);

    }
}
