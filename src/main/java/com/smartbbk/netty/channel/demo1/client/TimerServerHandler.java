package com.smartbbk.netty.channel.demo1.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handler主要用于对网络事件进行读写操作,是真正的业务类
 * 通常只需要关注 channelRead 和 exceptionCaught 方法
 * Created by sherry on 16/11/5.
 */
public class TimerServerHandler extends ChannelHandlerAdapter {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(TimerServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //ByteBuf,类似于NIO中的ByteBuffer,但是更强大
        ByteBuf reqBuf = (ByteBuf) msg;
        //获取请求字符串
        String req = getReq(reqBuf);
        logger.debug("From:"+ctx.channel().remoteAddress());
        logger.debug("服务端收到:" + req);

        if ("GET TIME".equals(req)){
            String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date());
            String resStr = "当前时间:" + timeNow;

            //获取发送给客户端的数据
            ByteBuf resBuf = getRes(resStr);

            logger.debug("服务端应答数据:\n" + resStr);
            ctx.write(resBuf);
        }else {
            //丢弃
            logger.debug("丢弃");
            ReferenceCountUtil.release(msg);
        }


    }

    /**
     * 获取发送给客户端的数据
     *
     * @param resStr
     * @return
     */
    private ByteBuf getRes(String resStr) throws UnsupportedEncodingException {
        byte[] req = resStr.getBytes("UTF-8");
        ByteBuf pingMessage = Unpooled.buffer(req.length);
        //将字节数组信息写入到ByteBuf
        pingMessage.writeBytes(req);

        return pingMessage;
    }

    /**
     * 获取请求字符串
     *
     * @param buf
     * @return
     */
    private String getReq(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];
        //将ByteByf信息写出到字节数组
        buf.readBytes(con);
        try {
            return new String(con, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将消息发送队列中的消息写入到SocketChannel中发送给对方
        logger.debug("channelReadComplete");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发生异常时,关闭 ChannelHandlerContext,释放ChannelHandlerContext 相关的句柄等资源
        logger.error("exceptionCaught");
        ctx.close();
    }
}