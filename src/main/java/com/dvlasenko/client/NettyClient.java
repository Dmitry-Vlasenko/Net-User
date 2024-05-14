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
    static final int PORT = 8001;
    public static String option;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture f = b.connect(HOST, PORT).sync();
            Channel channel = f.sync().channel();
            f.channel().closeFuture().sync();
            try {
                while (true){
                option = scanner.nextLine();
                channel.writeAndFlush(option);
//                channel.flush();
            }}
            catch (Exception e){
                e.printStackTrace();
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
