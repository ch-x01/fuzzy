package ch.x01.fuzzy.api;

import ch.x01.fuzzy.api.FuzzyEngine.InputVariable;
import ch.x01.fuzzy.api.FuzzyEngine.OutputVariable;
import org.junit.Test;

import static ch.x01.fuzzy.api.FuzzyModel.LinguisticVariable.lv;
import static ch.x01.fuzzy.api.FuzzyModel.Term.trapezoid;
import static ch.x01.fuzzy.api.FuzzyModel.Term.triangle;
import static ch.x01.fuzzy.api.FuzzyModel.model;
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

        FuzzyModel model = model().name("car")
                                  .vars(lv().usage("input")
                                            .name("carSpeed")
                                            .terms(triangle().name("low")
                                                             .start(20)
                                                             .top(60)
                                                             .end(100),
                                                   triangle().name("medium")
                                                             .start(60)
                                                             .top(100)
                                                             .end(140)),
                                        lv().usage("output")
                                            .name("brakeForce")
                                            .terms(triangle().name("moderate")
                                                             .start(40)
                                                             .top(60)
                                                             .end(80),
                                                   triangle().name("strong")
                                                             .start(70)
                                                             .top(85)
                                                             .end(100)))
                                  .rules("if carSpeed is low then brakeForce is moderate",
                                         "if carSpeed is medium then brakeForce is strong");

        FuzzyEngine engine = new FuzzyEngine(model);

        OutputVariable output = engine.evaluate(new InputVariable("carSpeed", 70));

        // test output value
        assertEquals(65.9939, output.getValue(), 0.01);

        System.out.println(output);

    }

    @Test
    public void testCarInputValueRange() {

        FuzzyModel model = model().name("car")
                                  .vars(lv().usage("input")
                                            .name("carSpeed")
                                            .terms(triangle().name("low")
                                                             .start(20)
                                                             .top(60)
                                                             .end(100),
                                                   triangle().name("medium")
                                                             .start(60)
                                                             .top(100)
                                                             .end(140)),
                                        lv().usage("output")
                                            .name("brakeForce")
                                            .terms(triangle().name("moderate")
                                                             .start(40)
                                                             .top(60)
                                                             .end(80),
                                                   triangle().name("strong")
                                                             .start(70)
                                                             .top(85)
                                                             .end(100)))
                                  .rules("if carSpeed is low then brakeForce is moderate",
                                         "if carSpeed is medium then brakeForce is strong");

        FuzzyEngine engine = new FuzzyEngine(model);

        // compute output values for a range of input values
        for (int i = 0; i < 50; ++i) {
            double speed = 20 + i * (120.0 / 50);
            InputVariable input = new InputVariable("carSpeed", speed);
            OutputVariable output = engine.evaluate(input);
            System.out.println(engine.printResult(input, output, 6, 2));
        }

    }

    @Test
    public void testCarTrapezoid() {

        FuzzyModel model = model().name("car (trapezoid)")
                                  .vars(lv().usage("input")
                                            .name("carSpeed")
                                            .terms(trapezoid().name("low")
                                                              .start(20)
                                                              .left_top(60)
                                                              .right_top(60)
                                                              .end(100),
                                                   trapezoid().name("medium")
                                                              .start(60)
                                                              .left_top(100)
                                                              .right_top(1000)
                                                              .end(140)),
                                        lv().usage("output")
                                            .name("brakeForce")
                                            .terms(trapezoid().name("moderate")
                                                              .start(40)
                                                              .left_top(60)
                                                              .right_top(60)
                                                              .end(80),
                                                   trapezoid().name("strong")
                                                              .start(70)
                                                              .left_top(85)
                                                              .right_top(85)
                                                              .end(100)))
                                  .rules("if carSpeed is low then brakeForce is moderate",
                                         "if carSpeed is medium then brakeForce is strong");

        FuzzyEngine engine = new FuzzyEngine(model);

        OutputVariable output = engine.evaluate(new InputVariable("carSpeed", 70));

        // test output value
        assertEquals(65.9939, output.getValue(), 0.01);

        System.out.println(output);
    }

    @Test(expected = RuntimeException.class)
    public void testCarInvalidModel() {

        FuzzyModel model = model().name("car")
                                  .vars(lv().usage("input")
                                            .name("carSpeed")
                                            .terms(triangle().name("low")
                                                             .start(20)
                                                             .top(60)
                                                             .end(100),
                                                   triangle().name("medium")
                                                             .start(60)
                                                             .top(100)
                                                             .end(140)),
                                        lv().usage("output")
                                            .name("breakForce")
                                            .terms(triangle().name("moderate")
                                                             .start(40)
                                                             .top(60)
                                                             .end(80),
                                                   triangle().name("strong")
                                                             .start(70)
                                                             .top(85)
                                                             .end(100)))
                                  .rules("if carSpeed is low then brakeForce is moderate",
                                         "if carSpeed is medium then brakeForce is strong");

        FuzzyEngine engine = new FuzzyEngine(model);

        engine.evaluate(new InputVariable("carSpeed", 70));

    }

    /**
     * A simple reference system is given, which models models the power behaviour of a dimmer
     * depending on the ambient light. The inference machine should determine the power for a
     * given ambient light.<br>
     * The example is adapted from the example presented at <a href="www.fuzzylite.com/java">www.fuzzylite.com/java</a>
     */
    @Test
    public void testDimmer() {

        FuzzyModel model = model().name("dimmer")
                                  .vars(lv().usage("input")
                                            .name("ambient")
                                            .terms(triangle().name("dark")
                                                             .start(0)
                                                             .top(0.25)
                                                             .end(0.5),
                                                   triangle().name("medium")
                                                             .start(0.25)
                                                             .top(0.5)
                                                             .end(0.75),
                                                   triangle().name("bright")
                                                             .start(0.5)
                                                             .top(0.75)
                                                             .end(1)),
                                        lv().usage("output")
                                            .name("power")
                                            .terms(triangle().name("low")
                                                             .start(0)
                                                             .top(0.25)
                                                             .end(0.5),
                                                   triangle().name("medium")
                                                             .start(0.25)
                                                             .top(0.5)
                                                             .end(0.75),
                                                   triangle().name("high")
                                                             .start(0.5)
                                                             .top(0.75)
                                                             .end(1)))
                                  .rules("if Ambient is DARK then Power is HIGH",
                                         "if Ambient is MEDIUM then Power is MEDIUM",
                                         "if Ambient is BRIGHT then Power is LOW");

        FuzzyEngine engine = new FuzzyEngine(model);

        OutputVariable output = engine.evaluate(new InputVariable("ambient", 0.25));

        // test output value
        assertEquals(0.75, output.getValue(), 0.01);

        System.out.println(output);

    }

    @Test
    public void testDimmerInputValueRange() {

        FuzzyModel model = model().name("dimmer")
                                  .vars(lv().usage("input")
                                            .name("ambient")
                                            .terms(triangle().name("dark")
                                                             .start(0)
                                                             .top(0.25)
                                                             .end(0.5),
                                                   triangle().name("medium")
                                                             .start(0.25)
                                                             .top(0.5)
                                                             .end(0.75),
                                                   triangle().name("bright")
                                                             .start(0.5)
                                                             .top(0.75)
                                                             .end(1)),
                                        lv().usage("output")
                                            .name("power")
                                            .terms(triangle().name("low")
                                                             .start(0)
                                                             .top(0.25)
                                                             .end(0.5),
                                                   triangle().name("medium")
                                                             .start(0.25)
                                                             .top(0.5)
                                                             .end(0.75),
                                                   triangle().name("high")
                                                             .start(0.5)
                                                             .top(0.75)
                                                             .end(1)))
                                  .rules("if Ambient is DARK then Power is HIGH",
                                         "if Ambient is MEDIUM then Power is MEDIUM",
                                         "if Ambient is BRIGHT then Power is LOW");

        FuzzyEngine engine = new FuzzyEngine(model);

        // compute output values for a range of input values
        for (int i = 0; i < 50; i++) {
            double light = 0 + i * (1.0 / 50);
            InputVariable input = new InputVariable("ambient", light);
            OutputVariable output = engine.evaluate(input);
            System.out.println(engine.printResult(input, output, 4, 2));
        }

    }

    @Test
    public void testDimmerBug() {

        FuzzyModel model = model().name("dimmer")
                                  .vars(lv().usage("input")
                                            .name("ambient")
                                            .terms(triangle().name("dark")
                                                             .start(0)
                                                             .top(0.25)
                                                             .end(0.5),
                                                   triangle().name("medium")
                                                             .start(0.25)
                                                             .top(0.5)
                                                             .end(0.75),
                                                   triangle().name("bright")
                                                             .start(0.5)
                                                             .top(0.75)
                                                             .end(1)),
                                        lv().usage("output")
                                            .name("power")
                                            .terms(triangle().name("low")
                                                             .start(0)
                                                             .top(0.25)
                                                             .end(0.5),
                                                   triangle().name("medium")
                                                             .start(0.25)
                                                             .top(0.5)
                                                             .end(0.75),
                                                   triangle().name("high")
                                                             .start(0.5)
                                                             .top(0.75)
                                                             .end(1)))
                                  .rules("if Ambient is DARK then Power is HIGH",
                                         "if Ambient is MEDIUM then Power is MEDIUM",
                                         "if Ambient is BRIGHT then Power is LOW");

        FuzzyEngine engine = new FuzzyEngine(model);

        OutputVariable output = engine.evaluate(new InputVariable("ambient", 0.50));

        // test output value
        assertEquals(0.49, output.getValue(), 0.01);

        System.out.println(output);

    }

    @Test
    public void testTip() {

        FuzzyModel model = model().name("tip")
                                  .vars(lv().usage("input")
                                            .name("service")
                                            .terms(trapezoid().name("poor")
                                                              .start(0)
                                                              .left_top(2)
                                                              .right_top(4)
                                                              .end(6),
                                                   trapezoid().name("good")
                                                              .start(4)
                                                              .left_top(6)
                                                              .right_top(8)
                                                              .end(10),
                                                   trapezoid().name("excellent")
                                                              .start(8)
                                                              .left_top(10)
                                                              .right_top(12)
                                                              .end(14)),
                                        lv().usage("input")
                                            .name("food")
                                            .terms(trapezoid().name("rancid")
                                                              .start(0)
                                                              .left_top(2)
                                                              .right_top(4)
                                                              .end(6),
                                                   trapezoid().name("tasty")
                                                              .start(4)
                                                              .left_top(6)
                                                              .right_top(8)
                                                              .end(10),
                                                   trapezoid().name("delicious")
                                                              .start(8)
                                                              .left_top(10)
                                                              .right_top(12)
                                                              .end(14)),
                                        lv().usage("output")
                                            .name("tip")
                                            .terms(trapezoid().name("poor")
                                                              .start(0)
                                                              .left_top(2)
                                                              .right_top(4)
                                                              .end(6),
                                                   trapezoid().name("average")
                                                              .start(4)
                                                              .left_top(6)
                                                              .right_top(8)
                                                              .end(10),
                                                   trapezoid().name("generous")
                                                              .start(8)
                                                              .left_top(10)
                                                              .right_top(12)
                                                              .end(14)))
                                  .rules("if (food is rancid or service is poor) then tip is poor",
                                         "if (food is tasty and service is good) then tip is average",
                                         "if (food is tasty and service is excellent) then tip is generous",
                                         "if (food is delicious and service is good) then tip is average",
                                         "if (food is delicious and service is excellent) then tip is generous"
                                  );

        FuzzyEngine engine = new FuzzyEngine(model);

        // rancid food, poor service
        OutputVariable output = engine.evaluate(new InputVariable("food", 3), new InputVariable("service", 3));
        assertEquals(3, output.getValue(), 0.01);
        System.out.println(output);

        // tasty food, good service
        output = engine.evaluate(new InputVariable("food", 7), new InputVariable("service", 7));
        assertEquals(7, output.getValue(), 0.01);
        System.out.println(output);

        // tasty food, excellent service
        output = engine.evaluate(new InputVariable("food", 7), new InputVariable("service", 11));
        assertEquals(11, output.getValue(), 0.01);
        System.out.println(output);

        // delicious food, good service
        output = engine.evaluate(new InputVariable("food", 11), new InputVariable("service", 7));
        assertEquals(7, output.getValue(), 0.01);
        System.out.println(output);

        // delicious food, excellent service
        output = engine.evaluate(new InputVariable("food", 11), new InputVariable("service", 11));
        assertEquals(11, output.getValue(), 0.01);
        System.out.println(output);

    }

    @Test
    public void testTipBug() {

        // AND or OR are evaluated correct within brackets only
        FuzzyModel model = model().name("tip")
                                  .vars(lv().usage("input")
                                            .name("service")
                                            .terms(trapezoid().name("poor")
                                                              .start(0)
                                                              .left_top(2)
                                                              .right_top(4)
                                                              .end(6),
                                                   trapezoid().name("good")
                                                              .start(4)
                                                              .left_top(6)
                                                              .right_top(8)
                                                              .end(10),
                                                   trapezoid().name("excellent")
                                                              .start(8)
                                                              .left_top(10)
                                                              .right_top(12)
                                                              .end(14)),
                                        lv().usage("input")
                                            .name("food")
                                            .terms(trapezoid().name("rancid")
                                                              .start(0)
                                                              .left_top(2)
                                                              .right_top(4)
                                                              .end(6),
                                                   trapezoid().name("tasty")
                                                              .start(4)
                                                              .left_top(6)
                                                              .right_top(8)
                                                              .end(10),
                                                   trapezoid().name("delicious")
                                                              .start(8)
                                                              .left_top(10)
                                                              .right_top(12)
                                                              .end(14)),
                                        lv().usage("output")
                                            .name("tip")
                                            .terms(trapezoid().name("poor")
                                                              .start(0)
                                                              .left_top(2)
                                                              .right_top(4)
                                                              .end(6),
                                                   trapezoid().name("average")
                                                              .start(4)
                                                              .left_top(6)
                                                              .right_top(8)
                                                              .end(10),
                                                   trapezoid().name("generous")
                                                              .start(8)
                                                              .left_top(10)
                                                              .right_top(12)
                                                              .end(14)))
                                  .rules("if (food is rancid or service is poor) then tip is poor",
                                         "if (food is tasty and service is good) then tip is average",
                                         "if (food is tasty and service is excellent) then tip is generous",
                                         "if (food is delicious and service is good) then tip is average",
                                         "if (food is delicious and service is excellent) then tip is generous"
                                  );

        FuzzyEngine engine = new FuzzyEngine(model);

        // rancid food, good service
        OutputVariable output = engine.evaluate(new InputVariable("food", 3), new InputVariable("service", 7));
        assertEquals(3, output.getValue(), 0.01);
        System.out.println(output);

    }

}