package ch.x01.fuzzy.parser;

import java.util.LinkedList;

public class RuleScanner {

    private static final char SP = '\u0020';  // Space
    //private static final char NUL = '\u0000'; // Null
    private static final char STX = '\u0002'; // Start of Text
    private static final char ETX = '\u0003'; // End of Text
    private static final char[] LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final char[] NUMBERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final char[] expression;
    private final LinkedList<String> identifiers = new LinkedList<>();

    private char ch = STX;
    private ScannerState currentState;
    private int index;

    public RuleScanner(String expression) {
        this.expression = expression.trim().toLowerCase().toCharArray();
        currentState = new Starting();
    }

    private void nextChar() {
        if (index < expression.length) {
            ch = expression[index];
            index++;
        } else {
            ch = ETX;
        }
    }

    private boolean isLetter(char ch) {
        boolean result = false;

        for (char l : LETTERS) {
            if (ch == l) {
                result = true;
                break;
            }
        }

        return result;
    }

    private boolean isNumber(char ch) {
        boolean result = false;

        for (char n : NUMBERS) {
            if (ch == n) {
                result = true;
                break;
            }
        }

        return result;
    }

    private Token testForKeyword(String expression) {
        Token result = null;

        for (Token token : Token.values()) {
            if (token.isKeyword(expression)) {
                result = token;
                break;
            }
        }

        return result;
    }

    private Token readIdentifier() throws IllegalNameException {
        Token result;
        StringBuilder id = new StringBuilder();

        if (isLetter(ch)) {
            id.append(ch);
            nextChar();
        } else {
            throw new IllegalNameException(index);
        }

        while (isLetter(ch) || isNumber(ch)) {
            id.append(ch);
            nextChar();
        }

        Token key = testForKeyword(id.toString());

        if (key == null) {
            result = Token.IDENT;
            identifiers.add(id.toString());
        } else {
            result = key;
        }

        return result;
    }

    public String getIdentifier() {
        return identifiers.getLast();
    }

    public String getLastIdentifier() {
        return identifiers.get(identifiers.size() - 2);
    }

    public Token nextToken() throws IllegalNameException {
        return currentState.nextToken();
    }

    public boolean hasMoreTokens() {
        return currentState.hasMoreTokens();
    }

    private interface ScannerState {
        Token nextToken() throws IllegalNameException;

        boolean hasMoreTokens();
    }

    private class Starting implements ScannerState {

        public Token nextToken() throws IllegalNameException {
            Token result = null;

            if (ch == STX) {
                result = Token.START;
                nextChar();
                currentState = new Scanning();
            }

            return result;
        }

        public boolean hasMoreTokens() {
            return true;
        }

    } // class Starting

    private class Scanning implements ScannerState {

        public Token nextToken() throws IllegalNameException {
            Token result;

            while (ch == SP) {
                nextChar();
            }

            if (ch == '(') {
                result = Token.LEFT_PAR;
                nextChar();
            } else if (ch == ')') {
                result = Token.RIGHT_PAR;
                nextChar();
            } else if (ch == ETX) {
                result = Token.END;
                currentState = new Finish();
            } else {
                result = readIdentifier();
            }

            return result;
        }

        public boolean hasMoreTokens() {
            return true;
        }

    } // class Scanning

    private class Finish implements ScannerState {

        public Token nextToken() throws IllegalNameException {
            Token result = null;

            if (ch == ETX) {
                result = Token.END;
            }

            return result;
        }

        public boolean hasMoreTokens() {
            return false;
        }

    } // Finish

}
