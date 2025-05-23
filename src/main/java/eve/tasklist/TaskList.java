package eve.tasklist;

import eve.parser.Parser;

import eve.tasks.Deadline;
import eve.tasks.Event;
import eve.tasks.Task;
import eve.tasks.Todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents a list of task objects.
 */
public class TaskList {
    private List<Task> tasks;
    private int numTasks = 0;
    private enum TaskType {TODO, DEADLINE, EVENT};
    private String[] taskCommands = {"todo", "deadline", "event"};
    private Map<String, List<Task>> stringToTasks = new HashMap<>();

    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Adds task to list of tasks and returns a message indicating the task has been added.
     *
     * @param t T task to be added to the list of tasks.
     * @param p P parser.
     * @return message as a string.
     */
    public String addTask(Task t, Parser p) {
        tasks.add(t);
        numTasks++;
        String taskDescription = t.getTaskDescription();
        String message = "Got it. I've added this task:\n" + taskDescription
                + "\nNow you have " + numTasks + " tasks in the list.";

        // update hashmap with keywords in task description to facilitate searching later
        String taskDescriptionWOIcon = t.getTaskDescriptionWOIcon();
        String[] taskDescriptionParts = p.splitStringBySpacing(taskDescriptionWOIcon);
        for (String taskDescriptionPart : taskDescriptionParts) {
            if (stringToTasks.containsKey(taskDescriptionPart)) {
                List<Task> ll = stringToTasks.get(taskDescriptionPart);
                ll.add(t);
            } else {
                List<Task> ll = new ArrayList<>();
                ll.add(t);
                stringToTasks.put(taskDescriptionPart, ll);
            }
        }
        return message;
    }

    /**
     * Creates and adds task to list of tasks.
     *
     * @param p P parser.
     * @param s S string representation of a single task, loaded from saved file containing task data.
     * @throws IndexOutOfBoundsException if string contains too few slashes separating the relevant information for a
     * particular task type.
     */
    public void createAndAddTask(Parser p, String s) throws IndexOutOfBoundsException, IllegalArgumentException {
        TaskType t = getTaskType(p, s); // throws illegal argument exception. Otherwise, it returns an enum type
        Task task;
        String[] arr = p.splitStringBySlash(s);
        String taskDescription = arr[2];
        String isDone = arr[1];
        if (t == TaskType.TODO) {
            task = new Todo(taskDescription);
        } else if (t == TaskType.DEADLINE) {
            String deadline = arr[3];
            task = new Deadline(taskDescription, deadline);
        } else {
            String startTime = arr[3];
            String endTime = arr[4];
            task = new Event(taskDescription, startTime, endTime);
        }
        if (isDone.equals("true")) {
            task.setAsDone();
        }
        tasks.add(task);
        numTasks++;

        // update hashmap with keywords in task description to facilitate searching later
        String[] taskDescriptionParts = p.splitStringBySpacing(taskDescription);
        for (String taskDescriptionPart : taskDescriptionParts) {
            if (stringToTasks.containsKey(taskDescriptionPart)) {
                List<Task> ll = stringToTasks.get(taskDescriptionPart);
                ll.add(task);
            } else {
                List<Task> ll = new ArrayList<>();
                ll.add(task);
                stringToTasks.put(taskDescriptionPart, ll);
            }
        }
    }

    /**
     * Marks a task at a particular index as done or not done, depending on isDone flag, and returns a message
     * indicating if task completion status has been updated successfully.
     *
     * @param index Index position of task in task list.
     * @param isDone IsDone true for task to be marked as complete and false for a task to be marked as incomplete.
     * @return message as a string
     */
    public String updateTaskCompletionStatus(int index, boolean isDone) {
        String taskDescription;
        if (index <= 0 || index > numTasks) {
            taskDescription = ""; // The task description is only returned iff the marking was successful.
            return taskDescription;
        }
        index--;
        if (isDone) {
            tasks.get(index).setAsDone();
        } else {
            tasks.get(index).setAsNotDone();
        }
        taskDescription = tasks.get(index).getTaskDescription();
        return taskDescription;
    }

    /**
     * Shows the tasks as a numbered list of tasks and returns a string containing all the tasks.
     *
     * @return all the tasks as a string.
     */
    public String displayTasks() {
        String message = "Here are the tasks in your list:\n";
        for (int i = 0; i < numTasks; i++) {
            String stringToAdd;
            if (i == numTasks - 1) {
                stringToAdd = (i + 1) + "." + tasks.get(i).getTaskDescription();
            } else {
                stringToAdd = (i + 1) + "." + tasks.get(i).getTaskDescription() + "\n";
            }
            message += stringToAdd;
        }
        return message;
    }

    /**
     * Returns the indices of the completed tasks. The indices follow 1-indexing.
     *
     * @return a list of indices of the tasks that are complete.
     */
    public List<Integer> getIndicesOfCompletedTasks() {
        List<Integer> completedTasks = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.isComplete()) {
                int indexOfTask = i + 1;
                completedTasks.add(indexOfTask);
            }
        }
        return completedTasks;
    }

    /**
     * Removes task at given index and returns a string giving information about which task was removed.
     *
     * @param index Index position of task in list.
     * @param p P parser.
     * @return task description of task deleted if deletion was successful.
     */
    public String deleteTask(int index, Parser p) {
        String taskDescription;
        if (index <= 0 || index > numTasks) {
            taskDescription = "";
            return taskDescription; // The task description is only returned iff the deletion was successful.
        }
        index--;
        Task t = tasks.get(index);
        tasks.remove(t);
        numTasks--;
        taskDescription = t.getTaskDescription();

        String taskDescriptionWOIcon = t.getTaskDescriptionWOIcon();
        String[] taskDescriptionParts = p.splitStringBySpacing(taskDescriptionWOIcon);
        for (String taskDescriptionPart : taskDescriptionParts) {
            if (stringToTasks.containsKey(taskDescriptionPart)) {
                List<Task> ll = stringToTasks.get(taskDescriptionPart);
                ll.remove(t);
            }
        }
        return taskDescription;
    }

    /**
     * Returns the number of tasks present in task list.
     *
     * @return number of tasks in task list.
     */
    public int getNumTasks() {
        return numTasks;
    }

    /**
     * Returns task type.
     *
     * @param p P parser.
     * @param s S string representation of a task.
     * @throws IllegalArgumentException if string does not contain the any of the substrings: "todo", "event", "deadline".
     */
    public TaskType getTaskType(Parser p, String s) throws IllegalArgumentException {
        for (int i = 0; i < taskCommands.length; i++) {
            if (p.prefixedByKeyword(s, taskCommands[i], "")) {
                return TaskType.values()[i];
            }
        }
        throw new IllegalArgumentException("File is corrupted.");
    }

    /**
     * Returns list of tasks containing the input keyword.
     *
     * @param input Input word to look for in task description.
     * @return list of tasks, each of which has a task description containing the input keyword.
     */
    public List<Task> getSearchResults(String input) {
        return stringToTasks.get(input);
    }

    /**
     * Returns list of tasks.
     *
     * @return tasks.
     */
    public List<Task> getTaskList() {
        return tasks;
    }
}