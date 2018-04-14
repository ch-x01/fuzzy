package ch.x01.fuzzy.api;

import java.util.Arrays;

/**
 * This class represents a complete Fuzzy Model with linguistic variables, terms and rules.
 * The implementation uses the builder pattern with Java 8 lambdas.
 * See also http://benjiweber.co.uk/blog/2014/11/02/builder-pattern-with-java-8-lambdas/
 */
public class FuzzyModel {

    private final String name;
    private final LinguisticVariable[] vars;
    private final String[] rules;

    private FuzzyModel(String name, LinguisticVariable[] vars, String[] rules) {
        this.name = name;
        this.vars = vars;
        this.rules = rules;
    }

    public static FuzzyModelBuilder model() {
        return name -> vars -> rules -> new FuzzyModel(name, vars, rules);
    }

    @Override
    public String toString() {
        return "FuzzyModel{" +
                "name='" + name + '\'' +
                ", vars=" + Arrays.toString(vars) +
                ", rules=" + Arrays.toString(rules) +
                '}';
    }

    interface FuzzyModelBuilder {
        VariableBuilder name(String name);
    }

    interface VariableBuilder {
        RuleBuilder vars(LinguisticVariable... lv);
    }

    interface RuleBuilder {
        FuzzyModel rules(String... rule);
    }

    public static class LinguisticVariable {

        private final String usage;
        private final String name;
        private final Term[] terms;

        private LinguisticVariable(String usage, String name, Term[] terms) {
            this.usage = usage;
            this.name = name;
            this.terms = terms;
        }

        public static LinguisticVariableBuilder lv() {
            return usage -> name -> terms -> new LinguisticVariable(usage, name, terms);
        }

        @Override
        public String toString() {
            return "LinguisticVariable{" +
                    "usage='" + usage + '\'' +
                    ", name='" + name + '\'' +
                    ", terms=" + Arrays.toString(terms) +
                    '}';
        }

        interface LinguisticVariableBuilder {
            NameBuilder usage(String usage);
        }

        interface NameBuilder {
            TermBuilder name(String name);
        }

        interface TermBuilder {
            LinguisticVariable terms(Term... term);
        }

        interface Term {
        }

    }

    public static class LinguisticTerm implements LinguisticVariable.Term { //implements Triangle, Trapezoid {

        private final String name;
        private final double[] mf;

        private LinguisticTerm(String name, double start, double top, double end) {
            this.name = name;
            this.mf = new double[]{start, top, end};
        }

        private LinguisticTerm(String name, double start, double left_top, double right_top, double end) {
            this.name = name;
            this.mf = new double[]{start, left_top, right_top, end};
        }

        public static TriangleBuilder triangle() {
            return name -> start -> top -> end -> new LinguisticTerm(name, start, top, end);
        }

        public static TrapezoidBuilder trapezoid() {
            return name -> start -> left_top -> right_top -> end -> new LinguisticTerm(name, start, left_top, right_top, end);
        }

        @Override
        public String toString() {
            return "LinguisticTerm{" +
                    "name='" + name + '\'' +
                    ", mf=" + Arrays.toString(mf) +
                    '}';
        }

        interface TriangleBuilder {
            TriangleStartBuilder name(String name);
        }

        interface TriangleStartBuilder {
            TriangleTopBuilder start(double value);
        }

        interface TriangleTopBuilder {
            TriangleEndBuilder top(double value);
        }

        interface TriangleEndBuilder {
            LinguisticTerm end(double value);
        }

        interface TrapezoidBuilder {
            TrapezoidStartBuilder name(String name);
        }

        interface TrapezoidStartBuilder {
            TrapezoidLeftTopBuilder start(double value);
        }

        interface TrapezoidLeftTopBuilder {
            TrapezoidRightTopBuilder left_top(double value);
        }

        interface TrapezoidRightTopBuilder {
            TrapezoidEndBuilder right_top(double value);
        }

        interface TrapezoidEndBuilder {
            LinguisticTerm end(double value);
        }
    }
}
