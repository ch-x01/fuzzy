package ch.x01.fuzzy.parser;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RuleScannerTest {

    @Test
    public void testScanner() {
        RuleScanner scanner = new RuleScanner("if (carSpeed is low) then (brakeForce is moderate)");
        Token token;
        while (scanner.hasMoreTokens()) {
            try {
                token = scanner.nextToken();
                System.out.println(token);
            } catch (IllegalNameException e) {
                e.printStackTrace();
                break;
            }
        }

    }

    @Test
    public void testNextToken() {
        RuleScanner scanner = new RuleScanner("if carSpeed is low then brakeForce is moderate");
        try {
            assertTrue(Token.START.equals(scanner.nextToken()));
            assertTrue(Token.IF.equals(scanner.nextToken()));
            assertTrue(Token.IDENT.equals(scanner.nextToken()));
            assertTrue(Token.IS.equals(scanner.nextToken()));
            assertTrue(Token.IDENT.equals(scanner.nextToken()));
            assertTrue(Token.THEN.equals(scanner.nextToken()));
            assertTrue(Token.IDENT.equals(scanner.nextToken()));
            assertTrue(Token.IS.equals(scanner.nextToken()));
            assertTrue(Token.IDENT.equals(scanner.nextToken()));
            assertTrue(Token.END.equals(scanner.nextToken()));
        } catch (IllegalNameException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testTestForKeyword() {
    /*  commented this test because method RuleScanner.testForKeyword is private
     
        RuleScanner scanner = new RuleScanner("");
       
        assertTrue(Token.IF.equals(scanner.testForKeyword("if")));
        assertTrue(Token.IS.equals(scanner.testForKeyword("is")));
        assertTrue(Token.THEN.equals(scanner.testForKeyword("then")));
        assertTrue(Token.AND.equals(scanner.testForKeyword("and")));
        assertTrue(Token.OR.equals(scanner.testForKeyword("or")));

        assertFalse(Token.AND.equals(scanner.testForKeyword("or")));
        assertFalse(Token.OR.equals(scanner.testForKeyword("and")));

        assertFalse(Token.LEFT_PAR.equals(scanner.testForKeyword("(")));
        assertFalse(Token.RIGHT_PAR.equals(scanner.testForKeyword(")")));
        assertFalse(Token.IDENT.equals(scanner.testForKeyword("literal")));
        assertFalse(Token.END.equals(scanner.testForKeyword("end")));
     */
    }

}
