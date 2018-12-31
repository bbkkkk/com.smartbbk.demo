package com.smartbbk.netty.channel.demo1.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by sherry on 16/11/5.
 */
public class TimerServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new TimerServerHandler());
    }
}