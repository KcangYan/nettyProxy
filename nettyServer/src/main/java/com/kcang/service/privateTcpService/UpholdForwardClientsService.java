package com.kcang.service.privateTcpService;

import com.kcang.pojo.ForwardClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 维护注册的客户端，允许同一个服务名注册多个客户端
 */
public class UpholdForwardClientsService {
    private Logger myLogger = LoggerFactory.getLogger(this.getClass());
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
        myLogger.info("新客户端注册成功: "+forwardClient.toString());
    }

    public void updateHealthy(String clientName,String address, int port, Integer healthyTime){
        List<ForwardClient> pass = this.forwardClients.get(clientName);
        for (ForwardClient forwardClient : pass) {
            if (forwardClient.getPort().equals(port) && forwardClient.getAddress().equals(address)) {
                forwardClient.setHealthyTimes(healthyTime);
                forwardClient.setClientStatus(true);
                myLogger.debug("客户端心跳更新: "+forwardClient.toString());
            }
        }
    }
    public void downForwardClient(String clientName,String address, int port){
        List<ForwardClient> pass = this.forwardClients.get(clientName);
        for (ForwardClient forwardClient : pass) {
            if (forwardClient.getPort().equals(port) && forwardClient.getAddress().equals(address)) {
                forwardClient.setClientStatus(true);
                myLogger.info("关闭客户端: "+forwardClient.toString());
            }
        }
    }

    /**
     * 移除客户端
     * @param forwardClient 客户端对象
     */
    public void delForwardClient(String clientName, String address, int port){
        List<ForwardClient> pass = this.forwardClients.get(clientName);
        int i;
        for(i=0;i<pass.size();i++){
            if (pass.get(i).getPort().equals(port) && address.equals(pass.get(i).getAddress())) {
                break;
            }
        }
        this.forwardClients.get(clientName).remove(i);
        myLogger.info("移除客户端: "+clientName+" "+address+":"+port);
    }
}
