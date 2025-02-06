package bob;

import bob.dukeException.DukeException;

import bob.parser.Parser;

import bob.storage.Storage;

import bob.tasklist.TaskList;

import bob.tasks.Deadline;
import bob.tasks.Event;
import bob.tasks.Task;
import bob.tasks.Todo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the chatbot named Bob.
 */
public class Bob {

    private static Parser parser = new Parser();
    private static boolean isEndConversation = false;
    private static final TaskList TASK_LIST = new TaskList();
    private static final String[] COMMANDS = {"mark", "todo", "deadline", "event" , "bye", "list", "delete", ""};
    private static final String[] PREFIX_FOR_COMMANDS = {"un", "", "", "", "", "", "", ""};
    private enum CommandType {MARK, TODO, DEADLINE, EVENT, BYE, LIST, DELETE, SEARCH};
    private static final Storage STORAGE = new Storage();

    /**
     * Exits from program.
     */
    public static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Returns a string corresponding to CommandType.
     *
     * @param t T command type.
     * @return string depending on input t
     */
    public static String enumCommandTypeToString(CommandType t) {
        if (t == CommandType.MARK) {
            return "mark";
        } else if (t == CommandType.TODO) {
            return "todo";
        } else if (t == CommandType.DEADLINE) {
            return "deadline";
        } else if (t == CommandType.EVENT) {
            return "event";
        } else if (t == CommandType.BYE) {
            return "bye";
        } else if (t == CommandType.LIST) {
            return "list";
        } else if (t == CommandType.DELETE) {
            return "delete";
        } else {
            return "search";
        }
    }

    /**
     * Returns the identified command type from text.
     *
     * @param text Text user input.
     * @return command type depending on input text
     */
    public static CommandType identifyCommandFromInput(String text) throws DukeException {
        for (int i = 0; i < COMMANDS.length; i++) {
            if (parser.prefixedByKeyword(text, COMMANDS[i], PREFIX_FOR_COMMANDS[i])) {
                return CommandType.values()[i];
            }
        }
        throw new DukeException("Invalid command.");
    }

    /**
     * Marks task as done.
     *
     * @param input Input user input which contains the substring "mark" or "unmark" and is expected to end with a number.
     */
    public static void handleMarkCommand(String input) {
        try {
            int index = parser.getNumberFromString(input);
            boolean isDone = !(parser.prefixedByKeyword(input, "un", ""));
            TASK_LIST.updateTaskCompletionStatus(index, isDone);
        } catch (NumberFormatException e) {
            System.out.println("Too many spaces used.");
        }
    }

    /**
     * Creates a todo task, which is then added to tasklist.
     *
     * @param input Input user input which contains the substring "todo".
     */
    public static void handleTodoCommand(String input) {
        String taskDescription = parser.removeKeywordFromString(input, "todo");
        if (taskDescription.length() != 0) {
            Task t = new Todo(taskDescription);
            TASK_LIST.addTask(t,parser);
        } else {
            System.out.println("I think you're missing the task description...");
        }
    }

    /**
     * Creates a deadline task, which is then added to tasklist.
     *
     * @param input Input user input which contains the substring "deadline".
     */
    public static void handleDeadlineCommand(String input) {
        try {
            String taskDescription = parser.removeKeywordFromString(input, "deadline");
            String[] taskDescriptionSegments = parser.splitStringBySlash(taskDescription);

            String dateString = parser.removeKeywordFromString(taskDescriptionSegments[1], "by ");
            String monthString = taskDescriptionSegments[2];
            String yearString = taskDescriptionSegments[3];
            String deadlineString = yearString + "-" + monthString + "-" + dateString;

            Task t = new Deadline(taskDescriptionSegments[0], deadlineString);
            TASK_LIST.addTask(t, parser);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("You may have followed an incorrect format. " +
                    "Try this format: deadline <task> /by <dd/mm/yyyy>");
        }
    }

    /**
     * Creates an event task, which is then added to the tasklist.
     *
     * @param input Input user input which contains the substring "event".
     */
    public static void handleEventCommand(String input) {
        try {
            String taskDescription = parser.removeKeywordFromString(input, "event");
            String[] taskDescriptionSegments = parser.splitStringBySlash(taskDescription);

            String startDateString = parser.removeKeywordFromString(taskDescriptionSegments[1], "from ");
            String startMonthString = taskDescriptionSegments[2];
            String startYearString = taskDescriptionSegments[3].substring(0, 4);
            String startTimeString = startYearString + "-" + startMonthString + "-" + startDateString;

            String endDateString = parser.removeKeywordFromString(taskDescriptionSegments[4], "to ");
            String endMonthString = taskDescriptionSegments[5];
            String endYearString = taskDescriptionSegments[6];
            String endTimeString = endYearString + "-" + endMonthString + "-" + endDateString;

            Task t = new Event(taskDescriptionSegments[0], startTimeString, endTimeString);
            TASK_LIST.addTask(t, parser);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("You may have followed an incorrect format. Try this format: event <task> / from " +
                    "<dd/mm/yyyy> /to <dd/mm/yyyy>");
        }
    }

    /**
     * Displays the tasks present in the tasklist.
     */
    public static void handleListCommand() {
        TASK_LIST.displayTasks();
    }

    /**
     * Removes tasks from tasklist.
     *
     * @param input Input user input which contains "delete" substring followed by the index of the task to remove.
     */
    public static void handleDeleteCommand(String input) {
        try {
            int index = parser.getNumberFromString(input);
            TASK_LIST.deleteTask(index, parser);
        } catch (NumberFormatException e) {
            System.out.println("Too many spaces used.");
        }
    }

    /**
     * Performs search based on user input.
     *
     * @param input Input user input.
     */
    public static void handleSearchCommand(String input) {
        String[] inputParts = parser.splitStringBySpacing(input);
        Set<Task> alreadyAdded = new HashSet<>();
        List<Task> res = new ArrayList<>();
        for (String inputPart : inputParts) {
            List<Task> searchResults = TASK_LIST.getSearchResults(inputPart);
            if (searchResults != null) {
                for (Task searchResult : searchResults) {
                    if (!alreadyAdded.contains(searchResult)) {
                        alreadyAdded.add(searchResult);
                        res.add(searchResult);
                    }
                }
            }
        }
        if (res.size() != 0) {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < res.size(); i++) {
                Task t = res.get(i);
                String taskDescription = t.getTaskDescription();
                System.out.println((i + 1) + "." + taskDescription);
            }
        } else {
            System.out.println("None of the tasks match the description.");
        }
    }


     /**
     * Performs tasks based on user input.
     */
    public static void chat() {
        while (!isEndConversation) {
            String input = parser.parse();
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
                    parser.closeParser();
                    exit();
                } else {
                    handleSearchCommand(input);
                }
            } catch (DukeException e) {
                System.out.println("I really want to help you but I do not know how I can do so." +
                        "Try another command or give me more info.");
            }
        }
    }

    /**
     * Greets the user.
     */
    public static void greet() {
        String name = "Bob";
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
    }

    /**
     * Loads saved tasks from hard disc, if present.
     */
    public static void retrieveSavedTaskData() {
        STORAGE.loadSavedTasks(parser, TASK_LIST);
    }

    /**
     * Saves tasks in task list to hard disc.
     */
    public static void saveTaskData() {
        STORAGE.writeTaskDataToFile(TASK_LIST);
    }

    public static void main(String[] args) {
        retrieveSavedTaskData();
        greet();
        chat();
        saveTaskData();
    }
}
