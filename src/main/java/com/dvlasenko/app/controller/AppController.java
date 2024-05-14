package com.dvlasenko.app.controller;

import com.dvlasenko.app.exceptions.OptionException;
import com.dvlasenko.app.utils.AppStarter;
import com.dvlasenko.app.utils.Constants;
import com.dvlasenko.app.view.AppView;

public class AppController {

    final AppView view;

    public AppController(AppView view) {
        this.view = view;
    }

    public void runApp() {
        final UserController controller = new UserController();
        int option = view.getOption();
        switch (option) {
            case 1 -> controller.create();
            case 2 -> controller.read();
            case 3 -> controller.update();
            case 4 -> controller.delete();
            case 5 -> controller.readById();
            case 0 -> new AppView().getOutput(Integer.toString(option));
            default -> {
                try {
                    throw new OptionException(Constants.INCORRECT_OPTION_MSG);
                } catch (OptionException e) {
                    new AppView().getOutput(e.getMessage());
                    AppStarter.startApp();
                }
            }
        }
    }
}
