package bob;

import bob.dukeException.DukeException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BobTest {

    @Test
    public void testIdentifyCommandFromInput() throws DukeException {
        // test 1 -> Even if the message format is incorrect, no errors should be thrown
        String message1 = "todo";
        String output1 = Bob.getStringFromEnumCommandType(Bob.identifyCommandFromInput(message1));
        assertEquals("todo", output1);

        // test 2
        String message2 = "todo fishing";
        String output2 = Bob.getStringFromEnumCommandType(Bob.identifyCommandFromInput(message2));
        assertEquals("todo", output2);

        // test 3
        String message3 = "unmark 3";
        String output3 = Bob.getStringFromEnumCommandType(Bob.identifyCommandFromInput(message3));
        assertEquals("mark", output3);

        // test 4
        String message4 = "todoo";
        String output4 = Bob.getStringFromEnumCommandType(Bob.identifyCommandFromInput(message4));
        assertEquals("todo", output4);
    }

    @Test
    public void identifyCommandFromTask_messageWithSpellingMistake_DukeExceptionThrown() {
        try {
            String message = "unmarrk 3";
            String output = Bob.getStringFromEnumCommandType(Bob.identifyCommandFromInput(message));
            fail();
        } catch (DukeException e) {
            assertEquals("Invalid command.", e.getMessage());
        }
    }
}