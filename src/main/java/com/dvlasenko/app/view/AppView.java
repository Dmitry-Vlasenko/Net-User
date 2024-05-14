package com.dvlasenko.app.view;

import com.dvlasenko.app.utils.AppStarter;
import com.dvlasenko.app.utils.Constants;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppView {

    public int getOption() {
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        getMenu();
        try {
            option = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println(Constants.INCORRECT_VALUE_MSG);
            AppStarter.startApp();
        }
        return option;
    }

    private void getMenu() {
        System.out.print("""
                OPTIONS:
                1 - Create user.
                2 - Read users.
                3 - Update user.
                4 - Delete user.
                5 - Read user by id.
                0 - Close the App.
                """);
        System.out.print("Input your option: ");
    }

    public void getOutput(String output) {
        if (output.equals("0")) {
            System.out.println(Constants.APP_CLOSE_MSG);
            System.exit(0);
        } else System.out.println(output);
    }
}
