package ch.x01.fuzzy.engine;

import ch.x01.fuzzy.parser.RuleParser;
import ch.x01.fuzzy.parser.SymbolTable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FuzzySystemTest {

    private SymbolTable symbolTable;

    @Before
    public void setUp() throws Exception {
        symbolTable = new SymbolTable();
    }

    /**
     * A simple reference system is given, which models the brake behaviour of a car driver
     * depending on the car speed. The inference machine should determine the brake force for a
     * given car speed. The speed is specified by the two linguistic terms 'low' and 'medium', and
     * the brake force by 'moderate' and 'strong'.
     */
    @Test
    public void testFuzzySystem() {
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

        System.out.println("--- linguistic variables");
        for (LinguisticVariable lv : symbolTable.getLinguisticVariables()) {
            System.out.println(lv);
        }

        // define rules
        FuzzyRule rule1 = new FuzzyRule("if carSpeed is low then brakeForce is moderate", symbolTable);
        FuzzyRule rule2 = new FuzzyRule("if carSpeed is medium then brakeForce is strong", symbolTable);

        System.out.println("--- rules");
        System.out.println(rule1.getRuleText());
        System.out.println(rule2.getRuleText());

        // parse rules
        RuleParser parser = new RuleParser(symbolTable);
        parser.parse(rule1);
        parser.parse(rule2);
        System.out.println("--- parsing");
        System.out.println("parsing status=" + rule1.getStatus() + ", parsing error: " + rule1.getParsingError());
        assertTrue(rule1.getStatus() == FuzzyRuleStatus.DONE && rule1.getParsingError().equals("n/a"));
        System.out.println("parsing status=" + rule2.getStatus() + ", parsing error: " + rule2.getParsingError());
        assertTrue(rule2.getStatus() == FuzzyRuleStatus.DONE && rule2.getParsingError().equals("n/a"));

        // set a crisp input value for carSpeed
        carSpeed.setInputValue(70);

        System.out.println("--- crisp input value for carSpeed");
        System.out.println("value = " + carSpeed.getInputValue());

        // compute degree of relevance (H)
        double h1 = rule1.computeDegreeOfRelevance();
        double h2 = rule2.computeDegreeOfRelevance();

        assertTrue(h1 == 0.75);
        assertTrue(h2 == 0.25);

        // compute conclusion
        MembershipFunction[] conclusions = new MembershipFunction[2];
        conclusions[0] = rule1.computeConclusion();
        conclusions[1] = rule2.computeConclusion();

        // compute superposition
        double[][] superposition = MembershipFunction.computeSuperposition(conclusions, 1000);

        // defuzzify using center of mass approach
        double CoM = MembershipFunction.computeCenterOfMass(superposition);

        System.out.println("--- defuzzification");
        System.out.println("x = " + CoM);

    }

}
