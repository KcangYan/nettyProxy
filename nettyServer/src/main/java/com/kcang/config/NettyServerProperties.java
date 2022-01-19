package com.kcang.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class NettyServerProperties {
    private static int PublicPort;
    private static int PrivatePort;
    private static boolean HttpProxy;
    private static boolean AesOpen;
    private static String AesKey;
    private static Logger myLogger = LoggerFactory.getLogger(NettyServerProperties.class);

    static {
        try{
            myLogger.info("正在加载nettyServer.properties配置文件");
            Properties prop = new Properties();
            prop.load(new FileInputStream("nettyServer.properties"));
            HttpProxy = Boolean.parseBoolean(prop.getProperty("HttpProxy"));
            AesOpen = Boolean.parseBoolean(prop.getProperty("AesOpen"));
            AesKey = prop.getProperty("AesKey");//加载AES密钥
            AesKey = aesKeyCompletion(AesKey);//补全
            PublicPort = Integer.parseInt(prop.getProperty("PublicPort"));//加载对外http服务端口
            PrivatePort = Integer.parseInt(prop.getProperty("PrivatePort"));//加载服务端与客户端tcp通信端口
            myLogger.info("nettyServer.properties配置文件加载成功");
        }catch (Exception e){
            AesKey = "kcang12346890123";
            PublicPort = 19191;
            PrivatePort = 9191;
            //e.printStackTrace();
            myLogger.error("配置文件加载失败: "+e.toString()+ " 启用默认配置");
        }
    }
    private static String aesKeyCompletion(String aesKey){
        String template = "kcang12346890123";
        if(aesKey.length()>16){
            return aesKey.substring(0,16);
        }else if(aesKey.length()<16){
            return aesKey+template.substring(0,16-aesKey.length());
        }else {
            return aesKey;
        }
    }

    public int getPublicPort() {
        return PublicPort;
    }

    public void setPublicPort(int publicPort) {
        PublicPort = publicPort;
    }

    public int getPrivatePort() {
        return PrivatePort;
    }

    public void setPrivatePort(int privatePort) {
        PrivatePort = privatePort;
    }

    public String getAesKey() {
        return AesKey;
    }

    public void setAesKey(String aesKey) {
        AesKey = aesKey;
    }

    public boolean isHttpProxy() {
        return HttpProxy;
    }

    public void setHttpProxy(boolean httpProxy) {
        HttpProxy = httpProxy;
    }

    public boolean isAesOpen() {
        return AesOpen;
    }

    public void setAesOpen(boolean aesOpen) {
        AesOpen = aesOpen;
    }
}
