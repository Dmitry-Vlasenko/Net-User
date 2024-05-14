package com.dvlasenko.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CountDownLatch;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
//    private CountDownLatch latch;
//
//    public void setLatch(CountDownLatch latch) {
//        this.latch = latch;
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("SERVER: " + msg);
//        latch.countDown(); // Signal that the server's response has been received
    }
}