## Fuzzy Logic Engine [![Build Status](https://travis-ci.org/ch-x01/fuzzy.svg?branch=master)](https://travis-ci.org/ch-x01/fuzzy)
A simple and lightweight fuzzy logic engine written in Java without utilising any third party library published under the MIT licence.
Currently, the binary size of the JAR file is **less than 40 KB**.

### Features
**Controller Type** Mamdani

**Membership Functions** Triangle, Trapezoid

**Reasoning Scheme** Max-Min Composition is used

**Defuzzifier** Center of Mass

### Example
```java
public class FuzzyEngineTest {
    /**
     * A simple reference system is given, which models the brake behaviour of a car driver
     * depending on the car speed. The inference machine should determine the brake force for a
     * given car speed. The speed is specified by the two linguistic terms 'low' and 'medium', and
     * the brake force by 'moderate' and 'strong'.
     */
    @Test
    public void testCar() {
        FuzzyEngine engine = new FuzzyEngine();

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

        // evaluate rules
        engine.evaluateRules();

        // print status info
        System.out.println(engine.toString());

        // test output value
        assertEquals(65.9939, brakeForce.getOutputValue(), 0.01);

        // compute output values for a range of input values
        for (int i = 0; i < 50; ++i) {
            double speed = 20 + i * (120.0 / 50);
            engine.evaluateRules(speed);
            System.out.println(engine.getResult());
        }
    }
 }
```

### Build
To build the project with Maven from the command line go to the directory `fuzzy` and run 
```bash
mvn clean install
```