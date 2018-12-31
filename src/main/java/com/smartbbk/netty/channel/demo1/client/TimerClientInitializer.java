package com.smartbbk.netty.channel.demo1.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by sherry on 16/11/5.
 */
public class TimerClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new TimerClientHandler());
    }
}