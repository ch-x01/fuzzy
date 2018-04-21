package ch.x01.fuzzy.api;

import ch.x01.fuzzy.core.FuzzyEngineException;
import ch.x01.fuzzy.core.FuzzyRule;
import ch.x01.fuzzy.core.LinguisticVariable;
import ch.x01.fuzzy.core.MembershipFunction;
import ch.x01.fuzzy.parser.RuleParser;
import ch.x01.fuzzy.parser.SymbolTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FuzzyEngine {

    private static final Logger logger = LoggerFactory.getLogger(FuzzyEngine.class);

    private static final String formatString = "%s.input = %{padding}.{precision}f -> %s.output = %{padding}.{precision}f";

    private final FuzzyModel model;
    private final int numOfSteps;
    private HashSet<FuzzyRule> fuzzyRules;
    private SymbolTable symbolTable;

    private boolean isReady;

    public FuzzyEngine(FuzzyModel model, int numOfSteps) {
        this.model = model;
        this.numOfSteps = numOfSteps;
    }

    public FuzzyEngine(FuzzyModel model) {
        this(model, 1000);
    }

    public OutputVariable evaluate(InputVariable... input) {
        if (logger.isDebugEnabled()) {
            logger.debug("Evaluating " + model.toString());
        }

        // === setup engine

        if (!isReady) {
            // invalidate symbol table and fuzzy rules
            fuzzyRules = new HashSet<>();
            symbolTable = new SymbolTable();

            // create linguistic variables and register them with symbol table
            for (FuzzyModel.LinguisticVariable var : model.getLinguisticVariables()) {
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
                if (logger.isDebugEnabled()) {
                    logger.debug("Created linguistic variable " + lv.toString());
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
            fuzzyRules.forEach(parser::parse);

            isReady = true;
        }

        // === compute output value

        // set input value(s)
        for (InputVariable var : input) {
            if (model.isValidInputVariable(var.name)) {
                LinguisticVariable lv = symbolTable.getLV(var.name);
                lv.setValue(var.value);
            } else {
                throw new FuzzyEngineException(String.format("\"%s\" is not a valid input variable.", var.name));
            }
        }

        // compute conclusions
        List<MembershipFunction> conclusions = new ArrayList<>();
        for (FuzzyRule rule : fuzzyRules) {
            conclusions.add(rule.computeConclusion());
        }

        // compute superposition
        MembershipFunction[] cs = new MembershipFunction[this.fuzzyRules.size()];
        double[][] superposition = MembershipFunction.computeSuperposition(conclusions.toArray(cs), numOfSteps);

        // defuzzify using center of mass approach
        double CoM = MembershipFunction.computeCenterOfMass(superposition);

        if (logger.isDebugEnabled()) {
            logger.debug("--- defuzzification");
            logger.debug("x = " + CoM);
        }

        // set output value
        return new OutputVariable(model.getOutputVariableName(), CoM);
    }

    public String printResult(InputVariable input, OutputVariable output, int padding, int precision) {
        String str = formatString.replace("{padding}", Integer.toString(padding))
                                 .replace("{precision}", Integer.toString(precision));

        return String.format(str, input.getName(), input.getValue(), output.getName(), output.getValue());
    }

    public static class InputVariable {
        private final String name;
        private final double value;

        public InputVariable(String name, double value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public double getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "InputVariable{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    public static class OutputVariable {
        private final String name;
        private final double value;

        private OutputVariable(String name, double value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public double getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "OutputVariable{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

}
