package bob.tasklist;

import bob.parser.Parser;

import bob.tasks.Deadline;
import bob.tasks.Event;
import bob.tasks.Task;
import bob.tasks.Todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Adds task to list of tasks.
     *
     * @param t T task to be added to the list of tasks.
     */
    public void addTask(Task t, Parser p) {
        System.out.println("Got it. I've added this task:");
        tasks.add(t);
        numTasks++;
        System.out.println(t.getTaskDescription());
        System.out.println("Now you have " + numTasks + " tasks in the list.");

        // update hashmap with keywords in task description to facilitate searching later
        String taskDescription = t.getTaskDescriptionWOIcon();
        String[] taskDescriptionParts = p.splitStringBySpacing(taskDescription);
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
    }

    /**
     * Creates and adds task to list of tasks.
     *
     * @param p P parser.
     * @param s S string representation of a single task, loaded from saved file containing task data.
     * @throws IndexOutOfBoundsException if string contains too few slashes separating the relevant information for a
     * particular task type.
     */
    public void createAndAddTask(Parser p, String s) throws IndexOutOfBoundsException {
        TaskType t = getTaskType(p, s);
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
     * Marks a task at a particular index as done or not done, depending on isDone flag.
     *
     * @param index Index position of task in task list.
     * @param isDone IsDone true for task to be marked as complete and false for a task to be marked as incomplete.
     */
    public void updateTaskCompletionStatus(int index, boolean isDone) {
        if (index <= 0 || index > numTasks) {
            System.out.println("Invalid task index.");
        } else {
            index--;
            if (isDone) {
                System.out.println("Nice! I have marked this task as done:");
                tasks.get(index).setAsDone();
                System.out.println(tasks.get(index).getTaskDescription());
            } else {
                System.out.println("OK, I've marked this task as not done yet:");
                tasks.get(index).setAsNotDone();
                System.out.println(tasks.get(index).getTaskDescription());
            }
        }
    }

    /**
     * Shows the tasks as a numbered list of tasks.
     */
    public void displayTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < numTasks; i++) {
            System.out.println((i + 1) + "." + tasks.get(i).getTaskDescription());
        }
    }

    /**
     * Removes task at given index.
     *
     * @param index Index position of task in list.
     */
    public void deleteTask(int index, Parser p) {
        if (index <= 0 || index > numTasks) {
            System.out.println("Invalid task index.");
        } else {
            System.out.println("Noted. I've removed this task:");
            index--;
            Task t = tasks.get(index);
            System.out.println(t.getTaskDescription());
            tasks.remove(t);
            numTasks--;
            System.out.println("Now you have " + numTasks + " tasks in the list.");

            // Update hashmap
            String taskDescription = t.getTaskDescriptionWOIcon();
            String[] taskDescriptionParts = p.splitStringBySpacing(taskDescription);
            for (String taskDescriptionPart : taskDescriptionParts) {
                if (stringToTasks.containsKey(taskDescriptionPart)) {
                    List<Task> ll = stringToTasks.get(taskDescriptionPart);
                    ll.remove(t);
                }
            }
        }
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

    public List<Task> getSearchResults(String input) {
        if (stringToTasks.containsKey(input)) {
            return stringToTasks.get(input);
        } else {
            return null;
        }
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