package com.kcang.pojo;

import com.kcang.template.Observer;
import io.netty.channel.ChannelHandlerContext;

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
    private Long healthyTimes = null;
    //客户端是否在线
    private Boolean clientStatus = null;
    //注册回调者列表
    private List<Observer> observers;
    //连接通道
    private ChannelHandlerContext ctx;

    public ForwardClient(String clientName,String address, int port, ChannelHandlerContext ctx){
        this.clientName = clientName;
        this.address = address;
        this.port = port;
        this.ctx = ctx;
        this.message = new Vector<>();
        this.observers = new ArrayList<>();
    }

    /**
     * 客户端比较方法
     * @param obj 客户端
     * @return
     */
    public boolean forwardClientEquals(ForwardClient obj) {
        return this.getClientName().equals(obj.getClientName()) &&
                this.getAddress().equals(obj.getAddress()) &&
                this.getPort().equals(obj.getPort());
    }
    public boolean forwardClientEquals(String address, int port){
        return this.getAddress().equals(address) &&
                this.getPort().equals(port);
    }

    @Override
    public String toString() {
        return "ForwardClient{" +
                "address='" + address + '\'' +
                ", clientName='" + clientName + '\'' +
                ", port=" + port +
                ", healthyTimes=" + healthyTimes +
                ", clientStatus=" + clientStatus +
                '}';
    }

    /**
     * 关闭连接
     */
    public void shutdownClient(){
        this.ctx.disconnect();
    }

    /**
     * 获取客户端连接对象
     * @param ctx 客户端连接对象
     */
    public ChannelHandlerContext getChannelHandlerContext(){
        return this.ctx;
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

    /**
     * 心跳更新超过七秒 则认为无效
     * @return boolean
     */
    public Boolean getClientStatus() {
        if(this.clientStatus){
            if((System.currentTimeMillis()-healthyTimes) > 7*1000){
                return false;
            }
        }
        return clientStatus;
    }

    public void setClientStatus(Boolean clientStatus) {
        this.clientStatus = clientStatus;
    }

    public Long getHealthyTimes() {
        return healthyTimes;
    }

    public void setHealthyTimes(Long healthyTimes) {
        this.healthyTimes = healthyTimes;
    }

    public String getAddress() {
        return address;
    }

    public String getClientName() {
        return clientName;
    }

    public Integer getPort() {
        return port;
    }
}
