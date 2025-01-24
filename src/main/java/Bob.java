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
                try {
                    int index = p.getNumberFromString(input); // throws error
                    boolean isDone = !(p.lookForKeyword(input, "un", 0));
                    taskList.updateTaskCompletionStatus(index, isDone);
                } catch (NumberFormatException e) {
                    System.out.println("Too many spaces used.");
                }
            } else if (p.lookForKeyword(input, "todo", 0)) {
                String taskDescription = p.removeKeywordFromString(input, "todo ");
                Task t = new Todo(taskDescription);
                taskList.addTask(t);
            } else if (p.lookForKeyword(input, "deadline", 0)) {
                String taskDescription = p.removeKeywordFromString(input, "deadline ");
                String[] taskDescriptionSegments = p.splitStringBySlash(taskDescription);
                String deadline = p.removeKeywordFromString(taskDescriptionSegments[1], "by ");
                Task t = new Deadline(taskDescriptionSegments[0], deadline);
                taskList.addTask(t);
            } else if (p.lookForKeyword(input, "event", 0)) {
                String taskDescription = p.removeKeywordFromString(input, "event ");
                String[] taskDescriptionSegments = p.splitStringBySlash(taskDescription);
                String startTime = p.removeKeywordFromString(taskDescriptionSegments[1], "from ");
                String endTime = p.removeKeywordFromString(taskDescriptionSegments[2], "to ");
                Task t = new Event(taskDescriptionSegments[0], startTime, endTime);
                taskList.addTask(t);
            } else if (input.equals("bye")) {
                isEndConversation = true;
                p.closeParser();
                exit();
            } else if (input.equals("list")) {
                taskList.displayTasks();
            } else {
                // what if none of the keywords are found?
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
