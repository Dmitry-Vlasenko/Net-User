package com.dvlasenko.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import com.dvlasenko.app.service.UserService;

import java.util.*;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    static final List<Channel> channels = new ArrayList<>();
    final UserService service = new UserService();

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("Client joined - " + ctx);
        getMenu(ctx);
        channels.add(ctx.channel());
    }
    private void getMenu(ChannelHandlerContext ctx) {
        String menu = """
                OPTIONS:
                1 - Create user.
                2 - Read users.
                3 - Update user.
                4 - Delete user.
                5 - Read user by id.
                0 - Close the App.
                "Input your option: "
                """;
        ctx.writeAndFlush(menu);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        Map<String, String> map = new HashMap<>();
        int data = Integer.parseInt(msg);
        switch (data) {
            case 1:
                ctx.writeAndFlush("\nCREATE FORM");
                ctx.writeAndFlush("Input first name: ");
                map.put("first_name", msg);
                ctx.writeAndFlush("Input last name: ");
                map.put("last_name", msg);
                ctx.writeAndFlush("Input email in format example@mail.com: ");
                map.put("email", msg);
                service.create(map);
                break;
            case 2:
                ctx.writeAndFlush(service.read());
                break;
            case 3:
                System.out.println("\nUPDATE FORM");
                Scanner scanner = new Scanner(System.in);
                ctx.writeAndFlush("Input id: ");
                map.put("id", scanner.nextLine().trim());
                ctx.writeAndFlush("Input first name: ");
                map.put("first_name", scanner.nextLine().trim());
                ctx.writeAndFlush("Input last name: ");
                map.put("last_name", scanner.nextLine().trim());
                ctx.writeAndFlush("Input email in format example@mail.com: ");
                map.put("email", scanner.nextLine().trim());
                service.update(map);
                break;
            case 4:
                System.out.println("\nDELETE FORM");
                ctx.writeAndFlush("Input id: ");
                map.put("id", msg);
                service.delete(map);
                break;
            case 5:
                System.out.println("\nREAD BY ID FORM");
                ctx.writeAndFlush("Input id: ");
                map.put("id", msg);
                service.readById(map);
                break;
            case 0:
                ctx.writeAndFlush("Closing connection for client - " + ctx);
                ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Closing connection for client - " + ctx);
        ctx.close();
    }
}
