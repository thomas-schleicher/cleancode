package at.aau.cleancode.parsing;

public interface PageParser<InputType> {
    Page parse(InputType input);
}
