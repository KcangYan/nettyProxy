package com.kcang.config;

import com.kcang.annotation.BeanConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

@BeanConfiguration
public class NettyServerProperties {
    //内部通信的端口
    private int serverPort;
    //最大服务端数量
    private int maxServerNum;
    //单个服务端下最大连接数
    private int maxConnectNum;
    //单个服务端下最小连接数
    private int minConnectNum;

    public NettyServerProperties(){
        Logger myLogger = LoggerFactory.getLogger(NettyServerProperties.class);
        try{
            myLogger.info("正在加载nettyServer.properties配置文件");
            Properties prop = new Properties();
            prop.load(new FileInputStream("nettyServer.properties"));
            this.serverPort = Integer.parseInt(prop.getProperty("serverPort"));
            this.maxServerNum = Integer.parseInt(prop.getProperty("maxServerNum"));
            this.maxConnectNum = Integer.parseInt(prop.getProperty("maxConnectNum"));
            this.minConnectNum = Integer.parseInt(prop.getProperty("maxConnectNum"));
            myLogger.info("nettyServer.properties配置文件加载成功");
        }catch (Exception e){
            this.serverPort = 19191;
            this.maxServerNum = 5;
            this.maxConnectNum = 10;
            this.minConnectNum = 1;
            //e.printStackTrace();
            myLogger.error("配置文件加载失败: "+e.toString()+ " 启用默认配置");
        }
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getMaxServerNum() {
        return maxServerNum;
    }

    public void setMaxServerNum(int maxServerNum) {
        this.maxServerNum = maxServerNum;
    }

    public int getMaxConnectNum() {
        return maxConnectNum;
    }

    public void setMaxConnectNum(int maxConnectNum) {
        this.maxConnectNum = maxConnectNum;
    }

    public int getMinConnectNum() {
        return minConnectNum;
    }

    public void setMinConnectNum(int minConnectNum) {
        this.minConnectNum = minConnectNum;
    }
}
