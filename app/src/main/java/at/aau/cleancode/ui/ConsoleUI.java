package at.aau.cleancode.ui;

import java.util.Scanner;

public class ConsoleUI implements AutoCloseable {
    private final Scanner scanner = new Scanner(System.in);

    public void printMessage(String message) {
        System.out.println(message);
    }

    public String nextLine() {
        return scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
