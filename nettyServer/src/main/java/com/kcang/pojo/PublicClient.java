package com.kcang.pojo;

import com.kcang.decode.PublicTcpDecode;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Random;

public class PublicClient {
    private String address;
    private Integer port;
    private String id;
    //连接通道对象
    private ChannelHandlerContext ctx;

    public PublicClient(ChannelHandlerContext ctx){
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        this.address = ipSocket.getAddress().getHostAddress();
        this.port = ipSocket.getPort();
        this.ctx = ctx;
        this.id = this.getRandomString();
    }

    @Override
    public String toString() {
        return "PublicClient{" +
                "address='" + address + '\'' +
                ", port=" + port +
                ", id='" + id + '\'' +
                '}';
    }
    public boolean equals(String address,int port){
        return address.equals(this.address) && this.port.equals(port);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    //生成id
    private String getRandomString(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<16;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
