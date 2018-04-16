package ch.x01.fuzzy.api;

import org.junit.Test;

import static ch.x01.fuzzy.api.FuzzyModel.Term.trapezoid;
import static ch.x01.fuzzy.api.FuzzyModel.Term.triangle;
import static ch.x01.fuzzy.api.FuzzyModel.LinguisticVariable.lv;
import static ch.x01.fuzzy.api.FuzzyModel.model;

public class FuzzyModelTest {

    @Test
    public void testFuzzyModelCarTriangle() {
        FuzzyModel model = model().name("car driver (triangle)")
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

        System.out.println(model);
    }

    @Test
    public void testFuzzyModelCarTrapezoid() {
        FuzzyModel model = model().name("car driver (trapezoid)")
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
                                            .name("breakForce")
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

        System.out.println(model);
    }

    @Test
    public void testFuzzyModelAmbientLight() {
        FuzzyModel model = model().name("ambient light")
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

        System.out.println(model);
    }

}