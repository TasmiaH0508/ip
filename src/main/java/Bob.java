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
            if (input.length() == 0) {
                // what to do if there is no text?
                System.out.println("No text detected.");
            } else if (p.lookForKeyword(input, "mark", 2)) {
                int index;
                try {
                    index = p.getNumberFromString(input);
                    boolean isDone = !(p.lookForKeyword(input, "un", 0));
                    taskList.updateTaskCompletionStatus(index, isDone);
                } catch (NumberFormatException e) {
                    System.out.println("Too many spaces used.");
                }
            } else if (input.equals("bye")) {
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
