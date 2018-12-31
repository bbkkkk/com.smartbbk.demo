package com.smartbbk.netty.channel.demo1.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间服务器客户端
 * Created by sherry on 16/11/5.
 */
public class TimerClient {
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(TimerClient.class);

    private String HOST;
    private int PORT;

    public TimerClient(String HOST, int PORT) {
        this.HOST = HOST;
        this.PORT = PORT;
    }

    public void connect(){
        //配置客户端NIO线程组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new TimerClientInitializer());
            //发起异步连接操作
            logger.debug("发起异步连接操作 - start");
            ChannelFuture channelFuture = bootstrap.connect(HOST,PORT).sync();
            logger.debug("发起异步连接操作 - end");
            //等待客户端链路关闭
            logger.debug("等待客户端链路关闭 - start");
            channelFuture.channel().closeFuture().sync();
            logger.debug("等待客户端链路关闭 - end");
        } catch (InterruptedException e) {
            logger.error(e.getMessage(),e);
        }finally {
            //优雅的关闭
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        TimerClient client=new TimerClient("127.0.0.1",8899);
        client.connect();
    }
}