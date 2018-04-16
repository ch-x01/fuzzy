package ch.x01.fuzzy.api;

import ch.x01.fuzzy.api.FuzzyEngine.InputVariable;
import ch.x01.fuzzy.api.FuzzyEngine.OutputVariable;
import org.junit.Before;
import org.junit.Test;

public class FuzzyEngineTest {

    private FuzzyModel model;

    @Before
    public void setUp() throws Exception {
        model = null;
    }

    @Test
    public void testFuzzyEngine() {

        FuzzyEngine engine = new FuzzyEngine(model);

        OutputVariable output = engine.evaluate(new InputVariable("", 0));

        System.out.println(output);

    }

    @Test
    public void testFuzzyEngineMultiInVars() {

        FuzzyEngine engine = new FuzzyEngine(model);

        OutputVariable output = engine.evaluate(new InputVariable("", 0), new InputVariable("", 0));

        System.out.println(output);

    }
}