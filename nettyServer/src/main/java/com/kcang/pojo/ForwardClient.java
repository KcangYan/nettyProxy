package com.kcang.pojo;

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

    public ForwardClient(){
        this.message = new Vector<>();
    }

    public Boolean getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(Boolean clientStatus) {
        this.clientStatus = clientStatus;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
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
