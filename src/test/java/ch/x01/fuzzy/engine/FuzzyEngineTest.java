package ch.x01.fuzzy.engine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FuzzyEngineTest {

    /**
     * A simple reference system is given, which models the brake behaviour of a car driver
     * depending on the car speed. The inference machine should determine the brake force for a
     * given car speed. The speed is specified by the two linguistic terms 'low' and 'medium', and
     * the brake force by 'moderate' and 'strong'.
     */
    @Test
    public void testCar() {
        FuzzyEngine engine = new FuzzyEngine(100);

        // define input variable 'car speed'
        InputVariable carSpeed = engine.addInputVariable("carSpeed");
        carSpeed.addTerm("low", new MembershipFunction(20, 60, 100));
        carSpeed.addTerm("medium", new MembershipFunction(60, 100, 140));

        // define output variable 'brake force'
        OutputVariable brakeForce = engine.addOutputVariable("brakeForce");
        brakeForce.addTerm("moderate", new MembershipFunction(40, 60, 80));
        brakeForce.addTerm("strong", new MembershipFunction(70, 85, 100));

        // define rules
        FuzzyRuleSet ruleSet = engine.addRuleSet();
        ruleSet.addRule(FuzzyRule.parse("if carSpeed is low then brakeForce is moderate", engine));
        ruleSet.addRule(FuzzyRule.parse("if carSpeed is medium then brakeForce is strong", engine));

        // set a crisp input value for carSpeed
        carSpeed.setInputValue(70);

        // evaluateRules rules
        engine.evaluateRules();

        // print status info
        System.out.println(engine.toString());

        assertEquals(65.9939, brakeForce.getOutputValue(), 0.01);

        for (int i = 0; i < 50; ++i) {
            double speed = 20 + i * (120.0 / 50);
            engine.evaluateRules(speed);
            System.out.println(engine.getResult());
        }
    }

    /**
     * A simple reference system is given, which models models the power behaviour of a dimmer
     * depending on the ambient light. The inference machine should determine the power for a
     * given ambient light.<br>
     * The example is adapted from the example presented at <a href="www.fuzzylite.com/java">www.fuzzylite.com/java</a>
     */
    @Test
    public void testDimmer() throws Exception {
        FuzzyEngine engine = new FuzzyEngine();

        InputVariable ambient = engine.addInputVariable("Ambient");
        ambient.addTerm("DARK", new MembershipFunction(0.00, 0.25, 0.50));
        ambient.addTerm("MEDIUM", new MembershipFunction(0.25, 0.50, 0.75));
        ambient.addTerm("BRIGHT", new MembershipFunction(0.50, 0.75, 1.00));

        OutputVariable power = engine.addOutputVariable("Power");
        power.addTerm("LOW", new MembershipFunction(0.00, 0.25, 0.50));
        power.addTerm("MEDIUM", new MembershipFunction(0.25, 0.50, 0.75));
        power.addTerm("HIGH", new MembershipFunction(0.50, 0.75, 1.00));

        FuzzyRuleSet ruleSet = engine.addRuleSet();
        ruleSet.addRule(FuzzyRule.parse("if Ambient is DARK then Power is HIGH", engine));
        ruleSet.addRule(FuzzyRule.parse("if Ambient is MEDIUM then Power is MEDIUM", engine));
        ruleSet.addRule(FuzzyRule.parse("if Ambient is BRIGHT then Power is LOW", engine));

        ambient.setInputValue(0.25);

        engine.evaluateRules();

        System.out.println(engine.toString());

        assertEquals(0.75, power.getOutputValue(), 0.01);

        for (int i = 0; i < 50; i++) {
            double light = 0 + i * (1.0 / 50);
            engine.evaluateRules(light);
            System.out.println(engine.getResult());
        }

    }
}
