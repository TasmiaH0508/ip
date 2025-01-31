package bob.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {

    @Test
    public void testContainsKeyword() {
        // testContainsKeyword relies purely on Java String methods
        Parser p = new Parser();

        // test 1
        String input1 = "mark 7";
        String lookFor1 = "mark";
        String prefix1 = "";
        assertEquals(true, p.containsKeyword(input1, lookFor1, prefix1));

        // test 2 returns true since the function is supposed to look for the existences of "mark" and "unmark"
        String input2 = "mark 7";
        String lookFor2 = "mark";
        String prefix2 = "un";
        assertEquals(true, p.containsKeyword(input2, lookFor2, prefix2));

        // test 3
        String input3 = "mark 7";
        String lookFor3 = "unmark";
        String prefix3 = "";
        assertEquals(false, p.containsKeyword(input3, lookFor3, prefix3));

        // test 4
        String input4 = "mark";
        String lookFor4 = "john";
        String prefix4 = "";
        assertEquals(false, p.containsKeyword(input4, lookFor4, prefix4));

        p.closeParser();
    }

    @Test
    public void testGetNumberFromString() {
        Parser p = new Parser();

        // test 1
        String input1 = "mark 3";
        assertEquals(3, p.getNumberFromString(input1));

        // test 2
        String input2 = "3 4";
        assertEquals(4, p.getNumberFromString(input2));

        p.closeParser();
    }

    @Test
    public void getNumberFromString_excessSpacingUsed_NumberFormatExceptionThrown() {
        Parser p = new Parser();

        try {
            String input = "2  4";
            p.getNumberFromString(input);
            fail();
        } catch (NumberFormatException e) {
            assertEquals("Excess spacing was used.", e.getMessage());
        }

        p.closeParser();
    }
}