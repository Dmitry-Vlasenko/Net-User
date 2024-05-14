package com.dvlasenko.app.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserDeleteView {

    public Map<String, String> getData() {
        System.out.println("\nDELETE FORM");
        Map<String, String> map = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input id: ");
        map.put("id", scanner.nextLine().trim());
        return map;
    }

    public void getOutput(String output) {
        System.out.println(output);
    }
}
