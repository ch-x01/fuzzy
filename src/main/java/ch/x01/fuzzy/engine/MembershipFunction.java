package ch.x01.fuzzy.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a trapezoidal membership function.
 * <p>
 * A membership function (MF) is a subjective measure for a linguistic term.
 * A linguistic term is a member of a term set associated to a linguistic variable.
 */
public class MembershipFunction {

    private static Logger logger = LoggerFactory.getLogger(MembershipFunction.class);

    private final double start;
    private final double left_top;
    private final double right_top;
    private final double end;
    private final double height;

    /**
     * Constructs a trapezoid.
     *
     * @param start     start point of trapezoid
     * @param left_top  left top point of trapezoid
     * @param right_top right top point of trapezoid
     * @param end       end point of trapezoid
     * @param height    height of trapezoid
     */
    public MembershipFunction(double start, double left_top, double right_top, double end, double height) {
        this.start = start;
        this.left_top = left_top;
        this.right_top = right_top;
        this.end = end;
        this.height = height;
    }

    /**
     * Constructs a trapezoid of normalised height.
     *
     * @param start     start point of trapezoid
     * @param left_top  left top point of trapezoid
     * @param right_top right top point of trapezoid
     * @param end       end point of trapezoid
     */
    public MembershipFunction(double start, double left_top, double right_top, double end) {
        this(start, left_top, right_top, end, 1);
    }

    /**
     * Constructs a triangle of normalised height.
     *
     * @param start start point of triangle
     * @param top   top point of triangle
     * @param end   end point of triangle
     */
    public MembershipFunction(double start, double top, double end) {
        this(start, top, top, end);
    }

    /**
     * Computes superposition of membership functions provided using the max-operator, that is
     * <code>u = max{u<sub>0</sub>(x), u<sub>1</sub>(x), .. , u<sub>n-1</sub>(x)}<code>.<br>
     * The more fine grained the discretisation is (number of discrete steps is high) the better the
     * computational accuracy.
     *
     * @param membershipFunctions array of membership functions
     *                            <code>[u<sub>0</sub>(x), u<sub>1</sub>(x), .. , u<sub>n-1</sub>(x)]</code>
     * @param numOfSteps          number of discrete steps
     * @return double[][] discrete membership function as a two-dimensional array where row<sub>0</sub>
     * represents the x-coordinate and row<sub>1</sub> represents the y-coordinate
     */
    public static double[][] computeSuperposition(MembershipFunction[] membershipFunctions, int numOfSteps) {
        double[][] result = new double[2][numOfSteps + 1];

        if (membershipFunctions.length >= 2) {
            double minSupport = 0.0;
            double maxSupport = 0.0;

            for (MembershipFunction mf : membershipFunctions) {
                // determine max and min support values
                if (mf.start < minSupport) {
                    minSupport = mf.start;
                }
                if (mf.end > maxSupport) {
                    maxSupport = mf.end;
                }
            }

            if (logger.isTraceEnabled()) {
                logger.trace("--- superposition");
            }

            double[][] du = membershipFunctions[0].plot(minSupport, maxSupport, numOfSteps);
            for (int k = 1; k < membershipFunctions.length; k++) {
                double[][] dk = membershipFunctions[k].plot(minSupport, maxSupport, numOfSteps);
                for (int i = 0; i < numOfSteps + 1; i++) {
                    result[0][i] = du[0][i];
                    result[1][i] = Math.max(du[1][i], dk[1][i]);
                }
            }

            if (logger.isTraceEnabled()) {
                String[] label = new String[]{"x -->", "y -->"};
                for (int row = 0; row < result.length; row++) {
                    StringBuilder builder = new StringBuilder(label[row]);
                    for (int col = 0; col < result[row].length; col++) {
                        builder.append(String.format("%12.4f", result[row][col]));
                    }
                    logger.trace(builder.toString());
                }
            }
        }

        return result;
    }

    /**
     * Computes the center of mass of a discretised linear function.
     *
     * @param function discretised linear function as a two-dimensional array where row<sub>0</sub>
     *                 represents the x-coordinate and row<sub>1</sub> represents the y-coordinate
     * @return X<sub>s</sub>, the value of the x-coordinate of center of mass
     */
    public static double computeCenterOfMass(double[][] function) {
        double sumNumerator = 0.0;
        double sumDenominator = 0.0;

        if (logger.isTraceEnabled()) {
            logger.trace("--- center of mass");
        }

        for (int i = 0; i < function[0].length - 1; i++) {
            double x1 = function[0][i];
            double x2 = function[0][i + 1];

            double y1 = function[1][i];
            double y2 = function[1][i + 1];

            double xsi = 0.5 * (x1 + x2);
            double Ai = 0.5 * (y1 + y2) * (x2 - x1);

            if (logger.isTraceEnabled()) {
                logger.trace(String.format("xsi = %1.4f", xsi));
                logger.trace(String.format("Ai  = %1.4f", Ai));
            }

            sumNumerator += (xsi * Ai);
            sumDenominator += Ai;

            if (logger.isTraceEnabled()) {
                logger.trace(String.format("sum(xsi * Ai) = %1.4f", sumNumerator));
                logger.trace(String.format("sum(Ai)       = %1.4f", sumDenominator));
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace(String.format("CoM = %1.4f", sumNumerator / sumDenominator));
        }

        return sumNumerator / sumDenominator;

    }

    /**
     * Computes the degree of membership for a linguistic term that is associated with this MF.
     *
     * @param x crisp input value
     * @return degree of membership
     */
    public double fuzzify(double x) {
        double result = 0;
        // check if input value is in range, if not, return 0
        if (x > this.start && x < this.end) {

            // determine which of 3 /-\ slopes works
            if (x >= this.left_top && x <= this.right_top) {
                // for middle part, return 1
                result = this.height;

            } else if (x >= this.start && x < this.left_top) {
                // compute ascending slope
                result = this.height * (x - this.start) / (this.left_top - this.start);

            } else if (x > this.right_top && x <= this.end) {
                // compute descending slope
                result = this.height * (this.end - x) / (this.end - this.right_top);
            }
        }
        return result;
    }

    public MembershipFunction computeReasoning(double degreeOfRelevance) {
        MembershipFunction result = null;

        if (this.height == 1.0) {
            double left_top = degreeOfRelevance * (this.left_top - this.start) + this.start;
            double right_top = this.end - degreeOfRelevance * (this.end - this.right_top);
            result = new MembershipFunction(this.start, left_top, right_top, this.end, degreeOfRelevance);
        }

        return result;
    }

    /**
     * Returns a discrete representation of this membership function.
     *
     * @param from       left x-axis value
     * @param to         right x-axis value
     * @param numOfSteps number of discrete steps
     * @return double[][] discrete membership function as a two-dimensional array where row<sub>0</sub>
     * represents the x-coordinate and row<sub>1</sub> represents the y-coordinate
     */
    public double[][] plot(double from, double to, int numOfSteps) {
        double[][] result = new double[2][numOfSteps + 1];

        double increment = Math.abs((to - from) / numOfSteps);

        if (logger.isTraceEnabled()) {
            logger.trace(String.format("discretisation: from=%1$1.4f to=%2$1.4f, numOfSteps=%3$d, increment=%4$1.4f", from, to, numOfSteps,
                    increment));
        }

        for (int i = 0; i < numOfSteps + 1; i++) {
            double x = from + increment * i;
            result[0][i] = x;
            result[1][i] = fuzzify(x);
        }

        return result;
    }

}
