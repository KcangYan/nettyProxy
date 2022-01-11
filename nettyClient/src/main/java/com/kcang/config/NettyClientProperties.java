package com.kcang.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class NettyClientProperties {
    private static String PublicTcpAddress;
    private static int PublicTcpPort;
    private static String LocalHttpAddress;
    private static int LocalHttpPort;
    private static String ClientName;
    private static String AesKey;
    private static Logger myLogger = LoggerFactory.getLogger(NettyClientProperties.class);
    static {
        try{
            myLogger.info("正在加载nettyClient.properties配置文件");
            Properties prop = new Properties();
            prop.load(new FileInputStream("nettyServer.properties"));
            AesKey = prop.getProperty("AesKey");//加载AES密钥
            AesKey = aesKeyCompletion(AesKey);//补全
            PublicTcpAddress = prop.getProperty("PublicTcpAddress");
            PublicTcpPort = Integer.parseInt(prop.getProperty("PublicTcpPort"));//加载公网tcp服务端口
            LocalHttpAddress = prop.getProperty("LocalHttpAddress");
            LocalHttpPort = Integer.parseInt(prop.getProperty("LocalHttpPort"));//加载要转发的本地http服务端口
            ClientName = prop.getProperty("ClientName");
            myLogger.info("nettyClient.properties配置文件加载成功");
        }catch (Exception e){
            AesKey = "kcang12346890123";
            PublicTcpAddress = "127.0.0.1";
            PublicTcpPort = 19191;
            LocalHttpAddress = "127.0.0.1";
            LocalHttpPort = 9191;
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

    public String getLocalHttpAddress() {
        return LocalHttpAddress;
    }

    public void setLocalHttpAddress(String localHttpAddress) {
        LocalHttpAddress = localHttpAddress;
    }

    public int getLocalHttpPort() {
        return LocalHttpPort;
    }

    public void setLocalHttpPort(int localHttpPort) {
        LocalHttpPort = localHttpPort;
    }

    public String getAesKey() {
        return AesKey;
    }

    public void setAesKey(String aesKey) {
        AesKey = aesKey;
    }
}
