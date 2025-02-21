package eve;

import eve.dukeexception.DukeException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class EveTest {

    @Test
    public void testIdentifyCommandFromInput() throws DukeException {
        // test 1 -> Even if the message format is incorrect, no errors should be thrown
        String message1 = "todo";
        String output1 = Eve.enumCommandTypeToString(Eve.identifyCommandFromInput(message1));
        assertEquals("todo", output1);

        // test 2
        String message2 = "todo fishing";
        String output2 = Eve.enumCommandTypeToString(Eve.identifyCommandFromInput(message2));
        assertEquals("todo", output2);

        // test 3
        String message3 = "unmark 3";
        String output3 = Eve.enumCommandTypeToString(Eve.identifyCommandFromInput(message3));
        assertEquals("mark", output3);

        // test 4
        String message4 = "todoo";
        String output4 = Eve.enumCommandTypeToString(Eve.identifyCommandFromInput(message4));
        assertEquals("todo", output4);
    }

    @Test
    public void identifyCommandFromTask_messageWithSpellingMistake_DukeExceptionThrown() {
        try {
            String message = "unmarrk 3";
            String output = Eve.enumCommandTypeToString(Eve.identifyCommandFromInput(message));
            fail();
        } catch (DukeException e) {
            assertEquals("Invalid command.", e.getMessage());
        }
    }
}