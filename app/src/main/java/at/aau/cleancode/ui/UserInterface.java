package at.aau.cleancode.ui;

public interface UserInterface extends AutoCloseable {
    void printMessage(String message);

    String nextLine();

    @Override
    void close();
}
