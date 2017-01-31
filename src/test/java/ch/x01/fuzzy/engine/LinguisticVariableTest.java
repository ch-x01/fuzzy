package ch.x01.fuzzy.engine;

import ch.x01.fuzzy.parser.SymbolTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LinguisticVariableTest {

    private LinguisticVariable carSpeed;
    private LinguisticVariable brakeForce;

    @Before
    public void setUp() throws Exception {
        SymbolTable symbolTable = new SymbolTable();

        // define linguistic variable 'car speed'
        this.carSpeed = new LinguisticVariable("carSpeed", symbolTable);

        // define linguistic values for variable 'car speed'
        this.carSpeed.addTerm("low", new MembershipFunction(20, 60, 60, 100));
        this.carSpeed.addTerm("medium", new MembershipFunction(60, 100, 100, 140));

        // define linguistic variable 'brake force'
        this.brakeForce = new LinguisticVariable("brakeForce", symbolTable);

        // define linguistic values for variable 'brake force'
        this.brakeForce.addTerm("moderate", new MembershipFunction(40, 60, 60, 80));
        this.brakeForce.addTerm("strong", new MembershipFunction(70, 85, 85, 100));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testIs() {
        this.carSpeed.setInputValue(70);
        double fuzzy = this.carSpeed.is("low");
        assertTrue(fuzzy == 0.75);
        fuzzy = this.carSpeed.is("medium");
        assertTrue(fuzzy == 0.25);

        this.brakeForce.setInputValue(60);
        fuzzy = this.brakeForce.is("moderate");
        assertTrue(fuzzy == 1.0);
        fuzzy = this.brakeForce.is("strong");
        assertTrue(fuzzy == 0.0);

        this.brakeForce.setInputValue(70);
        fuzzy = this.brakeForce.is("moderate");
        assertTrue(fuzzy == 0.5);
        fuzzy = this.brakeForce.is("strong");
        assertTrue(fuzzy == 0.0);
    }

    @Test(expected = FuzzyEngineException.class)
    public final void testIsUndefinedTerm() {
        this.carSpeed.setInputValue(120);
        double fuzzy = this.carSpeed.is("fast");
        assertTrue(fuzzy == -1);
    }

}
