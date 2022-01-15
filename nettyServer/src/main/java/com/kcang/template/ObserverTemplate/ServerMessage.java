package com.kcang.template.ObserverTemplate;

import com.kcang.pojo.ForwardClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 存储待转发的消息，有消息以后通知函数去处理
 */
public class ServerMessage {
    //注册回调者列表
    private List<Observer> observers;
    private HashMap<String, ForwardClient> clients;

    public ServerMessage(){
        this.observers = new ArrayList<Observer>();
        this.clients = new HashMap<>();
    }

    public synchronized void setClients(ForwardClient forwardClient){
        clients.put(forwardClient.getClientName(), forwardClient);
        notifyObservers();
    }

    /**
     * 注册回调对象
     * @param observer 回调对象
     * @return this
     */
    public ServerMessage addObserver(Observer observer){
        this.observers.add(observer);
        return this;
    }
    private void notifyObservers(){
        for(Observer observer : this.observers){
            observer.collBack();
        }
    }
}
