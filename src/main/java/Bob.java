public class Bob {

    private static Parser p;
    private static boolean isEndConversation = false;

    public static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public static void echo() {
        p = new Parser();
        while (!isEndConversation) {
            String input = p.parse();
            if (input.equals("bye")) {
                isEndConversation = true;
                p.closeParser();
                exit();
            } else {
                System.out.println(input);
            }
        }
    }

    public static void greet() {
        String name = "Bob";
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
    }

    public static void main(String[] args) {
        greet();
        echo();
    }
}
