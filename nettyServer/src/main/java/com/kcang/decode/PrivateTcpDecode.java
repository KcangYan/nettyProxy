package com.kcang.decode;

import com.kcang.config.NettyServerProperties;
import com.kcang.template.DecodeTemplate;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * 与客户端通信的解码器，负责获取客户端的发送的信息
 */
public class PrivateTcpDecode extends DecodeTemplate {

    /**
     * 若开启aes加密认证，则消息会通过aes解密，解密失败则会告诉handler AesDecodeError 让他踢出客户端
     * 内置消息格式为 \001 开头 ----- \002 结尾
     */
    private String messages = "";
    private String start = "\001";
    private String end = "\002";
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf bufMsg = in.readBytes(in.readableBytes());
        String getMsg = bufMsg.toString(CharsetUtil.UTF_8);
        if(messages.equals("")){
            //防止黏包
            if(getMsg.contains(start) && getMsg.contains(end)){
                out.add(outMessage(getMsg.replaceAll("\001","").replaceAll("\002","")));
            }else if(getMsg.contains(start)){
                messages = getMsg;
            }else {
                out.add("DecodeError");
            }
        }else {
            //防止半包
            getMsg = messages + getMsg;
            if(getMsg.contains(end)){
                out.add(outMessage(getMsg.replaceAll("\001","").replaceAll("\002","")));
                messages = "";
            }else {
                messages = getMsg;
                ctx.read();
            }
        }
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
