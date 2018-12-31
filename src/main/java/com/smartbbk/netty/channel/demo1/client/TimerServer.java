package com.smartbbk.netty.channel.demo1.client;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间服务器服务端
 * Created by sherry on 16/11/5.
 */
public class TimerServer {
    /**
     * 服务端绑定端口号
     */
    private int PORT;

    public TimerServer(int PORT) {
        this.PORT = PORT;
    }

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(TimerServer.class);

    public void bind() {
        /*
        NioEventLoopGroup是线程池组
        包含了一组NIO线程,专门用于网络事件的处理
        bossGroup:服务端,接收客户端连接
        workGroup:进行SocketChannel的网络读写
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            /*
            ServerBootstrap:用于启动NIO服务的辅助类,目的是降低服务端的开发复杂度
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)//配置TCP参数,能够设置很多,这里就只设置了backlog=1024,
                    .childHandler(new TimerServerInitializer());//绑定I/O事件处理类
            logger.debug("绑定端口号:" + PORT + ",等待同步成功");
            /*
            bind:绑定端口
            sync:同步阻塞方法,等待绑定完成,完成后返回 ChannelFuture ,主要用于通知回调
             */
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            logger.debug("等待服务端监听窗口关闭");
            /*
             closeFuture().sync():为了阻塞,服务端链路关闭后才退出.也是一个同步阻塞方法
             */
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            logger.debug("优雅退出,释放线程池资源");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        TimerServer server=new TimerServer(8899);
        server.bind();
    }
}