package ch.x01.fuzzy.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a complete Fuzzy Model with linguistic variables, terms and rules.
 * The implementation utilizes the builder pattern with Java 8 lambdas.
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

    public List<LinguisticVariable> getLinguisticVariables() {
        return new ArrayList<>(Arrays.asList(vars));
    }

    public List<String> getRules() {
        return new ArrayList<>(Arrays.asList(rules));
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

        public String getName() {
            return name;
        }

        public List<Term> getTerms() {
            return new ArrayList<>(Arrays.asList(terms));
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

    }

    public static class Term {

        private final String name;
        private final double start;
        private final double left_top;
        private final double right_top;
        private final double end;

        private Term(String name, double start, double top, double end) {
            this(name, start, top, top, end);
        }

        private Term(String name, double start, double left_top, double right_top, double end) {
            this.name = name;
            this.start = start;
            this.left_top = left_top;
            this.right_top = right_top;
            this.end = end;
        }

        public static TriangleBuilder triangle() {
            return name -> start -> top -> end -> new Term(name, start, top, end);
        }

        public static TrapezoidBuilder trapezoid() {
            return name -> start -> left_top -> right_top -> end -> new Term(name, start, left_top, right_top, end);
        }

        @Override
        public String toString() {
            return "Term{" +
                    "name='" + name + '\'' +
                    ", mf=" + Arrays.toString(new double[]{start, left_top, right_top, end}) +
                    '}';
        }

        public String getName() {
            return name;
        }

        public double getStart() {
            return start;
        }

        public double getLeft_top() {
            return left_top;
        }

        public double getRight_top() {
            return right_top;
        }

        public double getEnd() {
            return end;
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
            Term end(double value);
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
            Term end(double value);
        }
    }
}
