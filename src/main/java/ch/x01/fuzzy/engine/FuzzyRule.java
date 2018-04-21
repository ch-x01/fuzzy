package ch.x01.fuzzy.engine;

import ch.x01.fuzzy.parser.SymbolTable;
import ch.x01.fuzzy.parser.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

/**
 * This class implements the concept of a fuzzy rule. A fuzzy rule is of the form
 * <p>
 * if x<sub>1</sub> is a<sub>1</sub> and x<sub>2</sub> is a<sub>2</sub> ... and x<sub>n</sub>
 * is a<sub>n</sub> then y is b
 * </p>
 * where x<sub>i</sub> and y are linguistic variables and a<sub>i</sub> and b are linguistic
 * terms.<br>
 * The 'if'-part is the rule's <i>premise(s)</i>, while the 'then'-part is the rule's <i>conclusion</i>.
 */
public class FuzzyRule {

    private static final Logger logger = LoggerFactory.getLogger(FuzzyRule.class);

    private final String ruleText;
    private final SymbolTable symbolTable;

    // left-hand side of rule
    private final Stack<String> premises = new Stack<>();

    // right-hand side of rule
    private final Stack<String> conclusion = new Stack<>();

    // tokens of the parsed rule
    private final ArrayList<Token> tokens = new ArrayList<>();

    // parsing error if occurred
    private String parsingError = "n/a";

    // status of rule
    private FuzzyRuleStatus status = FuzzyRuleStatus.IDLE;

    /**
     * Constructs a fuzzy rule.
     *
     * @param ruleText    the rule text
     * @param symbolTable the table where linguistic variables and its terms are registered
     */
    public FuzzyRule(String ruleText, SymbolTable symbolTable) {
        this.ruleText = ruleText.toLowerCase();
        this.symbolTable = symbolTable;
    }

    // TODO clean up
    //    /**
    //     * Factory method to construct a rule by parsing its rule text.
    //     *
    //     * @param ruleText the rule text
    //     * @param engine   the fuzzy engine hosting the rule
    //     * @return fuzzy rule
    //     */
    //    public static FuzzyRule parse(String ruleText, FuzzyEngine engine) {
    //        FuzzyRule fuzzyRule = new FuzzyRule(ruleText, engine.getSymbolTable());
    //        engine.getRuleParser().parse(fuzzyRule);
    //        return fuzzyRule;
    //    }

    /**
     * Computes degree of relevance of this rule.
     * <p>
     * The <i>degree of relevance (H)</i> is a constant that combines the fact with the premise.<br>
     * If this rule's premise has a nonzero H then the rule is said to <i>fire</i>.
     * </p>
     * Given the rule <code>if x is a then y is b</code> and considering that <code>x</code> is
     * the linguistic variable, <code>u<sub>a</sub>(x)</code> is the membership function
     * associated to linguistic term <code>a</code> and <code>u<sub>f</sub>(x)</code> is the
     * singleton that represents a crisp input value then the degree of relevance for this rule
     * computes to <code>H = max{min{u<sub>f</sub>(x),u<sub>a</sub>(x)}}</code>
     *
     * @return <i>degree of relevance (H)</i>
     */
    public double computeDegreeOfRelevance() {
        double result;

        if (status != FuzzyRuleStatus.DONE) {
            throw new FuzzyEngineException(
                    String.format("Cannot compute degree of relevance of rule \"%s\" because its status is \"%s\".", ruleText,
                                  status));
        }

        Stack<Double> stack = new Stack<>();

        for (int i = 0; i < premises.size(); i++) {
            String token = premises.get(i);

            if (token.equals(Token.IS.toString())) {
                LinguisticVariable lv = symbolTable.getLV(premises.get(i - 2));
                stack.push(lv.is(premises.get(i - 1)));
            }

            if (token.equals(Token.AND.toString())) {
                Double operand2 = stack.pop();
                Double operand1 = stack.pop();
                stack.push(Math.min(operand1, operand2));
            }

            if (token.equals(Token.OR.toString())) {
                Double operand2 = stack.pop();
                Double operand1 = stack.pop();
                stack.push(Math.max(operand1, operand2));
            }
        }

        result = stack.pop();

        if (result > 0 && logger.isDebugEnabled()) {
            logger.debug(String.format("Rule \"%s\" fires. Degree of relevance H = %.4f", ruleText, result));
        }

        return result;
    }

    public MembershipFunction computeConclusion() {
        return computeConclusion(computeDegreeOfRelevance());
    }

    /**
     * Performs the reasoning process for this rule's conclusion, that is, the membership function
     * of the conclusion is determined using the <i>degree of relevance (H)</i> of this rule.
     * <p>
     * Precondition: the conclusion consists of only one linguistic variable.
     * </p>
     *
     * @param degreeOfRelevance degree of relevance (H)
     * @return membership function <code>min{H, u<sub>c</sub>(x)}</code>
     */
    public MembershipFunction computeConclusion(double degreeOfRelevance) {
        MembershipFunction result;

        if (status != FuzzyRuleStatus.DONE) {
            throw new FuzzyEngineException(String.format("Cannot compute conclusion of rule \"%s\" because its status is \"%s\".",
                                                         ruleText, status));
        }

        // if the conclusion is '... then y is b' --> conclusion = [y b is]
        LinguisticVariable lv = symbolTable.getLV(conclusion.get(0));
        MembershipFunction mf = lv.getMembershipFunction(conclusion.get(1));
        result = mf.computeReasoning(degreeOfRelevance);

        return result;
    }

    public String getRuleText() {
        return ruleText;
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public String getParsingError() {
        return parsingError;
    }

    public void setParsingError(String parsingError) {
        this.parsingError = parsingError;
    }

    public FuzzyRuleStatus getStatus() {
        return status;
    }

    public void setStatus(FuzzyRuleStatus status) {
        // possible state transitions are:
        // idle --> idle
        // idle --> done
        // idle --> erroneous
        if (this.status == FuzzyRuleStatus.IDLE) {
            this.status = status;
        }
    }

    public Stack<String> getConclusion() {
        return conclusion;
    }

    public Stack<String> getPremises() {
        return premises;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FuzzyRule fuzzyRule = (FuzzyRule) o;
        return Objects.equals(ruleText, fuzzyRule.ruleText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleText);
    }

    public String toString() {
        return ruleText;
    }
}
