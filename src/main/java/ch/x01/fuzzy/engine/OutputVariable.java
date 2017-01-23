package ch.x01.fuzzy.engine;

public interface OutputVariable {

    /**
     * Returns the name of this linguistic variable.
     *
     * @return name
     */
    String getName();

    /**
     * Returns the computed output value for this linguistic variable.
     *
     * @return output value
     */
    double getOutputValue();

    /**
     * Sets the computed output value for this linguistic variable.
     *
     * @param outputValue the output value
     */
    void setOutputValue(double outputValue);

    /**
     * Adds a linguistic term with its associated membership function to the variable's term set.
     *
     * @param name the name of linguistic term
     * @param mf   the associated membership function
     */
    void addTerm(String name, MembershipFunction mf);

}
