package ch.x01.fuzzy.parser;

import ch.x01.fuzzy.engine.FuzzyRule;
import ch.x01.fuzzy.engine.FuzzyRuleStatus;
import ch.x01.fuzzy.engine.LinguisticVariable;
import ch.x01.fuzzy.engine.MembershipFunction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

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
    public void setUp() throws Exception {
        symbolTable = new SymbolTable();
    }

    @Test
    public final void testParse() {
        // setup
        RuleParser parser = new RuleParser();
        FuzzyRule rule;
        ArrayList<Token> tokens;

        // define rules and parse'em
        rule = new FuzzyRule("if (x1 is a1 or (x2 is a2 and x3 is a3) or x4 is a4) then y is b", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.DONE);
        assertTrue(rule.getParsingError().equals("n/a"));
        tokens = rule.getTokens();
        assertTrue(Token.START.equals(tokens.get(0)));
        assertTrue(Token.IF.equals(tokens.get(1)));
        assertTrue(Token.LEFT_PAR.equals(tokens.get(2)));
        assertTrue(Token.IDENT.equals(tokens.get(3)));
        assertTrue(Token.IS.equals(tokens.get(4)));
        assertTrue(Token.IDENT.equals(tokens.get(5)));
        assertTrue(Token.OR.equals(tokens.get(6)));
        assertTrue(Token.LEFT_PAR.equals(tokens.get(7)));
        assertTrue(Token.IDENT.equals(tokens.get(8)));
        assertTrue(Token.IS.equals(tokens.get(9)));
        assertTrue(Token.IDENT.equals(tokens.get(10)));
        assertTrue(Token.AND.equals(tokens.get(11)));
        assertTrue(Token.IDENT.equals(tokens.get(12)));
        assertTrue(Token.IS.equals(tokens.get(13)));
        assertTrue(Token.IDENT.equals(tokens.get(14)));
        assertTrue(Token.RIGHT_PAR.equals(tokens.get(15)));
        assertTrue(Token.OR.equals(tokens.get(16)));
        assertTrue(Token.IDENT.equals(tokens.get(17)));
        assertTrue(Token.IS.equals(tokens.get(18)));
        assertTrue(Token.IDENT.equals(tokens.get(19)));
        assertTrue(Token.RIGHT_PAR.equals(tokens.get(20)));
        assertTrue(Token.THEN.equals(tokens.get(21)));
        assertTrue(Token.IDENT.equals(tokens.get(22)));
        assertTrue(Token.IS.equals(tokens.get(23)));
        assertTrue(Token.IDENT.equals(tokens.get(24)));
        assertTrue(Token.END.equals(tokens.get(25)));
        assertTrue(tokens.size() == 26);
        // System.out.println(rule.getTokens());
        // System.out.println();

        rule = new FuzzyRule("if x1 is or x2 is a2 then y is b", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Syntax error: IDENT expected"));
        tokens = rule.getTokens();
        assertTrue(Token.START.equals(tokens.get(0)));
        assertTrue(Token.IF.equals(tokens.get(1)));
        assertTrue(Token.IDENT.equals(tokens.get(2)));
        assertTrue(Token.IS.equals(tokens.get(3)));
        assertTrue(tokens.size() == 4);

        rule = new FuzzyRule("if  is a1 and x2 is a2 then y is b", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Syntax error: IDENT expected"));
        tokens = rule.getTokens();
        assertTrue(Token.START.equals(tokens.get(0)));
        assertTrue(Token.IF.equals(tokens.get(1)));
        assertTrue(tokens.size() == 2);

        rule = new FuzzyRule("if x1 is a1 or x2  a2 then y is b", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Syntax error: IS expected"));
        tokens = rule.getTokens();
        assertTrue(Token.START.equals(tokens.get(0)));
        assertTrue(Token.IF.equals(tokens.get(1)));
        assertTrue(Token.IDENT.equals(tokens.get(2)));
        assertTrue(Token.IS.equals(tokens.get(3)));
        assertTrue(Token.IDENT.equals(tokens.get(4)));
        assertTrue(Token.OR.equals(tokens.get(5)));
        assertTrue(Token.IDENT.equals(tokens.get(6)));
        assertTrue(tokens.size() == 7);

        rule = new FuzzyRule("if x1 is a1 then ((y is 9b or z is c) and u is d)", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Illegal name @25"));
        tokens = rule.getTokens();
        assertTrue(Token.START.equals(tokens.get(0)));
        assertTrue(Token.IF.equals(tokens.get(1)));
        assertTrue(Token.IDENT.equals(tokens.get(2)));
        assertTrue(Token.IS.equals(tokens.get(3)));
        assertTrue(Token.IDENT.equals(tokens.get(4)));
        assertTrue(Token.THEN.equals(tokens.get(5)));
        assertTrue(Token.LEFT_PAR.equals(tokens.get(6)));
        assertTrue(Token.LEFT_PAR.equals(tokens.get(7)));
        assertTrue(Token.IDENT.equals(tokens.get(8)));
        assertTrue(Token.IS.equals(tokens.get(9)));
        assertTrue(tokens.size() == 10);

        rule = new FuzzyRule("if (x1 is a1 or x2 is a2) u is d", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Syntax error: AND, OR or THEN expected"));
        tokens = rule.getTokens();
        assertTrue(Token.START.equals(tokens.get(0)));
        assertTrue(Token.IF.equals(tokens.get(1)));
        assertTrue(Token.LEFT_PAR.equals(tokens.get(2)));
        assertTrue(Token.IDENT.equals(tokens.get(3)));
        assertTrue(Token.IS.equals(tokens.get(4)));
        assertTrue(Token.IDENT.equals(tokens.get(5)));
        assertTrue(Token.OR.equals(tokens.get(6)));
        assertTrue(Token.IDENT.equals(tokens.get(7)));
        assertTrue(Token.IS.equals(tokens.get(8)));
        assertTrue(Token.IDENT.equals(tokens.get(9)));
        assertTrue(Token.RIGHT_PAR.equals(tokens.get(10)));
        assertTrue(tokens.size() == 11);

        rule = new FuzzyRule("if (x1 is a1  x2 is a2) then u is d", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Syntax error: AND or OR expected"));
        tokens = rule.getTokens();
        assertTrue(Token.START.equals(tokens.get(0)));
        assertTrue(Token.IF.equals(tokens.get(1)));
        assertTrue(Token.LEFT_PAR.equals(tokens.get(2)));
        assertTrue(Token.IDENT.equals(tokens.get(3)));
        assertTrue(Token.IS.equals(tokens.get(4)));
        assertTrue(Token.IDENT.equals(tokens.get(5)));
        assertTrue(tokens.size() == 6);

        rule = new FuzzyRule(" (x1 is a1 or x2 is a2) then u is d", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Syntax error: IF expected"));
        tokens = rule.getTokens();
        assertTrue(Token.START.equals(tokens.get(0)));
        assertTrue(tokens.size() == 1);

        rule = new FuzzyRule("if x1 is a1  x2 is a2 then u is d", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Syntax error: AND, OR or THEN expected"));
        tokens = rule.getTokens();
        assertTrue(Token.START.equals(tokens.get(0)));
        assertTrue(Token.IF.equals(tokens.get(1)));
        assertTrue(Token.IDENT.equals(tokens.get(2)));
        assertTrue(Token.IS.equals(tokens.get(3)));
        assertTrue(Token.IDENT.equals(tokens.get(4)));
        assertTrue(tokens.size() == 5);

        rule = new FuzzyRule("if x1 is a1 and x2 is a2  u is d", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Syntax error: AND, OR or THEN expected"));
        tokens = rule.getTokens();
        assertTrue(Token.START.equals(tokens.get(0)));
        assertTrue(Token.IF.equals(tokens.get(1)));
        assertTrue(Token.IDENT.equals(tokens.get(2)));
        assertTrue(Token.IS.equals(tokens.get(3)));
        assertTrue(Token.IDENT.equals(tokens.get(4)));
        assertTrue(Token.AND.equals(tokens.get(5)));
        assertTrue(Token.IDENT.equals(tokens.get(6)));
        assertTrue(Token.IS.equals(tokens.get(7)));
        assertTrue(Token.IDENT.equals(tokens.get(8)));
        assertTrue(tokens.size() == 9);

    }

    @Test
    public final void testStack() {
        // setup
        RuleParser parser = new RuleParser();
        FuzzyRule rule;
        String premise;
        String conclusion;

        // define rules and parse'em
        System.out.println("(x+y) => xy+");
        rule = new FuzzyRule("if (x1 is a1 and x2 is a2) then y is b", symbolTable);
        parser.parse(rule);
        premise = printPremise(rule).trim();
        assertTrue(premise.equals("x1 a1 IS x2 a2 IS AND"));
        System.out.print("Rule's premise   : " + premise);
        System.out.println(" ");
        conclusion = printConclusion(rule);
        System.out.print("Rule's conclusion: " + conclusion);

        System.out.println(" ");
        System.out.println(" ");

        System.out.println("((x-y)+z) => xy-z+");
        rule = new FuzzyRule("if ((x1 is a1 or x2 is a2) and x3 is a3) then y is b", symbolTable);
        parser.parse(rule);
        premise = printPremise(rule).trim();
        assertTrue(premise.equals("x1 a1 IS x2 a2 IS OR x3 a3 IS AND"));
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
        assertTrue(premise.equals("x1 a1 IS x2 a2 IS x3 a3 IS AND OR"));
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
        assertTrue(premise.equals("x1 a1 IS x2 a2 IS x3 a3 IS AND OR x4 a4 IS AND"));
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
        assertTrue(premise.equals("x1 a1 IS x2 a2 IS x3 a3 IS x4 a4 IS AND AND OR x5 a5 IS x6 a6 IS x7 a7 IS AND OR OR"));
        System.out.print("Rule's premise   : " + premise);
        System.out.println(" ");
        conclusion = printConclusion(rule);
        System.out.print("Rule's conclusion: " + conclusion);

        System.out.println(" ");
        System.out.println(" ");

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
        assertTrue(rule.getStatus() == FuzzyRuleStatus.DONE);
        assertTrue(rule.getParsingError().equals("n/a"));

        rule = new FuzzyRule("if carSpeed is medium then brakeForce is strong", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.DONE);
        assertTrue(rule.getParsingError().equals("n/a"));

        rule = new FuzzyRule("if speed is medium then brakeForce is strong", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Symbol 'speed' is not defined"));

        rule = new FuzzyRule("if carSpeed is fast then brakeForce is strong", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Symbol 'fast' is not defined"));

        rule = new FuzzyRule("if carSpeed is moderate then brakeForce is medium", symbolTable);
        parser.parse(rule);
        assertTrue(rule.getStatus() == FuzzyRuleStatus.ERRONEOUS);
        assertTrue(rule.getParsingError().equals("Symbol 'moderate' is not defined"));
        //        System.out.println(rule.getTokens());
        //        System.out.println("Status: " + rule.getStatus());
        //        System.out.println("Error: " + rule.getParsingError());

    }

}
