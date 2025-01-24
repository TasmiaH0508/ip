public class InvalidCommandException extends Exception {
    InvalidCommandException() {
        super("No such command found.");
    }
}