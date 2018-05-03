package ch.x01.fuzzy.core;

import ch.x01.fuzzy.parser.SymbolTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

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
public class LinguisticVariable {

    private static final Logger logger = LoggerFactory.getLogger(LinguisticVariable.class);

    private final String name;
    private final Map<String, MembershipFunction> termSet = new HashMap<>();
    private double value;

    /**
     * Constructs a linguistic variable. The constructed variable needs to be registered with the symbol table.
     *
     * @param name the name of this linguistic variable
     */
    public LinguisticVariable(String name) {
        this.name = name.toLowerCase();
    }

    /**
     * Constructs a linguistic variable and registers it with the symbol table.
     *
     * @param name        the name of this linguistic variable
     * @param symbolTable the table where linguistic variables and its terms are registered
     */
    public LinguisticVariable(String name, SymbolTable symbolTable) {
        this.name = name.toLowerCase();
        symbolTable.registerLV(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LinguisticVariable that = (LinguisticVariable) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
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
     * Returns the current crisp value for this linguistic variable.
     *
     * @return current value
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Sets a crisp value for this linguistic variable.
     *
     * @param value the value
     */
    public void setValue(double value) {
        this.value = value;
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Set crisp value = %.4f for linguistic variable \"%s\".", this.value, this.name));
        }
    }

    /**
     * Adds a linguistic term with its associated membership function to the variable's term set.
     *
     * @param name the name of linguistic term
     * @param mf   the associated membership function
     */
    public void addTerm(String name, MembershipFunction mf) {
        String term = name.toLowerCase();
        if (!this.termSet.containsKey(term)) {
            this.termSet.put(term, mf);
        } else {
            throw new RuntimeException(String.format(
                    "Cannot add linguistic term \"%s\" because it is already a member of the term set of linguistic variable \"%s\".",
                    term, this.name));
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
        double result;

        String term = name.toLowerCase();
        if (this.termSet.containsKey(term)) {
            MembershipFunction mf = this.termSet.get(term);
            result = mf.fuzzify(this.value);
        } else {
            throw new RuntimeException(String.format(
                    "Cannot compute fuzzification for linguistic term \"%s\" because it is not a member of the term set of linguistic variable \"%s\".",
                    term, this.name));
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
        MembershipFunction mf;

        String term = name.toLowerCase();
        if (this.termSet.containsKey(term)) {
            mf = this.termSet.get(term);
        } else {
            throw new RuntimeException(
                    String.format(
                            "Cannot retrieve membership function because linguistic term \"%s\" is not a member of the term set of linguistic variable \"%s\".",
                            term, this.name));
        }

        return mf;
    }

    public boolean containsTerm(String name) {
        return this.termSet.containsKey(name.toLowerCase());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Iterator<String> it = this.termSet.keySet()
                                               .iterator(); it.hasNext(); ) {
            builder.append(it.next());
            if (it.hasNext()) {
                builder.append(", ");
            }
        }

        return "T(" + this.name + ") = {" + builder.toString() + "}";
    }

}
