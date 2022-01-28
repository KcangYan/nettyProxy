package com.kcang.decode;

import com.kcang.config.NettyClientProperties;
import com.kcang.template.DecodeTemplate;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PublicTcpDecode extends DecodeTemplate {
    private static Logger myLogger = LoggerFactory.getLogger(PublicTcpDecode.class);

    /**
     * 解析服务端传过来的消息，消息格式为 id\032msg\001
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
            if(NettyClientProperties.getAesOpen()){
                return super.decryptAES(msg,NettyClientProperties.getAesKey());
            }else {
                return msg;
            }
        }catch (Exception e){
            return "DecodeError";
        }
    }
}
