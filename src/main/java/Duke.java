//package application;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Duke {

    private Scanner s;

    public static void main(String[] args) {
        System.out.println("Hello!");
    }

    /**
     * Generates a response for the user's chat message.
     */
    public static String getResponse(String input) {
        // messages are returned as strings

        // Create a stream to hold the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        System.out.println("Foofoofoo!");

        System.out.flush();
        System.setOut(old);

        System.out.println("Take note of the line below:");
        System.out.println("Here: " + baos.toString());
        return "Duke heard: " + input;
    }
}
