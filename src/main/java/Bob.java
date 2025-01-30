public class Bob {

    private static Parser p = new Parser();
    private static boolean isEndConversation = false;
    private static final TaskList taskList = new TaskList();
    private static final String[] commands = {"mark", "todo", "deadline", "event" , "bye", "list", "delete"};
    private static final String[] prefixForCommands = {"un", "", "", "", "", "", ""};
    private enum CommandType {MARK, TODO, DEADLINE, EVENT, BYE, LIST, DELETE};

    public static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public static CommandType identifyCommandFromInput(String text) throws DukeException {
        for (int i = 0; i < commands.length; i++) {
            if (p.containsKeyword(text, commands[i], prefixForCommands[i])) {
                return CommandType.values()[i];
            }
        }
        throw new DukeException("Invalid command.");
    }

    public static void handleMarkCommand(String input) {
        try {
            int index = p.getNumberFromString(input);
            boolean isDone = !(p.containsKeyword(input, "un", ""));
            taskList.updateTaskCompletionStatus(index, isDone);
        } catch (NumberFormatException e) {
            System.out.println("Too many spaces used.");
        }
    }

    public static void handleTodoCommand(String input) {
        String taskDescription = p.removeKeywordFromString(input, "todo");
        if (taskDescription.length() != 0) {
            Task t = new Todo(taskDescription);
            taskList.addTask(t);
        } else {
            System.out.println("I think you're missing the task description...");
        }
    }

    public static void handleDeadlineCommand(String input) {
        String taskDescription = p.removeKeywordFromString(input, "deadline");
        String[] taskDescriptionSegments = p.splitStringBySlash(taskDescription);
        if (taskDescriptionSegments.length != 2) {
            System.out.println("I need more information. Are you missing the task description or deadline?");
        } else {
            String deadline = p.removeKeywordFromString(taskDescriptionSegments[1], "by ");
            Task t = new Deadline(taskDescriptionSegments[0], deadline);
            taskList.addTask(t);
        }
    }
    
    public static void handleEventCommand(String input) {
        String taskDescription = p.removeKeywordFromString(input, "event");
        String[] taskDescriptionSegments = p.splitStringBySlash(taskDescription);
        if (taskDescriptionSegments.length != 3) {
            System.out.println("I need more information. Are you missing the task description, " +
                    "start time or end time?");
        } else {
            String startTime = p.removeKeywordFromString(taskDescriptionSegments[1], "from");
            String endTime = p.removeKeywordFromString(taskDescriptionSegments[2], "to");
            Task t = new Event(taskDescriptionSegments[0], startTime, endTime);
            taskList.addTask(t);
        }
    }

    public static void handleListCommand() {
        taskList.displayTasks();
    }

    public static void handleDeleteCommand(String input) {
        try {
            int index = p.getNumberFromString(input);
            taskList.deleteTask(index);
        } catch (NumberFormatException e) {
            System.out.println("Too many spaces used.");
        }
    }

    public static void chat() {
        while (!isEndConversation) {
            String input = p.parse();
            try {
                CommandType commandType = identifyCommandFromInput(input);
                if (commandType == CommandType.MARK) {
                    handleMarkCommand(input);
                } else if (commandType == CommandType.TODO) {
                    handleTodoCommand(input);
                } else if (commandType == CommandType.DEADLINE) {
                    handleDeadlineCommand(input);
                } else if (commandType == CommandType.EVENT) {
                    handleEventCommand(input);
                } else if (commandType == CommandType.LIST) {
                    handleListCommand();
                } else if (commandType == CommandType.DELETE) {
                    handleDeleteCommand(input);
                } else if (commandType == CommandType.BYE) {
                    isEndConversation = true;
                    p.closeParser();
                    exit();
                }
            } catch (DukeException e) {
                System.out.println("I really want to help you but I do not know how I can do so." +
                                   "Try another command or give me more info.");
            }
        }
    }

    public static void greet() {
        String name = "Bob";
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
    }

    public static void retrieveSavedTaskData() {
        taskList.loadSavedTasks(p);
    }

    public static void saveTaskData() {
        taskList.writeTaskDataToFile();
    }

    public static void main(String[] args) {
        retrieveSavedTaskData();
        greet();
        chat();
        saveTaskData();
    }
}
