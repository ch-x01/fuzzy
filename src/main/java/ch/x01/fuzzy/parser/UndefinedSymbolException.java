package ch.x01.fuzzy.parser;

public class UndefinedSymbolException extends Exception {

    /**
     * @param ident the undefined identifier
     */
    public UndefinedSymbolException(String ident) {
        super("Symbol \'" + ident + "\' is not defined");
    }

}
