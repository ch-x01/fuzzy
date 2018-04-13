package ch.x01.fuzzy.api;

import org.junit.Test;

import static ch.x01.fuzzy.api.FuzzyModel.LinguisticVariable.Triangle.triangle;
import static ch.x01.fuzzy.api.FuzzyModel.LinguisticVariable.lv;
import static ch.x01.fuzzy.api.FuzzyModel.model;

public class FuzzyModelTest {

    @Test
    public void testFuzzyModel() {
        FuzzyModel model = model().name("car")
                                  .vars(lv().usage("input")
                                            .name("carSpeed")
                                            .terms(triangle("low", 20, 60, 100),
                                                   triangle("medium", 60, 100, 140)),
                                        lv().usage("output")
                                            .name("breakForce")
                                            .terms(triangle("moderate", 40, 60, 80),
                                                   triangle("strong", 70, 85, 100)))
                                  .rules("if carSpeed is low then brakeForce is moderate",
                                         "if carSpeed is medium then brakeForce is strong");

        System.out.println(model);

    }
}