import java.util.Scanner;

class Parser {
    Scanner scanner;

    public Parser() {
        this.scanner = new Scanner(System.in);
    }

    public String parse() {
        String input = this.scanner.nextLine();
        return input;
    }

    public boolean containsKeyword(String input, String lookFor, String prefix) {
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
            throw new NumberFormatException();
        }
    }

    public String[] splitStringBySlash(String s) {
        return s.split("/");
    }

    public String removeKeywordFromString(String s, String toRemove) {
        int startIndex = toRemove.length();
        String stringNoKeyword = s.substring(startIndex);
        return stringNoKeyword;
    }

    public void closeParser() {
        if (this.scanner != null) {
            this.scanner.close();
        }
    }
}