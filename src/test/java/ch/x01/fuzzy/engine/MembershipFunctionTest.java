package ch.x01.fuzzy.engine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MembershipFunctionTest {

    private MembershipFunction trapezoid;
    private MembershipFunction triangle;

    /**
     * Prints values of a 2-dimensional array to system output.
     *
     * @param array two-dimensional array where row<sub>0</sub> represents the x-coordinate and row<sub>1</sub>
     *              represents the y-coordinate
     */
    private void printArray(double[][] array) {
        String[] label = new String[]{"x -->", "y -->"};
        for (int row = 0; row < array.length; row++) {
            StringBuilder builder = new StringBuilder(label[row]);
            for (int col = 0; col < array[row].length; col++) {
                builder.append(String.format("%8.4f", array[row][col]));
            }
            System.out.println(builder.toString());
        }
    }

    @Before
    public void setUp() throws Exception {
        this.trapezoid = new MembershipFunction(0.0, 1.0, 3.0, 4.0);
        this.triangle = new MembershipFunction(2.0, 5.0, 8.0);
    }

    @After
    public void tearDown() throws Exception {
        this.trapezoid = null;
        this.triangle = null;
    }

    @Test
    public final void testFuzzifyTrapezoid() {
        assertEquals(0.0, this.trapezoid.fuzzify(0.0), 0.0);
        assertEquals(1.0, this.trapezoid.fuzzify(1.0), 0.0);
        assertEquals(1.0, this.trapezoid.fuzzify(3.0), 0.0);
        assertEquals(0.0, this.trapezoid.fuzzify(4.0), 0.0);
    }

    @Test
    public final void testFuzzifyTriangle() {
        assertEquals(0.0, this.triangle.fuzzify(2.0), 0.0);
        assertEquals(1.0, this.triangle.fuzzify(5.0), 0.0);
        assertEquals(0.0, this.triangle.fuzzify(8.0), 0.0);
    }

    @Test
    public final void testComputeReasoningTrapezoid() {
        MembershipFunction mf = this.trapezoid.computeReasoning(0.5);
        assertEquals(0.5, mf.fuzzify(0.5), 0.0);
        assertEquals(0.5, mf.fuzzify(3.5), 0.0);
        assertEquals(0.5, mf.fuzzify(2.0), 0.0);
        assertEquals(0.25, mf.fuzzify(0.25), 0.0);
        assertEquals(0.25, mf.fuzzify(3.75), 0.0);
    }

    @Test
    public final void testComputeReasoningTriangle() {
        MembershipFunction mf = this.triangle.computeReasoning(0.65);
        assertEquals(0.65, mf.fuzzify(4.0), 0.0);
        assertEquals(0.65, mf.fuzzify(6.0), 0.0);
        assertEquals(0.65, mf.fuzzify(5.0), 0.0);
        assertEquals(0.5, mf.fuzzify(3.5), 0.0);
        assertEquals(0.5, mf.fuzzify(6.5), 0.0);
    }

    @Test
    public final void testPlotTrapezoid() {
        double from = 0.0;
        double to = 8.0;
        int numberOfIncrements = 8;

        System.out.println("--- plot trapezoid");

        double[][] dmf = this.trapezoid.plot(from, to, numberOfIncrements);

        printArray(dmf);

        for (int i = 0; i < dmf[0].length; i++) {
            assertEquals(dmf[0][i], (double) i, 0.01);
        }
        assertEquals(dmf[1][0], 0.00, 0.01);
        assertEquals(dmf[1][1], 1.00, 0.01);
        assertEquals(dmf[1][2], 1.00, 0.01);
        assertEquals(dmf[1][3], 1.00, 0.01);
        assertEquals(dmf[1][4], 0.00, 0.01);
        assertEquals(dmf[1][5], 0.00, 0.01);
        assertEquals(dmf[1][6], 0.00, 0.01);
        assertEquals(dmf[1][7], 0.00, 0.01);
        assertEquals(dmf[1][8], 0.00, 0.01);
    }

    @Test
    public final void testPlotTriangle() {
        double from = 0.0;
        double to = 8.0;
        int numberOfIncrements = 8;

        System.out.println("--- plot triangle");

        double[][] dmf = this.triangle.plot(from, to, numberOfIncrements);

        printArray(dmf);

        for (int i = 0; i < dmf[0].length; i++) {
            assertEquals(dmf[0][i], (double) i, 0.01);
        }
        assertEquals(dmf[1][0], 0.00, 0.01);
        assertEquals(dmf[1][1], 0.00, 0.01);
        assertEquals(dmf[1][2], 0.00, 0.01);
        assertEquals(dmf[1][3], 0.33, 0.01);
        assertEquals(dmf[1][4], 0.66, 0.01);
        assertEquals(dmf[1][5], 1.00, 0.01);
        assertEquals(dmf[1][6], 0.66, 0.01);
        assertEquals(dmf[1][7], 0.33, 0.01);
        assertEquals(dmf[1][8], 0.00, 0.01);
    }

    @Test
    public final void testComputeSuperposition() {
        int numberOfIncrements = 8;

        System.out.println("--- superposition(trapezoid,triangle)");

        MembershipFunction[] mfs = {this.trapezoid.computeReasoning(0.5), this.triangle.computeReasoning(0.65)};
        double[][] superposition = MembershipFunction.computeSuperposition(mfs, numberOfIncrements);

        printArray(superposition);

        for (int i = 0; i < superposition[0].length; i++) {
            assertEquals(superposition[0][i], (double) i, 0.01);
        }
        assertEquals(superposition[1][0], 0.00, 0.01);
        assertEquals(superposition[1][1], 0.50, 0.01);
        assertEquals(superposition[1][2], 0.50, 0.01);
        assertEquals(superposition[1][3], 0.50, 0.01);
        assertEquals(superposition[1][4], 0.65, 0.01);
        assertEquals(superposition[1][5], 0.65, 0.01);
        assertEquals(superposition[1][6], 0.65, 0.01);
        assertEquals(superposition[1][7], 0.33, 0.01);
        assertEquals(superposition[1][8], 0.00, 0.01);
    }

    @Test
    public final void testComputeCenterOfMassSuperposition() {
        int numberOfIncrements = 8;

        System.out.println("--- Center of Mass (superposition of trapezoid and triangle)");

        MembershipFunction[] mfs = {this.trapezoid.computeReasoning(0.5), this.triangle.computeReasoning(0.65)};
        double[][] superposition = MembershipFunction.computeSuperposition(mfs, numberOfIncrements);
        double com = MembershipFunction.computeCenterOfMass(superposition);

        System.out.println("CoM = " + com);

        assertEquals(3.9867, com, 0.0001);
    }

    @Test
    public final void testComputeCenterOfMassZeroArray() {
        double[][] zeroArray = new double[2][5];

        System.out.println("--- Center of Mass (zero array)");

        double com = MembershipFunction.computeCenterOfMass(zeroArray);

        System.out.println("CoM = " + com);

        assertEquals(Double.NaN, com, 0.01);
    }

    @Test
    public final void testComputeCenterOfMassRectangle() {
        double[][] rectangle = new double[][]{{0, 1, 2, 3, 4}, {0, 0, 4, 4, 0}};

        System.out.println("--- Center of Mass (rectangle)");

        double com = MembershipFunction.computeCenterOfMass(rectangle);

        System.out.println("CoM = " + com);

        assertEquals(2.5, com, 0.01);
    }

}
