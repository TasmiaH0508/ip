package bob.parser;

import bob.dukeException.DukeException;

import java.util.Scanner;


/**
 * Represents a parser, which is an object that can read from the console and perform operations on strings.
 */
public class Parser {
    private Scanner scanner;

    public Parser() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Returns user's input from the console as a string.
     *
     * @return user input as a string.
     */
    public String parse() {
        String input = this.scanner.nextLine();
        return input;
    }

    /**
     * Looks for the existences of a keyword or a keyword prefixed by a given prefix within a word.
     *
     * @param input Input word to check.
     * @param lookFor LookFor word to search for.
     * @param prefix Prefix a string that precedes LookFor.
     * @return true if the keyword or the prefixed keyword exists in word and false otherwise.
     */
    public boolean prefixedByKeyword(String input, String lookFor, String prefix) {
        int minLen = lookFor.length();
        if (input.length() < minLen) {
            return false;
        } else {
            boolean containsLookFor = input.startsWith(lookFor);
            boolean containsPrefixAndLookFor = input.startsWith(prefix + lookFor);
            return containsLookFor || containsPrefixAndLookFor;
        }
    }

    /**
     * Returns number existing in the second substring, where the first and second substring where originally separated
     * with a spacing.
     *
     * @param s S string consisting 2 substrings separated by a space character.
     * @return number in second substring.
     * @throws NumberFormatException if excess spacing is used.
     */
    public int getNumberFromString(String s) throws NumberFormatException {
        String[] parts = s.split(" ");
        if (parts.length == 2) {
            return Integer.parseInt(parts[1]);
        } else {
            throw new NumberFormatException("Excess spacing was used.");
        }
    }

    /**
     * Returns an array of substrings originally separated within the same string by a slash.
     *
     * @return array of substrings.
     */
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

    /**
     * Removes a substring from the string, assuming the substring already exists and is located at the front of the string.
     *
     * @param s S string from which substring will be removed.
     * @param toRemove ToRemove substring to remove.
     * @return string with substring removed.
     */
    public String removeKeywordFromString(String s, String toRemove) {
        // this method call is always preceded by the method call to prefixedByKeyword
        int startIndex = toRemove.length();
        String stringNoKeyword = s.substring(startIndex);
        return stringNoKeyword;
    }

    /**
     * Closes the scanner that reads from the console.
     */
    public void closeParser() {
        if (this.scanner != null) {
            this.scanner.close();
        }
    }
}