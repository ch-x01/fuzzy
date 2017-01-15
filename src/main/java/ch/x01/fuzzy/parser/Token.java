package ch.x01.fuzzy.parser;

/**
 * Created by developer on 04.01.17.
 */
public enum Token {
    IF {
        boolean isKeyword(String expression) {
            return "if".equals(expression);
        }
    },
    IS {
        boolean isKeyword(String expression) {
            return "is".equals(expression);
        }
    },
    THEN {
        boolean isKeyword(String expression) {
            return "then".equals(expression);
        }
    },
    AND {
        boolean isKeyword(String expression) {
            return "and".equals(expression);
        }
    },
    OR {
        boolean isKeyword(String expression) {
            return "or".equals(expression);
        }
    },
    LEFT_PAR {
        boolean isKeyword(String expression) {
            return false;
        }
    },
    RIGHT_PAR {
        boolean isKeyword(String expression) {
            return false;
        }
    },
    IDENT {
        boolean isKeyword(String expression) {
            return false;
        }
    },
    START {
        boolean isKeyword(String expression) {
            return false;
        }
    },
    END {
        boolean isKeyword(String expression) {
            return false;
        }
    };

    abstract boolean isKeyword(String expression);
}
