package ch.x01.fuzzy.engine;

/**
 * The rule's parsing status, that is, whether the rule was parsed or not, whether the parsing
 * was successful or not.
 */
public enum FuzzyRuleStatus {
    /**
     * The rule was not yet parsed.
     */
    IDLE,

    /**
     * The rule was parsed successfully.
     */
    DONE,

    /**
     * The rule was parsed with errors.
     */
    ERRONEOUS
}
