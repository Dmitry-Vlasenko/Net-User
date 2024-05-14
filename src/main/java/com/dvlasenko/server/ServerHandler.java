package com.dvlasenko.server;

import com.dvlasenko.app.controller.UserController;
import com.dvlasenko.app.exceptions.OptionException;
import com.dvlasenko.app.utils.AppStarter;
import com.dvlasenko.app.utils.Constants;
import com.dvlasenko.app.view.AppView;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    static final List<Channel> channels = new ArrayList<>();
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
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("Client joined - " + ctx);
        getMenu(ctx);
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("Client wrote - " + msg);
        getMenu(ctx);
        final UserController controller = new UserController();
        switch (Integer.parseInt(msg)) {
            case 1 -> controller.create();
            case 2 -> controller.read();
            case 3 -> controller.update();
            case 4 -> controller.delete();
            case 5 -> controller.readById();
            case 0 -> ctx.close();
            default -> {
                try {
                    throw new OptionException(Constants.INCORRECT_OPTION_MSG);
                } catch (OptionException e) {
                    new AppView().getOutput(e.getMessage());
                    AppStarter.startApp();
                }
            }
        }
        ctx.writeAndFlush("Client wrote - " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Closing connection for client - " + ctx);
        ctx.close();
    }
}
