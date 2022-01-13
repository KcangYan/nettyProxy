package com.kcang.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class NettyClientProperties {
    private static String PublicAddress;
    private static int PublicPort;
    private static String PrivateAddress;
    private static int PrivatePort;
    private static String ClientName;
    private static String AesKey;
    private static Logger myLogger = LoggerFactory.getLogger(NettyClientProperties.class);
    static {
        try{
            myLogger.info("正在加载nettyClient.properties配置文件");
            Properties prop = new Properties();
            prop.load(new FileInputStream("nettyClient.properties"));
            AesKey = prop.getProperty("AesKey");//加载AES密钥
            AesKey = aesKeyCompletion(AesKey);//补全
            PublicAddress = prop.getProperty("PublicAddress");
            PublicPort = Integer.parseInt(prop.getProperty("PublicPort"));//加载公网tcp服务端口
            PrivateAddress = prop.getProperty("PrivateAddress");
            PrivatePort = Integer.parseInt(prop.getProperty("PrivatePort"));//加载要转发的本地http服务端口
            ClientName = prop.getProperty("ClientName");
            myLogger.info("nettyClient.properties配置文件加载成功");
        }catch (Exception e){
            AesKey = "kcang12346890123";
            PublicAddress = "127.0.0.1";
            PublicPort = 19191;
            PrivateAddress = "127.0.0.1";
            PrivatePort = 9191;
            ClientName="ClientName";
            //e.printStackTrace();
            myLogger.error("配置文件加载失败: "+e.toString()+ " 启用默认配置");
        }
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
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

    public String getPublicAddress() {
        return PublicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        PublicAddress = publicAddress;
    }

    public int getPublicPort() {
        return PublicPort;
    }

    public void setPublicPort(int publicPort) {
        PublicPort = publicPort;
    }

    public String getPrivateAddress() {
        return PrivateAddress;
    }

    public void setPrivateAddress(String privateAddress) {
        PrivateAddress = privateAddress;
    }

    public int getPrivatePort() {
        return PrivatePort;
    }

    public void setLocalPort(int privatePort) {
        PrivatePort = privatePort;
    }

    public String getAesKey() {
        return AesKey;
    }

    public void setAesKey(String aesKey) {
        AesKey = aesKey;
    }
}
