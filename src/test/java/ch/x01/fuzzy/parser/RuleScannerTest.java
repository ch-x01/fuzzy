package ch.x01.fuzzy.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
            assertEquals(Token.START, scanner.nextToken());
            assertEquals(Token.IF, scanner.nextToken());
            assertEquals(Token.IDENT, scanner.nextToken());
            assertEquals(Token.IS, scanner.nextToken());
            assertEquals(Token.IDENT, scanner.nextToken());
            assertEquals(Token.THEN, scanner.nextToken());
            assertEquals(Token.IDENT, scanner.nextToken());
            assertEquals(Token.IS, scanner.nextToken());
            assertEquals(Token.IDENT, scanner.nextToken());
            assertEquals(Token.END, scanner.nextToken());
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
