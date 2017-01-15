package ch.x01.fuzzy.engine;

import ch.x01.fuzzy.parser.RuleParser;
import ch.x01.fuzzy.parser.SymbolTable;

/**
 * Created by developer on 05.01.17.
 */
public class FuzzyEngine {

    private final static String NEW_LINE = System.getProperty("line.separator");
    private final RuleParser ruleParser;
    private final SymbolTable symbolTable;
    private final int numOfSteps;
    private InputVariable inputVariable;
    private OutputVariable outputVariable;
    private FuzzyRuleSet fuzzyRuleSet;

    public FuzzyEngine() {
        this(1000);
    }

    /**
     * Constructor.
     *
     * @param numOfSteps the number of increments used for discretisation of membership functions
     */
    public FuzzyEngine(int numOfSteps) {
        this.numOfSteps = numOfSteps;

        symbolTable = new SymbolTable();
        ruleParser = new RuleParser(symbolTable);
    }

    public InputVariable addInputVariable(String name) {
        inputVariable = new LinguisticVariable(name, symbolTable);
        return inputVariable;
    }

    public OutputVariable addOutputVariable(String name) {
        outputVariable = new LinguisticVariable(name, symbolTable);
        return outputVariable;
    }

    public FuzzyRuleSet addRuleSet() {
        fuzzyRuleSet = new FuzzyRuleSet();
        return fuzzyRuleSet;
    }

    public void evaluateRules() {
        outputVariable.setOutputValue(fuzzyRuleSet.evaluateRules(numOfSteps));
    }

    public void evaluateRules(double inputValue) {
        inputVariable.setInputValue(inputValue);
        outputVariable.setOutputValue(fuzzyRuleSet.evaluateRules(numOfSteps));
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("--- Linguistic Variables").append(NEW_LINE);
        for (LinguisticVariable lv : symbolTable.getLinguisticVariables()) {
            builder.append(lv.toString()).append(NEW_LINE);
        }
        builder.append(NEW_LINE);
        builder.append("--- Fuzzy Rules").append(NEW_LINE);
        builder.append(fuzzyRuleSet.toString()).append(NEW_LINE);

        return builder.toString();
    }

    public String getResult() {
        return String.format("%s.input = %1.4f -> %s.output = %1.4f", inputVariable.getName(), inputVariable
                .getInputValue(), outputVariable.getName(), outputVariable.getOutputValue());
    }

    public RuleParser getRuleParser() {
        return ruleParser;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

}
