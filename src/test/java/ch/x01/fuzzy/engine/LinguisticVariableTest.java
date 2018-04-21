package ch.x01.fuzzy.engine;

import ch.x01.fuzzy.parser.SymbolTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        this.carSpeed.setValue(70);
        double fuzzy = this.carSpeed.is("low");
        assertEquals(0.75, fuzzy, 0.01);
        fuzzy = this.carSpeed.is("medium");
        assertEquals(0.25, fuzzy, 0.01);

        this.brakeForce.setValue(60);
        fuzzy = this.brakeForce.is("moderate");
        assertEquals(1.0, fuzzy, 0.01);
        fuzzy = this.brakeForce.is("strong");
        assertEquals(0.0, fuzzy, 0.1);

        this.brakeForce.setValue(70);
        fuzzy = this.brakeForce.is("moderate");
        assertEquals(0.5, fuzzy, 0.01);
        fuzzy = this.brakeForce.is("strong");
        assertEquals(0.0, fuzzy, 0.01);
    }

    @Test(expected = FuzzyEngineException.class)
    public final void testIsUndefinedTerm() {
        this.carSpeed.setValue(120);
        this.carSpeed.is("fast");
    }

}
