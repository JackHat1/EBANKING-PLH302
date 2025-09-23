package com.tuc.ebanking.ui.cli;

import java.util.Scanner;

public class Cli {
    public static void start() {
        System.out.println("E-Banking CLI — Bank of TUC");
        System.out.println("Type 'help' to list commands. 'exit' to quit.");
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) break;
            if (line.equalsIgnoreCase("help")) {
                System.out.println("Commands: help, demo, exit");
            } else if (line.equalsIgnoreCase("demo")) {
                System.out.println("Demo: transfer 50 EUR from A -> B (mock)");
            } else {
                System.out.println("Unknown command: " + line);
            }
        }
        System.out.println("Bye.");
    }
}
