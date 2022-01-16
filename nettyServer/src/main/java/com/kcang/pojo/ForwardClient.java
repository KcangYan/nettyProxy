package com.kcang.pojo;

import com.kcang.template.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ForwardClient {
    private String address;
    private String clientName;
    private Integer port;
    //待转发的消息
    private List<String> message;
    //客户端上次心跳时间戳
    private Integer healthyTimes;
    //客户端是否在线
    private Boolean clientStatus;
    //注册回调者列表
    private List<Observer> observers;

    @Override
    public String toString() {
        return "ForwardClient{" +
                "address='" + address + '\'' +
                ", clientName='" + clientName + '\'' +
                ", port=" + port +
                ", message=" + message +
                ", healthyTimes=" + healthyTimes +
                ", clientStatus=" + clientStatus +
                '}';
    }

    public ForwardClient(){
        this.message = new Vector<>();
        this.observers = new ArrayList<>();
    }

    /**
     * 添加该对象的观察者
     * @param observer 观察者对象
     * @return this
     */
    public ForwardClient addObservers(Observer observer){
        this.observers.add(observer);
        return this;
    }
    /**
     * 通知所有的观察者，本对象有变化
     */
    private void notifyObservers(){
        for(Observer observer : this.observers){
            observer.collBack(this);
        }
    }

    public Boolean getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(Boolean clientStatus) {
        this.clientStatus = clientStatus;
    }

    public Integer getHealthyTimes() {
        return healthyTimes;
    }

    public void setHealthyTimes(Integer healthyTimes) {
        this.healthyTimes = healthyTimes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
