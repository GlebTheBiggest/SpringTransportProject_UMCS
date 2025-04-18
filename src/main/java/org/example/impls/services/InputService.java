package org.example.impls.services;

import java.util.Scanner;

public class InputService {
    private static final Scanner scanner = new Scanner(System.in);
    private static char operator;

    public static char getOperatorInput(String message, char[] validOptions) {
        while (true) {
            System.out.println(message);
            String input = scanner.nextLine().trim();
            if (input.length() == 1) {
                char option = input.charAt(0);
                for (char valid : validOptions) {
                    if (option == valid) return option;
                }
            }
            System.out.println("Invalid input. Please try again.");
        }
    }

    public static String getUserInput(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public static boolean confirmAction(String message) {
        while (true) {
            String input = getUserInput(message).toLowerCase();
            if (input.equals("yes")) return true;
            if (input.equals("no")) return false;
            System.out.println("Invalid choice! Please type 'yes' or 'no'.");
        }
    }
}
