package ch.x01.fuzzy.api;

import ch.x01.fuzzy.engine.*;
import ch.x01.fuzzy.parser.RuleParser;
import ch.x01.fuzzy.parser.SymbolTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FuzzyEngine {

    private final FuzzyModel model;
    private final int numOfSteps;
    private final HashSet<FuzzyRule> fuzzyRules = new HashSet<>();

    private boolean isReady;

    public FuzzyEngine(FuzzyModel model, int numOfSteps) {
        this.model = model;
        this.numOfSteps = numOfSteps;
    }

    public FuzzyEngine(FuzzyModel model) {
        this(model, 1000);
    }

    public OutputVariable evaluate(InputVariable... input) {

        // === setup engine

        if (!isReady) {
            SymbolTable symbolTable = new SymbolTable();

            // create linguistic variables and register them with symbol table
            for (FuzzyModel.LinguisticVariable var : this.model.getLinguisticVariables()) {
                LinguisticVariable lv = new LinguisticVariable(var.getName());
                for (FuzzyModel.Term term : var.getTerms()) {
                    lv.addTerm(term.getName(),
                               new MembershipFunction(term.getStart(), term.getLeft_top(), term.getRight_top(), term.getEnd()));
                }
                if (!symbolTable.registerLV(lv)) {
                    throw new FuzzyEngineException(String.format(
                            "Cannot register linguistic variable \"%s\" with symbol table because the variable is registered already.",
                            lv.getName()));
                }
            }

            // create rules
            for (String rule : model.getRules()) {
                if (!fuzzyRules.add(new FuzzyRule(rule, symbolTable))) {
                    throw new FuzzyEngineException(
                            String.format("Cannot add rule \"%s\" to the rule set because it is present already.", rule));
                }
            }

            // parse rules
            RuleParser parser = new RuleParser(symbolTable);
            fuzzyRules.parallelStream()
                      .forEach(parser::parse);

            isReady = true;
        }

        // === compute output value

        // set input value(s)
        //...

        // compute conclusions
        List<MembershipFunction> conclusions = new ArrayList<>();
        for (FuzzyRule rule : fuzzyRules) {
            conclusions.add(rule.computeConclusion());
        }

        // compute superposition
        MembershipFunction[] cs = new MembershipFunction[this.fuzzyRules.size()];
        double[][] superposition = MembershipFunction.computeSuperposition(conclusions.toArray(cs), numOfSteps);

        // defuzzify using center of mass approach
        double output = MembershipFunction.computeCenterOfMass(superposition);

        return new OutputVariable("", output);
    }

    public static class InputVariable {
        private final String name;
        private final double value;

        public InputVariable(String name, double value) {
            this.name = name;
            this.value = value;
        }
    }

    public static class OutputVariable {
        private final String name;
        private final double value;

        private OutputVariable(String name, double value) {
            this.name = name;
            this.value = value;
        }
    }

}
