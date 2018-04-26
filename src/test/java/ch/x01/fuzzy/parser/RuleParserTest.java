package ch.x01.fuzzy.parser;

import ch.x01.fuzzy.core.FuzzyRule;
import ch.x01.fuzzy.core.FuzzyRuleStatus;
import ch.x01.fuzzy.core.LinguisticVariable;
import ch.x01.fuzzy.core.MembershipFunction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RuleParserTest {

    private SymbolTable symbolTable;

    private String printPremise(FuzzyRule rule) {
        StringBuilder builder = new StringBuilder();

        for (String token : rule.getPremises()) {
            builder.append(token);
            builder.append(" ");
        }

        return builder.toString();
    }

    private String printConclusion(FuzzyRule rule) {
        StringBuilder builder = new StringBuilder();

        for (String token : rule.getConclusion()) {
            builder.append(token);
            builder.append(" ");
        }

        return builder.toString();
    }

    @Before
    public void setUp() {
        symbolTable = new SymbolTable();
    }

    @Test
    public final void testParse() {
        RuleParser parser = new RuleParser();

        // define rules and parse'em
        FuzzyRule rule = new FuzzyRule("if (x1 is a1 or (x2 is a2 and x3 is a3) or x4 is a4) then y is b", symbolTable);
        parser.parse(rule);

        assertSame(FuzzyRuleStatus.DONE, rule.getStatus());
        assertEquals("n/a", rule.getParsingError());

        ArrayList<Token> tokens = rule.getTokens();
        assertEquals(Token.IF, tokens.get(1));
        assertEquals(Token.LEFT_PAR, tokens.get(2));
        assertEquals(Token.IDENT, tokens.get(3));
        assertEquals(Token.IS, tokens.get(4));
        assertEquals(Token.IDENT, tokens.get(5));
        assertEquals(Token.OR, tokens.get(6));
        assertEquals(Token.LEFT_PAR, tokens.get(7));
        assertEquals(Token.IDENT, tokens.get(8));
        assertEquals(Token.IS, tokens.get(9));
        assertEquals(Token.IDENT, tokens.get(10));
        assertEquals(Token.AND, tokens.get(11));
        assertEquals(Token.IDENT, tokens.get(12));
        assertEquals(Token.IS, tokens.get(13));
        assertEquals(Token.IDENT, tokens.get(14));
        assertEquals(Token.RIGHT_PAR, tokens.get(15));
        assertEquals(Token.OR, tokens.get(16));
        assertEquals(Token.IDENT, tokens.get(17));
        assertEquals(Token.IS, tokens.get(18));
        assertEquals(Token.IDENT, tokens.get(19));
        assertEquals(Token.RIGHT_PAR, tokens.get(20));
        assertEquals(Token.THEN, tokens.get(21));
        assertEquals(Token.IDENT, tokens.get(22));
        assertEquals(Token.IS, tokens.get(23));
        assertEquals(Token.IDENT, tokens.get(24));
        assertEquals(Token.END, tokens.get(25));
        assertEquals(26, tokens.size());

        rule = new FuzzyRule("if x1 is or x2 is a2 then y is b", symbolTable);
        parser.parse(rule);

        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Syntax error: IDENT expected", rule.getParsingError());

        tokens = rule.getTokens();
        assertEquals(Token.START, tokens.get(0));
        assertEquals(Token.IF, tokens.get(1));
        assertEquals(Token.IDENT, tokens.get(2));
        assertEquals(Token.IS, tokens.get(3));
        assertEquals(4, tokens.size());

        rule = new FuzzyRule("if  is a1 and x2 is a2 then y is b", symbolTable);
        parser.parse(rule);

        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Syntax error: IDENT expected", rule.getParsingError());

        tokens = rule.getTokens();
        assertEquals(Token.START, tokens.get(0));
        assertEquals(Token.IF, tokens.get(1));
        assertEquals(2, tokens.size());

        rule = new FuzzyRule("if x1 is a1 or x2  a2 then y is b", symbolTable);
        parser.parse(rule);

        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Syntax error: IS expected", rule.getParsingError());

        tokens = rule.getTokens();
        assertEquals(Token.START, tokens.get(0));
        assertEquals(Token.IF, tokens.get(1));
        assertEquals(Token.IDENT, tokens.get(2));
        assertEquals(Token.IS, tokens.get(3));
        assertEquals(Token.IDENT, tokens.get(4));
        assertEquals(Token.OR, tokens.get(5));
        assertEquals(Token.IDENT, tokens.get(6));
        assertEquals(7, tokens.size());

        rule = new FuzzyRule("if x1 is a1 then ((y is 9b or z is c) and u is d)", symbolTable);
        parser.parse(rule);

        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Illegal name @25", rule.getParsingError());

        tokens = rule.getTokens();
        assertEquals(Token.START, tokens.get(0));
        assertEquals(Token.IF, tokens.get(1));
        assertEquals(Token.IDENT, tokens.get(2));
        assertEquals(Token.IS, tokens.get(3));
        assertEquals(Token.IDENT, tokens.get(4));
        assertEquals(Token.THEN, tokens.get(5));
        assertEquals(Token.LEFT_PAR, tokens.get(6));
        assertEquals(Token.LEFT_PAR, tokens.get(7));
        assertEquals(Token.IDENT, tokens.get(8));
        assertEquals(Token.IS, tokens.get(9));
        assertEquals(10, tokens.size());

        rule = new FuzzyRule("if (x1 is a1 or x2 is a2) u is d", symbolTable);
        parser.parse(rule);

        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Syntax error: AND, OR or THEN expected", rule.getParsingError());

        tokens = rule.getTokens();
        assertEquals(Token.START, tokens.get(0));
        assertEquals(Token.IF, tokens.get(1));
        assertEquals(Token.LEFT_PAR, tokens.get(2));
        assertEquals(Token.IDENT, tokens.get(3));
        assertEquals(Token.IS, tokens.get(4));
        assertEquals(Token.IDENT, tokens.get(5));
        assertEquals(Token.OR, tokens.get(6));
        assertEquals(Token.IDENT, tokens.get(7));
        assertEquals(Token.IS, tokens.get(8));
        assertEquals(Token.IDENT, tokens.get(9));
        assertEquals(Token.RIGHT_PAR, tokens.get(10));
        assertEquals(11, tokens.size());

        rule = new FuzzyRule("if (x1 is a1  x2 is a2) then u is d", symbolTable);
        parser.parse(rule);

        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Syntax error: AND or OR expected", rule.getParsingError());

        tokens = rule.getTokens();
        assertEquals(Token.START, tokens.get(0));
        assertEquals(Token.IF, tokens.get(1));
        assertEquals(Token.LEFT_PAR, tokens.get(2));
        assertEquals(Token.IDENT, tokens.get(3));
        assertEquals(Token.IS, tokens.get(4));
        assertEquals(Token.IDENT, tokens.get(5));
        assertEquals(6, tokens.size());

        rule = new FuzzyRule(" (x1 is a1 or x2 is a2) then u is d", symbolTable);
        parser.parse(rule);

        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Syntax error: IF expected", rule.getParsingError());

        tokens = rule.getTokens();
        assertEquals(Token.START, tokens.get(0));
        assertEquals(1, tokens.size());

        rule = new FuzzyRule("if x1 is a1  x2 is a2 then u is d", symbolTable);
        parser.parse(rule);

        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Syntax error: AND, OR or THEN expected", rule.getParsingError());

        tokens = rule.getTokens();
        assertEquals(Token.START, tokens.get(0));
        assertEquals(Token.IF, tokens.get(1));
        assertEquals(Token.IDENT, tokens.get(2));
        assertEquals(Token.IS, tokens.get(3));
        assertEquals(Token.IDENT, tokens.get(4));
        assertEquals(5, tokens.size());

        rule = new FuzzyRule("if x1 is a1 and x2 is a2  u is d", symbolTable);
        parser.parse(rule);

        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Syntax error: AND, OR or THEN expected", rule.getParsingError());

        tokens = rule.getTokens();
        assertEquals(Token.START, tokens.get(0));
        assertEquals(Token.IF, tokens.get(1));
        assertEquals(Token.IDENT, tokens.get(2));
        assertEquals(Token.IS, tokens.get(3));
        assertEquals(Token.IDENT, tokens.get(4));
        assertEquals(Token.AND, tokens.get(5));
        assertEquals(Token.IDENT, tokens.get(6));
        assertEquals(Token.IS, tokens.get(7));
        assertEquals(tokens.get(8), Token.IDENT);
        assertEquals(9, tokens.size());

    }

    @Test
    public final void testStack() {
        RuleParser parser = new RuleParser();

        // define rules and parse'em
        System.out.println("(x+y) => xy+");
        FuzzyRule rule = new FuzzyRule("if (x1 is a1 and x2 is a2) then y is b", symbolTable);
        parser.parse(rule);
        String premise = printPremise(rule).trim();
        assertEquals("x1 a1 IS x2 a2 IS AND", premise);
        System.out.print("Rule's premise   : " + premise);
        System.out.println(" ");
        String conclusion = printConclusion(rule);
        System.out.print("Rule's conclusion: " + conclusion);

        System.out.println(" ");
        System.out.println(" ");

        System.out.println("((x-y)+z) => xy-z+");
        rule = new FuzzyRule("if ((x1 is a1 or x2 is a2) and x3 is a3) then y is b", symbolTable);
        parser.parse(rule);
        premise = printPremise(rule).trim();
        assertEquals("x1 a1 IS x2 a2 IS OR x3 a3 IS AND", premise);
        System.out.print("Rule's premise   : " + premise);
        System.out.println(" ");
        conclusion = printConclusion(rule);
        System.out.print("Rule's conclusion: " + conclusion);

        System.out.println(" ");
        System.out.println(" ");

        System.out.println("(x-(y+z)) => xyz+-");
        rule = new FuzzyRule("if (x1 is a1 or (x2 is a2 and x3 is a3)) then y is b", symbolTable);
        parser.parse(rule);
        premise = printPremise(rule).trim();
        assertEquals("x1 a1 IS x2 a2 IS x3 a3 IS AND OR", premise);
        System.out.print("Rule's premise   : " + premise);
        System.out.println(" ");
        conclusion = printConclusion(rule);
        System.out.print("Rule's conclusion: " + conclusion);

        System.out.println(" ");
        System.out.println(" ");

        System.out.println("(x-(y+z)+u) => xyz+-u+");
        rule = new FuzzyRule("if (x1 is a1 or (x2 is a2 and x3 is a3) and x4 is a4) then y is b", symbolTable);
        parser.parse(rule);
        premise = printPremise(rule).trim();
        assertEquals("x1 a1 IS x2 a2 IS x3 a3 IS AND OR x4 a4 IS AND", premise);
        System.out.print("Rule's premise   : " + premise);
        System.out.println(" ");
        conclusion = printConclusion(rule);
        System.out.print("Rule's conclusion: " + conclusion);

        System.out.println(" ");
        System.out.println(" ");

        System.out.println("(x1 - ((x2 + x3 + x4) - x5 - (x6 + x7))) => ???");
        rule = new FuzzyRule(
                "if (x1 is a1 or ((x2 is a2 and x3 is a3 and x4 is a4) or x5 is a5 or (x6 is a6 and x7 is a7))) then y is b", symbolTable);
        parser.parse(rule);
        premise = printPremise(rule).trim();
        assertEquals("x1 a1 IS x2 a2 IS x3 a3 IS x4 a4 IS AND AND OR x5 a5 IS x6 a6 IS x7 a7 IS AND OR OR", premise);
        System.out.print("Rule's premise   : " + premise);
        System.out.println(" ");
        conclusion = printConclusion(rule);
        System.out.print("Rule's conclusion: " + conclusion);

        System.out.println(" ");
        System.out.println(" ");

    }

    @Test
    public final void testBracketsBug() {
        RuleParser parser = new RuleParser();

        // AND or OR are evaluated correct within brackets only
        System.out.println("x+y => xy+");

        FuzzyRule rule = new FuzzyRule("if x1 is a1 and x2 is a2 then y is b", symbolTable);
        parser.parse(rule);
        String premise = printPremise(rule).trim();
        assertNotEquals("x1 a1 IS x2 a2 IS AND", premise);

        System.out.print("Rule's premise   : " + premise);
        System.out.println(" ");

        String conclusion = printConclusion(rule);
        System.out.print("Rule's conclusion: " + conclusion);

    }

    @Test
    public final void testValidation() {
        RuleParser parser = new RuleParser(symbolTable);

        //  define linguistic variable 'brake force'
        LinguisticVariable brakeForce = new LinguisticVariable("brakeForce", symbolTable);

        // define linguistic values for variable 'brake force'
        brakeForce.addTerm("moderate", new MembershipFunction(40, 60, 60, 80));
        brakeForce.addTerm("strong", new MembershipFunction(70, 85, 85, 100));

        // define linguistic variable 'car speed'
        LinguisticVariable carSpeed = new LinguisticVariable("carSpeed", symbolTable);

        // define linguistic values for variable 'car speed'
        carSpeed.addTerm("low", new MembershipFunction(20, 60, 60, 100));
        carSpeed.addTerm("medium", new MembershipFunction(60, 100, 100, 140));

        // parse rule
        FuzzyRule rule = new FuzzyRule("if carSpeed is low then brakeForce is moderate", symbolTable);
        parser.parse(rule);
        assertSame(FuzzyRuleStatus.DONE, rule.getStatus());
        assertEquals("n/a", rule.getParsingError());

        rule = new FuzzyRule("if carSpeed is medium then brakeForce is strong", symbolTable);
        parser.parse(rule);
        assertSame(FuzzyRuleStatus.DONE, rule.getStatus());
        assertEquals("n/a", rule.getParsingError());

        rule = new FuzzyRule("if speed is medium then brakeForce is strong", symbolTable);
        parser.parse(rule);
        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Symbol 'speed' is not defined", rule.getParsingError());

        rule = new FuzzyRule("if carSpeed is fast then brakeForce is strong", symbolTable);
        parser.parse(rule);
        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Symbol 'fast' is not defined", rule.getParsingError());

        rule = new FuzzyRule("if carSpeed is moderate then brakeForce is medium", symbolTable);
        parser.parse(rule);
        assertSame(FuzzyRuleStatus.ERRONEOUS, rule.getStatus());
        assertEquals("Symbol 'moderate' is not defined", rule.getParsingError());

    }

}
