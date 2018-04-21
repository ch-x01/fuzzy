package ch.x01.fuzzy.engine;

import ch.x01.fuzzy.parser.RuleParser;
import ch.x01.fuzzy.parser.SymbolTable;

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

//    public InputVariable addInputVariable(String name) {
//        inputVariable = new LinguisticVariable(name, symbolTable);
//        return inputVariable;
//    }
//
//    public OutputVariable addOutputVariable(String name) {
//        outputVariable = new LinguisticVariable(name, symbolTable);
//        return outputVariable;
//    }

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
        StringBuilder sb = new StringBuilder();
        sb.append("--- Engine").append(NEW_LINE);
        sb.append(String.format("number of increments = %s", numOfSteps)).append(NEW_LINE);
        sb.append(NEW_LINE);
        sb.append("--- Linguistic Variables").append(NEW_LINE);
        for (LinguisticVariable lv : symbolTable.getLinguisticVariables()) {
            sb.append(lv.toString()).append(NEW_LINE);
        }
        sb.append(NEW_LINE);
        sb.append("--- Fuzzy Rules").append(NEW_LINE);
        sb.append(fuzzyRuleSet.toString()).append(NEW_LINE);

        return sb.toString();
    }

    public String getResult() {
        return String.format("%s.input = %.4f -> %s.output = %.4f", inputVariable.getName(), inputVariable.getInputValue(),
                             outputVariable.getName(), outputVariable.getOutputValue());
    }

    public RuleParser getRuleParser() {
        return ruleParser;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

}
