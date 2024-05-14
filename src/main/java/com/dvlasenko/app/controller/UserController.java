package com.dvlasenko.app.controller;

import com.dvlasenko.app.service.UserService;
import com.dvlasenko.app.utils.AppStarter;
import com.dvlasenko.app.view.*;

public class UserController {

    final UserService service = new UserService();

    public void create() {
        UserCreateView view = new UserCreateView();
        view.getOutput(service.create(view.getData()));
        AppStarter.startApp();
    }

    public void read() {
        UserReadView view = new UserReadView();
        view.getOutput(service.read());
        AppStarter.startApp();
    }

    public void update() {
        UserUpdateView view = new UserUpdateView();
        view.getOutput(service.update(view.getData()));
        AppStarter.startApp();
    }

    public void delete() {
        UserDeleteView view = new UserDeleteView();
        view.getOutput(service.delete(view.getData()));
        AppStarter.startApp();
    }

    public void readById() {
        UserReadByIdView view = new UserReadByIdView();
        view.getOutput(service.readById(view.getData()));
        AppStarter.startApp();
    }
}
