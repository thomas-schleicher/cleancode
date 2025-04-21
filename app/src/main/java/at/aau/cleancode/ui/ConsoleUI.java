package at.aau.cleancode.ui;

import java.util.Scanner;

//TODO: this could impelment closable or auto-closeable so it works with try-with-resources like scanner
public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);

    public void printMessage(String message) {
        System.out.println(message);
    }

    public String nextLine() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}
