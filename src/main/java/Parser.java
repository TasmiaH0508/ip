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

    public boolean lookForKeyword(String input, String lookFor, int prefixLength) {
        int minLen = lookFor.length();
        if (input.length() < minLen) {
            return false;
        } else {
            boolean containsLookFor = input.substring(0, minLen).equals(lookFor);
            boolean containsPrefixAndLookFor = false;
            if (input.length() >= minLen + prefixLength) {
                containsPrefixAndLookFor = input.substring(prefixLength, minLen + prefixLength).equals(lookFor);
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

    public void closeParser() {
        if (this.scanner != null) {
            this.scanner.close();
        }
    }
}