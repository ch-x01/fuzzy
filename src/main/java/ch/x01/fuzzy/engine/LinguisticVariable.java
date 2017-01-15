package ch.x01.fuzzy.engine;

import ch.x01.fuzzy.parser.SymbolTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class implements the concept of a linguistic variable. Linguistic variables take on values
 * defined in its term set - its set of linguistic terms. Linguistic terms are subjective categories
 * for the linguistic variable. For example, for linguistic variable 'age', the term set T(age) may
 * be defined as follows:
 * <p>
 * T(age)={"young", "middle aged", "old"}
 * </p>
 * Each linguistic term is associated with a reference fuzzy set, each of which has a defined
 * membership function (MF).
 */
public class LinguisticVariable implements InputVariable, OutputVariable {

    private static Logger logger = LoggerFactory.getLogger(LinguisticVariable.class);

    private final String name;

    private double inputValue;
    private double outputValue;

    private Map<String, MembershipFunction> termSet = new HashMap<>();

    /**
     * Constructs a linguistic variable and registers it with the symbol table.
     *
     * @param name        the name of this linguistic variable
     * @param symbolTable the table where linguistic variables and its terms are registered
     */
    public LinguisticVariable(String name, SymbolTable symbolTable) {
        this.name = name.toLowerCase();
        symbolTable.register(this);
    }

    /**
     * Returns the name of this linguistic variable.
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the computed output value for this linguistic variable in case this linguistic variable is part of a rule's conclusion.
     *
     * @return output value
     */
    public double getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(double outputValue) {
        this.outputValue = outputValue;
    }

    /**
     * Returns the current crisp input value for this linguistic variable.
     *
     * @return current input value
     */
    public double getInputValue() {
        return this.inputValue;
    }

    /**
     * Sets a crisp input value for this linguistic variable.
     *
     * @param value input
     */
    public void setInputValue(double value) {
        this.inputValue = value;
        if (logger.isTraceEnabled()) {
            logger.trace(
                    String.format("Set crisp input value=%1$1.4f for linguistic variable \"%2$s\".", value, this.name));
        }
    }

    /**
     * Adds a linguistic term with its associated membership function to the variable's term set.
     *
     * @param name the name of linguistic term
     * @param mf   the associated membership function
     */
    public void addTerm(String name, MembershipFunction mf) {
        if (!this.termSet.containsKey(name.toLowerCase())) {
            this.termSet.put(name.toLowerCase(), mf);
        } else {
            logger.warn(String.format(
                    "Cannot add term \"%1$s\" because it is already a member of the term set of linguistic variable \"%2$s\".",
                    name, this.name));
        }
    }

    /**
     * Computes the degree of membership for a crisp input value for a given linguistic term.
     *
     * @param name the name of linguistic term
     * @return degree of membership for the given linguistic term as a result of fuzzification or -1
     * if fuzzification is not possible.
     */
    public double is(String name) {
        double result = -1;

        if (containsTerm(name)) {
            MembershipFunction mf = getMembershipFunction(name);
            result = mf.fuzzify(this.inputValue);
        } else {
            logger.warn(String.format(
                    "Cannot compute fuzzification for term \"%1$s\" because it is not a member of the term set of linguistic variable \"%2$s\".",
                    name, this.name));
        }

        return result;
    }

    /**
     * Returns the membership function associated to the specified linguistic term.
     *
     * @param name the name of linguistic term
     * @return the associated membership function
     */
    public MembershipFunction getMembershipFunction(String name) {
        return this.termSet.get(name.toLowerCase());
    }

    public boolean containsTerm(String name) {
        return this.termSet.containsKey(name.toLowerCase());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Iterator<String> it = this.termSet.keySet().iterator(); it.hasNext(); ) {
            builder.append(it.next());
            if (it.hasNext()) {
                builder.append(", ");
            }
        }
        return "T(" + this.name + ") = {" + builder.toString() + "}";
    }
}
