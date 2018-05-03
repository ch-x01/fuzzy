package ch.x01.fuzzy.parser;

import ch.x01.fuzzy.core.FuzzyRule;
import ch.x01.fuzzy.core.FuzzyRuleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * This class implements a straightforward LL parser to parse a fuzzy rule as
 * described by the class FuzzyRule.
 * <p>
 * In general a fuzzy rule is of the form 'IF <i>premise</i> THEN <i>conclusion</i>'
 * </p>
 * Using the vocabulary { x, (, ), + } where x represents a simple fuzzy
 * expression of the form 'LV is LT' and '+' replaces any fuzzy operator, a set
 * of sentences can be expressed
 * <ul>
 * <li> x </li>
 * <li>(x)</li>
 * <li>(x+x)</li>
 * <li>(x+(x+x))</li>
 * <li>...</li>
 * </ul>
 * These sentences describe a rule's premise/conclusion. The grammar of such a
 * premise/conclusion can be written in EBNF as
 * <p>
 * S := X | '(' B ')'<br>
 * B := S C<br>
 * C := {+ S}<br>
 * </p>
 * <p>
 * Thus, these are the production rules this parser is made of.
 */
public class RuleParser {

    private static final Logger logger = LoggerFactory.getLogger(RuleParser.class);

    private final Stack<Token> opDelayStack = new Stack<>();
    private final SymbolTable symbolTable;

    private Token token;
    private RuleScanner ruleScanner;
    private FuzzyRule fuzzyRule;
    private Stack<String> stack;

    /**
     * Constructor
     *
     * @param symbolTable the table where linguistic variables and its terms are registered
     */
    public RuleParser(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    /**
     * Use this constructor only for testing purposes.<br>
     * This constructor instantiates a rule parser without a symbol table associated to it. As a
     * result linguistic variables used within rules are not validated.
     */
    public RuleParser() {
        symbolTable = null;
    }

    private void get(Token token) throws IllegalNameException {
        fuzzyRule.addToken(token);
        get();
    }

    private void get() throws IllegalNameException {
        if (ruleScanner.hasMoreTokens()) {
            token = ruleScanner.nextToken();
        }
    }

    private void s() throws SyntaxError, IllegalNameException, UndefinedSymbolException {
        if (token == Token.LEFT_PAR) {
            get(token);
            b();
            if (token == Token.RIGHT_PAR) {
                for (int i = opDelayStack.size() - 1; i >= 0; i--) {
                    stack.push(opDelayStack.get(i).toString());
                }
                opDelayStack.clear();
                get(token);
            } else {
                throw new SyntaxError(Token.RIGHT_PAR);
            }
        } else if (token == Token.IDENT) {
            String ident = ruleScanner.getIdentifier();
            if (symbolTable != null && !symbolTable.validateLV(ident)) {
                throw new UndefinedSymbolException(ident);
            }
            stack.push(ident);
            get(token);
            x();
        } else {
            throw new SyntaxError(Token.IDENT);
        }
    }

    private void b() throws SyntaxError, IllegalNameException, UndefinedSymbolException {
        s();
        c();
    }

    private void c() throws SyntaxError, IllegalNameException, UndefinedSymbolException {
        if (token == Token.AND || token == Token.OR) {
            while (token == Token.AND || token == Token.OR) {
                opDelayStack.push(token);
                get(token);
                s();
            }
        } else {
            throw new SyntaxError("AND or OR expected");
        }
    }

    private void x() throws SyntaxError, IllegalNameException, UndefinedSymbolException {
        if (token == Token.IS) {
            get(token);
            if (token == Token.IDENT) {
                String identLt = ruleScanner.getIdentifier();
                String identLv = ruleScanner.getLastIdentifier();
                if (symbolTable != null && !symbolTable.validateLT(identLv, identLt)) {
                    throw new UndefinedSymbolException(identLt);
                }
                stack.push(identLt);
                stack.push(Token.IS.toString());
                get(token);
            } else {
                throw new SyntaxError(Token.IDENT);
            }
        } else {
            throw new SyntaxError(Token.IS);
        }
    }

    private void parseRule() {
        try {
            get(); // getIdentifier first token

            if (token == Token.START) {
                get(token);
            } else {
                throw new SyntaxError(Token.START);
            }

            if (token == Token.IF) {
                stack = fuzzyRule.getPremises(); // switch stack to rule's premise
                // parse premise
                do {
                    get(token);
                    s();
                    if (!(token == Token.THEN || token == Token.AND || token == Token.OR)) {
                        throw new SyntaxError("AND, OR or THEN expected");
                    }
                } while (token != Token.THEN);

                stack = fuzzyRule.getConclusion(); // switch stack to rule's conclusion
                // parse conclusion
                do {
                    get(token);
                    s();
                } while (ruleScanner.hasMoreTokens());

            } else {
                throw new SyntaxError(Token.IF);
            }

            if (token == Token.END) {
                get(token);
                fuzzyRule.setStatus(FuzzyRuleStatus.DONE);
            } else {
                throw new SyntaxError(Token.END);
            }
        } catch (SyntaxError | IllegalNameException | UndefinedSymbolException e) {
            fuzzyRule.setParsingError(e.getMessage());
            fuzzyRule.setStatus(FuzzyRuleStatus.ERRONEOUS);
        }
    }

    public void parse(FuzzyRule rule) {
        fuzzyRule = rule;
        ruleScanner = new RuleScanner(fuzzyRule.getRuleText());
        parseRule();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Parsed rule \"%s\".", rule.getRuleText()));
            logger.debug(String.format("parsing status = %s,  parsing error: %s", rule.getStatus(), rule.getParsingError()));
        }
    }

}
