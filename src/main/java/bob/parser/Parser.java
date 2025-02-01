package bob.parser;

import java.util.Scanner;

public class Parser {
    private Scanner scanner;

    public Parser() {
        this.scanner = new Scanner(System.in);
    }

    public String parse() {
        String input = this.scanner.nextLine();
        return input;
    }

    public boolean prefixedByKeyword(String input, String lookFor, String prefix) {
        int minLen = lookFor.length();
        int prefixLength = prefix.length();
        if (input.length() < minLen) {
            return false;
        } else {

            boolean containsLookFor = input.substring(0, minLen).equals(lookFor);
            boolean containsPrefixAndLookFor = false;

            if (input.length() >= minLen + prefixLength) {
                lookFor = prefix + lookFor;
                containsPrefixAndLookFor = input.substring(0, minLen + prefixLength).equals(lookFor);
            }

            return containsLookFor || containsPrefixAndLookFor;
        }
    }

    public int getNumberFromString(String s) throws NumberFormatException {
        String[] parts = s.split(" ");
        if (parts.length == 2) {
            return Integer.parseInt(parts[1]);
        } else {
            throw new NumberFormatException("Excess spacing was used.");
        }
    }

    public String[] splitStringBySlash(String s) {
        return s.split("/");
    }

    /**
     * Returns an array of substrings originally separated by spaces.
     *
     * @param s S string containing substrings separated by spaces.
     * @return array of substrings
     */
    public String[] splitStringBySpacing(String s) {
        return s.split(" ");
    }

    public String removeKeywordFromString(String s, String toRemove) {
        // this method call is preceded by the method call to containsKeyword
        int startIndex = toRemove.length();
        String stringNoKeyword = s.substring(startIndex);
        return stringNoKeyword;
    }

    /**
     * Returns true if a keyword exists and false otherwise.
     */
    public boolean containsKeyword(String input, String[] parts) {
        String[] inputParts = splitStringBySpacing(input);
        for (String inputPart : inputParts) {
            for (String part : parts) {
                if (part.equals(inputPart)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void closeParser() {
        if (this.scanner != null) {
            this.scanner.close();
        }
    }
}