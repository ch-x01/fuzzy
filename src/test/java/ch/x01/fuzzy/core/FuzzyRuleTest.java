package ch.x01.fuzzy.core;

import ch.x01.fuzzy.parser.RuleParser;
import ch.x01.fuzzy.parser.SymbolTable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class FuzzyRuleTest {

    private SymbolTable symbolTable;

    @Before
    public void setUp() {
        symbolTable = new SymbolTable();
    }

    /**
     * Tests computation of <i>degree of relevance (H)</i> for a system with one input variable.
     */
    @Test
    public void testComputeDegreeOfRelevanceWithOneInput() {
        // define linguistic variable 'car speed'
        LinguisticVariable carSpeed = new LinguisticVariable("carSpeed", symbolTable);

        // define linguistic values for variable 'car speed'
        carSpeed.addTerm("low", new MembershipFunction(20, 60, 60, 100));
        carSpeed.addTerm("medium", new MembershipFunction(60, 100, 100, 140));

        // define linguistic variable 'brake force'
        LinguisticVariable brakeForce = new LinguisticVariable("brakeForce", symbolTable);

        // define linguistic values for variable 'brake force'
        brakeForce.addTerm("moderate", new MembershipFunction(40, 60, 60, 80));
        brakeForce.addTerm("strong", new MembershipFunction(70, 85, 85, 100));

        // define rules
        FuzzyRule rule1 = new FuzzyRule("if carSpeed is low then brakeForce is moderate", symbolTable);
        FuzzyRule rule2 = new FuzzyRule("if carSpeed is medium then brakeForce is strong", symbolTable);

        // parse rules
        RuleParser parser = new RuleParser(symbolTable);
        parser.parse(rule1);
        parser.parse(rule2);

        assertSame(FuzzyRuleStatus.DONE, rule1.getStatus());
        assertEquals("n/a", rule1.getParsingError());

        assertSame(FuzzyRuleStatus.DONE, rule2.getStatus());
        assertEquals("n/a", rule2.getParsingError());

        // set a crisp input value for carSpeed
        carSpeed.setValue(70.0);

        // compute degree of relevance (H)
        double h1 = rule1.computeDegreeOfRelevance();
        double h2 = rule2.computeDegreeOfRelevance();

        assertEquals(0.75, h1, 0.01);
        assertEquals(0.25, h2, 0.01);

    }

    /**
     * Tests computation of <i>degree of relevance (H)</i> for a system with two input variables.
     */
    @Test
    public void testComputeDegreeOfRelevanceWithTwoInput() {
        // define linguistic variable x1
        LinguisticVariable x1 = new LinguisticVariable("x1", symbolTable);

        // define linguistic values for variable x1
        x1.addTerm("negative", new MembershipFunction(0, 0, 0, 1));
        x1.addTerm("positive", new MembershipFunction(0, 1, 1, 1));

        // define linguistic variable x2
        LinguisticVariable x2 = new LinguisticVariable("x2", symbolTable);

        // define linguistic values for variable x2
        x2.addTerm("small", new MembershipFunction(0, 0, 0, 0.5));
        x2.addTerm("medium", new MembershipFunction(0, 0.5, 0.5, 1));
        x2.addTerm("large", new MembershipFunction(0.5, 1, 1, 1));

        // define linguistic variable u
        LinguisticVariable u = new LinguisticVariable("u", symbolTable);

        // define linguistic values for variable u
        u.addTerm("small", new MembershipFunction(0, 0, 0, 0.5));
        u.addTerm("medium", new MembershipFunction(0.2, 0.5, 0.5, 0.8));
        u.addTerm("large", new MembershipFunction(0.5, 1, 1, 1));

        // define rules
        FuzzyRule rule1 = new FuzzyRule("if x1 is positive and x2 is medium then u is medium", symbolTable);
        FuzzyRule rule2 = new FuzzyRule("if x1 is negative and x2 is small then u is small", symbolTable);

        // parse rules
        RuleParser parser = new RuleParser(symbolTable);
        parser.parse(rule1);
        parser.parse(rule2);

        assertSame(FuzzyRuleStatus.DONE, rule1.getStatus());
        assertEquals("n/a", rule1.getParsingError());

        assertSame(FuzzyRuleStatus.DONE, rule2.getStatus());
        assertEquals("n/a", rule2.getParsingError());

        // set crisp input values
        x1.setValue(0.65);
        x2.setValue(0.1);

        // compute degree of relevance
        double h1 = rule1.computeDegreeOfRelevance();
        double h2 = rule2.computeDegreeOfRelevance();

        assertEquals(0.2, h1, 0.01);
        assertEquals(0.8, h2, 0.01);

    }

}
