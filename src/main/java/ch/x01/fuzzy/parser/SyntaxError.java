package ch.x01.fuzzy.parser;

public class SyntaxError extends Exception {

    /**
     * @param token the expected token
     */
    public SyntaxError(Token token) {
        super("Syntax error: " + token + " expected");
    }

    /**
     * @param message description of cause
     */
    public SyntaxError(String message) {
        super("Syntax error: " + message);
    }

}
