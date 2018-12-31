package com.smartbbk.netty.channel.demo1.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * Created by sherry on 16/11/5.
 */
public class TimerClientHandler extends ChannelHandlerAdapter {
    private  ChannelHandlerContext ctx;
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(TimerClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("客户端连接上了服务端");
        this.ctx=ctx;
        //发送请求
        ByteBuf reqBuf = getReq("GET TIME");

        ctx.writeAndFlush(reqBuf);
    }
    public void  sendMsg() throws UnsupportedEncodingException {
        logger.debug("send message");
        ByteBuf reqBuf = getReq("GET TIME");
        ctx.writeAndFlush(reqBuf);
    }

    /**
     * 将字符串包装成ByteBuf
     * @param s
     * @return
     */
    private ByteBuf getReq(String s) throws UnsupportedEncodingException {
        byte[] data = s.getBytes("UTF-8");
        ByteBuf reqBuf = Unpooled.buffer(data.length);
        reqBuf.writeBytes(data);
        return reqBuf;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String resStr = getRes(byteBuf);
        logger.debug("客户端收到:"+resStr);
    }

    private String getRes(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}