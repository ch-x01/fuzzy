package ch.x01.fuzzy.engine;

import ch.x01.fuzzy.parser.RuleParser;
import ch.x01.fuzzy.parser.SymbolTable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FuzzyRuleSetTest {

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
    public void testFuzzyRuleSet() {
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
        FuzzyRuleSet ruleSet = new FuzzyRuleSet();
        ruleSet.addRule(new FuzzyRule("if carSpeed is low then brakeForce is moderate", symbolTable));
        ruleSet.addRule(new FuzzyRule("if carSpeed is medium then brakeForce is strong", symbolTable));

        System.out.println("--- rules");
        for (FuzzyRule rule : ruleSet.getRules()) {
            System.out.println(rule);
        }

        // parse rules
        ruleSet.parseRules(new RuleParser(symbolTable));
        System.out.println("--- parsing");
        System.out.println(String.format("parsing status of rule set is %s", ruleSet.getStatus()));
        assertEquals(ruleSet.getStatus(), FuzzyRuleStatus.DONE);

        // set a crisp input value for carSpeed
        carSpeed.setInputValue(70.0);

        System.out.println("--- crisp input value for carSpeed");
        System.out.println("value = " + carSpeed.getInputValue());

        // evaluate rules
        double result = ruleSet.evaluateRules();

        System.out.println("--- defuzzification");
        System.out.println("x = " + result);

        assertEquals(65.9939, result, 0.0001);
    }

}
