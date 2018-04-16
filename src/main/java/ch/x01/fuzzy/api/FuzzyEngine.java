package ch.x01.fuzzy.api;

import ch.x01.fuzzy.engine.FuzzyEngineException;
import ch.x01.fuzzy.engine.LinguisticVariable;
import ch.x01.fuzzy.engine.MembershipFunction;
import ch.x01.fuzzy.parser.SymbolTable;

public class FuzzyEngine {

    private final FuzzyModel model;
    private final int numOfSteps;

    public FuzzyEngine(FuzzyModel model, int numOfSteps) {
        this.model = model;
        this.numOfSteps = numOfSteps;
    }

    public FuzzyEngine(FuzzyModel model) {
        this(model, 1000);
    }

    public OutputVariable evaluate(InputVariable... input) {

        SymbolTable symbolTable = new SymbolTable();

        // create linguistic variables and register them with symbol table
        for (FuzzyModel.LinguisticVariable var : this.model.getLinguisticVariables()) {
            LinguisticVariable lv = new LinguisticVariable(var.getName());
            for (FuzzyModel.Term term : var.getTerms()) {
                lv.addTerm(term.getName(), new MembershipFunction(term.getStart(), term.getLeft_top(), term.getRight_top(), term.getEnd()));
            }
            if (!symbolTable.registerLV(lv)) {
                throw new FuzzyEngineException(String.format(
                        "Cannot register linguistic variable \"%s\" with symbol table because the variable is registered already.",
                        lv.getName()));
            }
        }

        return new OutputVariable("", 0);
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
