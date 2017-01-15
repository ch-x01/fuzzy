package ch.x01.fuzzy.parser;

public class IllegalNameException extends Exception {

    /**
     * @param index position in text at which the exception occurred
     */
    public IllegalNameException(int index) {
        super("Illegal name @" + String.valueOf(index));
    }

}
