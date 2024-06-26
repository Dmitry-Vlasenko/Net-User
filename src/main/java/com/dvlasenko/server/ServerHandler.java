package com.dvlasenko.server;

import com.dvlasenko.app.entity.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import com.dvlasenko.app.service.UserService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    // Static list to store all active channels
    static final List<Channel> channels = new ArrayList<>();

    // UserService instance to handle user-related operations
    final UserService service = new UserService();

    // Attribute keys to store state and user data for each channel
    private static final AttributeKey<Integer> STATE = AttributeKey.valueOf("state");
    private static final AttributeKey<Map<String, String>> USER_DATA = AttributeKey.valueOf("userData");

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // Inform about new client connection
        System.out.println("Client joined - " + ctx);

        // Send initial menu to the client
        getMenu(ctx);

        // Add the channel to the active channels list
        channels.add(ctx.channel());
    }

    // Method to send initial menu to the client
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

        // Set initial state for the channel
        ctx.channel().attr(STATE).set(0);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        // Retrieve current state of the channel
        Integer state = ctx.channel().attr(STATE).get();
        if (state == null) {
            state = 0;
        }
        // Handle channel state based on user input
        switch (state) {
            case 0:
                handleMenuSelection(ctx, msg);
                break;
            case 1:
                handleCreateUser(ctx, msg);
                break;
            case 2:
                handleReadUser(ctx, msg);
                break;
            case 3:
                handleUpdateUser(ctx, msg);
                break;
            case 4:
                handleDeleteUser(ctx, msg);
                break;
            case 5:
                handleReadById(ctx, msg);
                break;
            default:
                getMenu(ctx);
                break;
        }
    }

    // Method to handle user's menu selection
    private void handleMenuSelection(ChannelHandlerContext ctx, String msg) {
        int data = Integer.parseInt(msg);

        // Create a new user data map for the channel
        ctx.channel().attr(USER_DATA).set(new HashMap<>());

        switch (data) {
            case 1:
                ctx.writeAndFlush("Input first name: ");
                ctx.channel().attr(STATE).set(1);
                break;
            case 2:
                handleReadUser(ctx, msg);
                break;
            case 3:
                ctx.writeAndFlush("Input id to update: ");
                ctx.channel().attr(STATE).set(3);
                break;
            case 4:
                ctx.writeAndFlush("Input id to delete: ");
                ctx.channel().attr(STATE).set(4);
                break;
            case 5:
                ctx.writeAndFlush("Input id to read: ");
                ctx.channel().attr(STATE).set(5);
                break;
            case 0:
                ctx.writeAndFlush("Closing connection for client - " + ctx);
                ctx.close();
                break;
            default:
                getMenu(ctx);
                break;
        }
    }

    // Method to handle user creation
    private void handleCreateUser(ChannelHandlerContext ctx, String msg) {
        // Retrieve user data map for the channel
        Map<String, String> userData = ctx.channel().attr(USER_DATA).get();
        if (!userData.containsKey("first_name")) {
            userData.put("first_name", msg);
            ctx.writeAndFlush("Input last name: ");
        } else if (!userData.containsKey("last_name")) {
            userData.put("last_name", msg);
            ctx.writeAndFlush("Input email in format example@mail.com: ");
        } else {
            userData.put("email", msg);
            service.create(userData);
            ctx.writeAndFlush("User created successfully.\n");
            getMenu(ctx);
        }
    }

    // Method to handle user update
    private void handleUpdateUser(ChannelHandlerContext ctx, String msg) {
        // Retrieve user data map for the channel
        Map<String, String> userData = ctx.channel().attr(USER_DATA).get();
        if (!userData.containsKey("id")) {
            userData.put("id", msg);
            ctx.writeAndFlush("Input first name: ");
        } else if (!userData.containsKey("first_name")) {
            userData.put("first_name", msg);
            ctx.writeAndFlush("Input last name: ");
        } else if (!userData.containsKey("last_name")) {
            userData.put("last_name", msg);
            ctx.writeAndFlush("Input email in format example@mail.com: ");
        } else {
            userData.put("email", msg);
            service.update(userData);
            ctx.writeAndFlush("User updated successfully.\n");
            getMenu(ctx);
        }
    }

    // Method to handle user deletion
    private void handleDeleteUser(ChannelHandlerContext ctx, String msg) {
        // Retrieve user data map for the channel
        Map<String, String> userData = ctx.channel().attr(USER_DATA).get();
        userData.put("id", msg);
        service.delete(userData);
        ctx.writeAndFlush("User deleted successfully.\n");
        getMenu(ctx);
    }

    // Method to handle reading user by id
    private void handleReadById(ChannelHandlerContext ctx, String msg) {
        // Retrieve user data map for the channel
        Map<String, String> userData = ctx.channel().attr(USER_DATA).get();
        userData.put("id", msg);
        ctx.writeAndFlush(service.readById(userData));
        getMenu(ctx);
    }

    // Method to handle reading all users
    private void handleReadUser(ChannelHandlerContext ctx, String msg) {
        List<User> users = service.read();
        if (users.isEmpty()) {
            ctx.writeAndFlush("No users found.\n");
        } else {
            StringBuilder sb = new StringBuilder("Users:\n");
            AtomicInteger count = new AtomicInteger(1);
            users.forEach(user -> sb.append(count.getAndIncrement()).append(". ").append(user));
            ctx.writeAndFlush(sb.toString());
        }
        getMenu(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Log and close the connection in case of an exception
        System.out.println("Closing connection for client - " + ctx);
        ctx.close();
    }
}
