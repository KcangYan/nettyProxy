package com.kcang.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class NettyClientProperties {
    private static String PublicTcpAddress;
    private static int PublicTcpPort;
    private static String PrivateHttpAddress;
    private static int PrivateHttpPort;
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
            PublicTcpAddress = prop.getProperty("PublicTcpAddress");
            PublicTcpPort = Integer.parseInt(prop.getProperty("PublicTcpPort"));//加载公网tcp服务端口
            PrivateHttpAddress = prop.getProperty("PrivateHttpAddress");
            PrivateHttpPort = Integer.parseInt(prop.getProperty("PrivateHttpPort"));//加载要转发的本地http服务端口
            ClientName = prop.getProperty("ClientName");
            myLogger.info("nettyClient.properties配置文件加载成功");
        }catch (Exception e){
            AesKey = "kcang12346890123";
            PublicTcpAddress = "127.0.0.1";
            PublicTcpPort = 19191;
            PrivateHttpAddress = "127.0.0.1";
            PrivateHttpPort = 9191;
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

    public String getPublicTcpAddress() {
        return PublicTcpAddress;
    }

    public void setPublicTcpAddress(String publicTcpAddress) {
        PublicTcpAddress = publicTcpAddress;
    }

    public int getPublicTcpPort() {
        return PublicTcpPort;
    }

    public void setPublicTcpPort(int publicTcpPort) {
        PublicTcpPort = publicTcpPort;
    }

    public String getPrivateHttpAddress() {
        return PrivateHttpAddress;
    }

    public void setPrivateHttpAddress(String privateHttpAddress) {
        PrivateHttpAddress = privateHttpAddress;
    }

    public int getPrivateHttpPort() {
        return PrivateHttpPort;
    }

    public void setLocalHttpPort(int privateHttpPort) {
        PrivateHttpPort = privateHttpPort;
    }

    public String getAesKey() {
        return AesKey;
    }

    public void setAesKey(String aesKey) {
        AesKey = aesKey;
    }
}
