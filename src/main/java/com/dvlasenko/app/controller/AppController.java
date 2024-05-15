package com.dvlasenko.app.controller;

import com.dvlasenko.app.entity.User;
import com.dvlasenko.app.service.UserService;
import com.dvlasenko.app.view.Menu;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AppController {
    private static final AttributeKey<Map<String, String>> USER_DATA = AttributeKey.valueOf("userData");
    final UserService service = new UserService();

    public void runApp(ChannelHandlerContext ctx, String msg) {
        switch (Integer.parseInt(msg)) {
            case 0:
                ctx.writeAndFlush("Closing connection for client - " + ctx);
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
                Menu.getMenu(ctx);
                break;
        }
    }

    private void handleCreateUser(ChannelHandlerContext ctx, String msg) {
        Map<String, String> userData = ctx.channel().attr(USER_DATA).get();
        ctx.writeAndFlush("Input first name: ");
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
            Menu.getMenu(ctx);
        }
    }

    private void handleUpdateUser(ChannelHandlerContext ctx, String msg) {
        Map<String, String> userData = ctx.channel().attr(USER_DATA).get();
        ctx.writeAndFlush("Input id to update: ");
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
            Menu.getMenu(ctx);
        }
    }

    private void handleDeleteUser(ChannelHandlerContext ctx, String msg) {
        Map<String, String> userData = ctx.channel().attr(USER_DATA).get();
        ctx.writeAndFlush("Input id to delete: ");
        userData.put("id", msg);
        service.delete(userData);
        ctx.writeAndFlush("User deleted successfully.\n");
        Menu.getMenu(ctx);
    }

    private void handleReadById(ChannelHandlerContext ctx, String msg) {
        Map<String, String> userData = ctx.channel().attr(USER_DATA).get();
        ctx.writeAndFlush("Input id to read: ");
        userData.put("id", msg);
        ctx.writeAndFlush(service.readById(userData));
        Menu.getMenu(ctx);
    }

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
        Menu.getMenu(ctx);
    }
}
