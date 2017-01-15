package ch.x01.fuzzy.engine;

/**
 * Created by developer on 05.01.17.
 */
public interface InputVariable {

    /**
     * Returns the name of this linguistic variable.
     *
     * @return name
     */
    String getName();

    /**
     * Returns the current crisp input value for this linguistic variable.
     *
     * @return current input value
     */
    double getInputValue();

    /**
     * Sets a crisp input value for this linguistic variable.
     *
     * @param value input
     */
    void setInputValue(double value);

    /**
     * Adds a linguistic term with its associated membership function to the variable's term set.
     *
     * @param name the name of linguistic term
     * @param mf   the associated membership function
     */
    void addTerm(String name, MembershipFunction mf);

}
