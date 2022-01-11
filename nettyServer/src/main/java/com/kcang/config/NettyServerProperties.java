package com.kcang.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class NettyServerProperties {
    private static int ExternalHttpPort;
    private static int InsideTcpPort;
    private static String AesKey;
    private static Logger myLogger = LoggerFactory.getLogger(NettyServerProperties.class);
    static {
        try{
            myLogger.info("正在加载nettyServer.properties配置文件");
            Properties prop = new Properties();
            prop.load(new FileInputStream("nettyServer.properties"));
            AesKey = prop.getProperty("AesKey");//加载AES密钥
            AesKey = aesKeyCompletion(AesKey);//补全
            ExternalHttpPort = Integer.parseInt(prop.getProperty("ExternalHttpPort"));//加载对外http服务端口
            InsideTcpPort = Integer.parseInt(prop.getProperty("InsideTcpPort"));//加载服务端与客户端tcp通信端口
            myLogger.info("nettyServer.properties配置文件加载成功");
        }catch (Exception e){
            AesKey = "kcang12346890123";
            ExternalHttpPort = 19191;
            InsideTcpPort = 9191;
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

    public int getExternalHttpPort() {
        return ExternalHttpPort;
    }

    public void setExternalHttpPort(int externalHttpPort) {
        ExternalHttpPort = externalHttpPort;
    }

    public int getInsideTcpPort() {
        return InsideTcpPort;
    }

    public void setInsideTcpPort(int insideTcpPort) {
        InsideTcpPort = insideTcpPort;
    }

    public String getAesKey() {
        return AesKey;
    }

    public void setAesKey(String aesKey) {
        AesKey = aesKey;
    }
}
