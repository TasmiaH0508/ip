public class Bob {

    private static Parser p;
    private static boolean isEndConversation = false;
    private static TaskList taskList = new TaskList();

    public static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public static void chat() {
        p = new Parser();
        while (!isEndConversation) {
            String input = p.parse();
            if (input.equals("bye")) {
                isEndConversation = true;
                p.closeParser();
                exit();
            } else if (input.equals("list")) {
                taskList.displayTasks();
            } else {
                taskList.addTask(input);
                System.out.println("added: " + input);
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
        chat();
    }
}
