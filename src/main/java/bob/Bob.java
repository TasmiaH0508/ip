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
     * Exits from program, saves task data and returns an exit message.
     *
     * @return exit message as a string.
     */
    public static String handleExitCommand() {
        parser.closeParser();
        isEndConversation = true;
        String exitMessage = "Bye. Hope to see you again soon!\n";
        boolean isDataSaved = STORAGE.writeTaskDataToFile(TASK_LIST);
        if (isDataSaved) {
            exitMessage += "Task Data saved successfully.";
        } else {
            exitMessage += "Your tasks could not be saved. Sorry for the inconvenience.";
        }
        return exitMessage;
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
     * Marks task as done and returns a message indicating if the task was successfully marked.
     *
     * @param input Input user input which contains the substring "mark" or "unmark" and is expected to end with a number.
     * @return message as a string.
     */
    public static String handleMarkCommand(String input) {
        try {
            int index = parser.getNumberFromString(input);
            boolean isDone = !(parser.prefixedByKeyword(input, "un", ""));
            String message = TASK_LIST.updateTaskCompletionStatus(index, isDone);
            return message;
        } catch (NumberFormatException e) {
            return "Too many spaces used.";
        }
    }

    /**
     * Creates a todo task, which is then added to tasklist, and returns a message indicating if the task has been added
     * successfully.
     *
     * @param input Input user input which contains the substring "todo".
     * @return message as a string.
     */
    public static String handleTodoCommand(String input) {
        String taskDescription = parser.removeKeywordFromString(input, "todo");
        String message;
        if (taskDescription.length() != 0) {
            Task t = new Todo(taskDescription);
            message = TASK_LIST.addTask(t, parser);
        } else {
            message = "I think you're missing the task description...";
        }
        return message;
    }

    /**
     * Creates a deadline task, which is then added to tasklist and returns a message indicating if the task has been
     * added successfully.
     *
     * @param input Input user input which contains the substring "deadline".
     * @return message as a string.
     */
    public static String handleDeadlineCommand(String input) {
        try {
            String taskDescription = parser.removeKeywordFromString(input, "deadline");
            String[] taskDescriptionSegments = parser.splitStringBySlash(taskDescription);

            String dateString = parser.removeKeywordFromString(taskDescriptionSegments[1], "by ");
            String monthString = taskDescriptionSegments[2];
            String yearString = taskDescriptionSegments[3];
            String deadlineString = yearString + "-" + monthString + "-" + dateString;

            Task t = new Deadline(taskDescriptionSegments[0], deadlineString);
            String message = TASK_LIST.addTask(t, parser);
            return message;
        } catch (IndexOutOfBoundsException e) {
            String message = "You may have followed an incorrect format. " +
                    "Try this format: deadline <task> /by <dd/mm/yyyy>";
            return message;
        }
    }

    /**
     * Creates an event task, which is then added to the tasklist, and returns a message indicating if the task has been
     * added successfully.
     *
     * @param input Input user input which contains the substring "event".
     */
    public static String handleEventCommand(String input) {
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
            String message = TASK_LIST.addTask(t, parser);
            return message;
        } catch (IndexOutOfBoundsException e) {
            String message = "You may have followed an incorrect format. Try this format: event <task> / from " +
                    "<dd/mm/yyyy> /to <dd/mm/yyyy>";
            return message;
        }
    }

    /**
     * Displays the tasks present in the tasklist and returns a string containing all the tasks.
     *
     * @return string containing all the tasks.
     */
    public static String handleListCommand() {
        String message = TASK_LIST.displayTasks();
        return message;
    }

    /**
     * Removes tasks from tasklist.
     *
     * @param input Input user input which contains "delete" substring followed by the index of the task to remove.
     */
    public static String handleDeleteCommand(String input) {
        try {
            int index = parser.getNumberFromString(input);
            String message = TASK_LIST.deleteTask(index, parser);
            return message;
        } catch (NumberFormatException e) {
            String message = "Too many spaces used.";
            return message;
        }
    }

    /**
     * Performs search based on user input.
     *
     * @param input Input user input.
     */
    public static String handleSearchCommand(String input) {
        String[] inputParts = parser.splitStringBySpacing(input);
        Set<Task> alreadyAdded = new HashSet<>();
        List<Task> searchResults = new ArrayList<>();

        for (String inputPart : inputParts) {
            List<Task> searchResultsForWord = TASK_LIST.getSearchResults(inputPart);
            if (searchResultsForWord != null) {
                searchResults.addAll(searchResultsForWord);
            }
        }

        for (Task searchResult : searchResults) {
            if (!alreadyAdded.contains(searchResult)) {
                alreadyAdded.add(searchResult);
            } else {
                searchResults.remove(searchResult);
            }
        }

        String message;
        if (searchResults.size() != 0) {
            message = "Here are the matching tasks in your list:\n";
            for (int i = 0; i < searchResults.size(); i++) {
                Task t = searchResults.get(i);
                String taskDescription;
                if (i == searchResults.size() - 1) {
                    taskDescription = (i + 1) + "." + t.getTaskDescription();
                } else {
                    taskDescription = (i + 1) + "." + t.getTaskDescription() + "\n";
                }
                message += taskDescription;
            }
            return message;
        } else {
            message = "None of the tasks match the description.";
        }
        return message;
    }


     /**
      * Performs tasks based on user input and returns a message string.
      *
      * @param input Input is given by the user. Input is "" if the CLI is used.
      * @param isFromConsole IsFromConsole indicates where the input is being read from, which is either the console or
      * the GUI.
      * @return message string
     */
    public static String chat(String input, boolean isFromConsole) {
        if (isFromConsole) {
            input = parser.parse();
        }
        String outputMessage;
        try {
            CommandType commandType = identifyCommandFromInput(input);
            if (commandType == CommandType.MARK) {
                outputMessage = handleMarkCommand(input);
            } else if (commandType == CommandType.TODO) {
                outputMessage = handleTodoCommand(input);
            } else if (commandType == CommandType.DEADLINE) {
                outputMessage = handleDeadlineCommand(input);
            } else if (commandType == CommandType.EVENT) {
                outputMessage = handleEventCommand(input);
            } else if (commandType == CommandType.LIST) {
                outputMessage = handleListCommand();
            } else if (commandType == CommandType.DELETE) {
                outputMessage = handleDeleteCommand(input);
            } else if (commandType == CommandType.BYE) {
                outputMessage = handleExitCommand();
            } else {
                outputMessage = handleSearchCommand(input);
            }
            System.out.println(outputMessage);
            return outputMessage;
        } catch (DukeException e) {
            outputMessage = "I really want to help you but I do not know how I can do so." +
                    "Try another command or give me more info.";
            System.out.println(outputMessage);
            return outputMessage;
        }
    }

    /**
     * Returns a message that is meant to greet the user.
     *
     * @return greeting message string.
     */
    public static String greet() {
        String greetingMessage = "Hello! I'm Bob. " +
                "Remember to say \"bye\" if you want me to remember all the tasks you've told me about..." +
                "\nSo, what can I do for you?";
        System.out.println(greetingMessage);
        return greetingMessage;
    }

    /**
     * Loads saved tasks from hard disc, if present and returns a message indicating if the saved tasks were
     * successfully loaded.
     *
     * @return message as a string.
     */
    public static String retrieveSavedTaskData() {
        String message = STORAGE.loadSavedTasks(parser, TASK_LIST);
        System.out.println(message);
        return message;
    }

    /**
     * Returns appropriate message to user as a string.
     *
     * @param input Input is the user input.
     * @return message as a string.
     */
    public String getResponse(String input) {
        String outputMessage = chat(input, false);
        return outputMessage;
    }

    public static void main(String[] args) {
        retrieveSavedTaskData();
        greet();
        while (!isEndConversation) {
            chat("", true); // for running from command line
        }
    }
}
