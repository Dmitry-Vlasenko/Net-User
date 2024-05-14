package com.dvlasenko.app.utils;

import com.dvlasenko.app.controller.AppController;
import com.dvlasenko.app.view.AppView;

public class AppStarter {

    public static void startApp() {
        AppView view = new AppView();
        AppController controller = new AppController(view);
        controller.runApp();
    }
}
