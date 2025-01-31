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
        try {
            String taskDescription = p.removeKeywordFromString(input, "deadline");
            String[] taskDescriptionSegments = p.splitStringBySlash(taskDescription);
            String dateString = p.removeKeywordFromString(taskDescriptionSegments[1], "by ");
            String monthString = taskDescriptionSegments[2];
            String yearString = taskDescriptionSegments[3];
            String deadlineString = yearString + "-" + monthString + "-" + dateString;
            Task t = new Deadline(taskDescriptionSegments[0], deadlineString);
            taskList.addTask(t);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("You may have followed an incorrect format. Try this format: deadline <task> /by <dd/mm/yyyy>");
        }
    }
    
    public static void handleEventCommand(String input) {
        try {
            String taskDescription = p.removeKeywordFromString(input, "event");
            String[] taskDescriptionSegments = p.splitStringBySlash(taskDescription);
            String startDateString = p.removeKeywordFromString(taskDescriptionSegments[1], "from ");
            String startMonthString = taskDescriptionSegments[2];
            String startYearString = taskDescriptionSegments[3].substring(0, 4);
            String startTimeString = startYearString + "-" + startMonthString + "-" + startDateString;
            String endDateString = p.removeKeywordFromString(taskDescriptionSegments[4], "to ");
            String endMonthString = taskDescriptionSegments[5];
            String endYearString = taskDescriptionSegments[6];
            String endTimeString = endYearString + "-" + endMonthString + "-" + endDateString;
            Task t = new Event(taskDescriptionSegments[0], startTimeString, endTimeString);
            taskList.addTask(t);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("You may have followed an incorrect format. Try this format: event <task> / from " +
                    "<dd/mm/yyyy> /to <dd/mm/yyyy>");
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
