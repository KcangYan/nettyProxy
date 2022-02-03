package com.kcang.decode;

import com.kcang.config.NettyServerProperties;
import com.kcang.template.DecodeTemplate;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 与客户端通信的解码器，负责获取客户端的发送的信息
 */
public class PrivateTcpDecode extends DecodeTemplate {
    private static Logger myLogger = LoggerFactory.getLogger(PrivateTcpDecode.class);
    /**
     * 若开启aes加密认证，则消息会通过aes解密，解密失败则会告诉handler AesDecodeError 让他踢出客户端
     * 内置消息格式为 \001 结尾
     */
    private String messages = "";
    private String end = "\001";
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf bufMsg = in.readBytes(in.readableBytes());
        String getMsg = messages + bufMsg.toString(CharsetUtil.UTF_8);
        myLogger.debug("获得消息: "+getMsg);
        String[] ss;
        while (getMsg.contains(end)){
            //切割字符串，可以适应多条消息粘结在一起的情况
            ss = getMsg.split(end,2);
            out.add(outMessage(ss[0]));
            getMsg = ss[1];
        }
        messages = getMsg;
    }

    private String outMessage(String msg) throws Exception {
        try{
            if(NettyServerProperties.isAesOpen()){
                return super.decryptAES(msg,NettyServerProperties.getAesKey());
            }else {
                return msg;
            }
        }catch (Exception e){
            return "DecodeError";
        }
    }
}
