package com.dvlasenko.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public final class NettyClient {

    static final String HOST = "127.0.0.1";
    static final int PORT = 56000;

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            System.out.println("Client started and waiting for messages...");
            Bootstrap b = new Bootstrap();
            ClientHandler clientHandler = new ClientHandler();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(clientHandler);
                        }
                    });
            ChannelFuture f = b.connect(HOST, PORT).sync();
            Scanner scanner = new Scanner(System.in);
            Channel channel = f.sync().channel();
            while (true) {
                String input = scanner.nextLine();
                channel.writeAndFlush(input);
                channel.flush();
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
