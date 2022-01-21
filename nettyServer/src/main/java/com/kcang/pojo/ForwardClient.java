package com.kcang.pojo;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
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
    //连接通道
    private ChannelHandlerContext ctx;

    public ForwardClient(String clientName,ChannelHandlerContext ctx){
        this.clientName = clientName;
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        this.address = ipSocket.getAddress().getHostAddress();
        this.port = ipSocket.getPort();
        this.ctx = ctx;
        this.message = new Vector<>();
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
