package at.aau.cleancode.ui;

import java.util.Scanner;

public class ConsoleUI implements UserInterface {
    private final Scanner scanner = new Scanner(System.in);

    @SuppressWarnings("java:S106") //Suppress SonarCube Logger Suggestion
    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
