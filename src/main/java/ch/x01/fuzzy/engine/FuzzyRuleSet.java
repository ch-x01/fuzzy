package ch.x01.fuzzy.engine;

import ch.x01.fuzzy.parser.RuleParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FuzzyRuleSet {

    private static Logger logger = LoggerFactory.getLogger(FuzzyRuleSet.class);

    private Set<FuzzyRule> rules = new HashSet<>();

    public FuzzyRuleSet() {
    }

    /**
     * Adds the specified rule to this set if it is not already present.
     *
     * @param rule the fuzzy rule to add
     */
    public void addRule(FuzzyRule rule) {
        if (!this.rules.add(rule)) {
            logger.warn(String.format("Cannot add rule \"%s\" to the rule set because it is already present.",
                                      rule.getRuleText()));
        }
    }

    /**
     * Removes the specified rule from this set if it is present.
     *
     * @param rule the fuzzy rule to remove
     */
    public void removeRule(FuzzyRule rule) {
        if (!this.rules.remove(rule)) {
            logger.warn(String.format("Cannot remove rule \"%s\" from the rule set because it is not present.",
                                      rule.getRuleText()));
        }
    }

    /**
     * Returns all rules of this set.
     *
     * @return fuzzy rules contained in this set
     */
    public Set<FuzzyRule> getRules() {
        return Collections.unmodifiableSet(this.rules);
    }

    public void parseRules(RuleParser parser) {
        for (FuzzyRule rule : this.rules) {
            if (rule.getStatus() == FuzzyRuleStatus.IDLE) {
                parser.parse(rule);
                if (logger.isTraceEnabled()) {
                    logger.trace(String.format("parsed rule \"%s\"", rule.getRuleText()));
                    logger.trace("parsing status=" + rule.getStatus() + ", parsing error: " + rule.getParsingError());
                }
            }
        }
    }

    public double evaluateRules() {
        return evaluateRules(1000);
    }

    public double evaluateRules(int numOfSteps) {
        double result;

        Deque<Double> degreeOfRelevances = new ArrayDeque<>();// pronounced "deck"
        List<MembershipFunction> conclusions = new ArrayList<>();

        if (getStatus() == FuzzyRuleStatus.DONE) {
            for (FuzzyRule rule : this.rules) {
                // compute degree of relevance (H)
                degreeOfRelevances.add(rule.computeDegreeOfRelevance());
                // compute conclusion
                conclusions.add(rule.computeConclusion(degreeOfRelevances.getLast()));
            }
            // compute superposition
            MembershipFunction[] cs = new MembershipFunction[this.rules.size()];
            double[][] superposition = MembershipFunction.computeSuperposition(conclusions.toArray(cs), numOfSteps);
            // defuzzify using center of mass approach
            result = MembershipFunction.computeCenterOfMass(superposition);
        } else {
            throw new RuntimeException(
                    String.format("Cannot evaluateRules rule set because its status is \"%s\".", getStatus()));
        }
        return result;
    }

    public FuzzyRuleStatus getStatus() {
        FuzzyRuleStatus result = FuzzyRuleStatus.DONE;
        for (FuzzyRule rule : this.rules) {
            result = (result == FuzzyRuleStatus.ERRONEOUS || result == FuzzyRuleStatus.IDLE) ? result : rule
                    .getStatus();
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Iterator<FuzzyRule> it = this.rules.iterator(); it.hasNext(); ) {
            FuzzyRule fuzzyRule = it.next();
            builder.append(fuzzyRule.getRuleText());
            builder.append(String.format(" | status = %s", fuzzyRule.getStatus()));
            if (fuzzyRule.getStatus() == FuzzyRuleStatus.ERRONEOUS) {
                builder.append(String.format(" | message = %s", fuzzyRule.getParsingError()));
            }
            if (it.hasNext()) {
                builder.append(System.getProperty("line.separator"));
            }
        }
        return builder.toString();
    }

}
