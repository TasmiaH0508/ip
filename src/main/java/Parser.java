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

    public void closeParser() {
        if (this.scanner != null) {
            this.scanner.close();
        }
    }
}