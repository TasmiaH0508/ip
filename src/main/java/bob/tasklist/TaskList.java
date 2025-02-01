package bob.tasklist;

import bob.parser.Parser;

import bob.tasks.Deadline;
import bob.tasks.Event;
import bob.tasks.Task;
import bob.tasks.Todo;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private static List<Task> tasks;
    private static int numTasks = 0;
    private enum TaskType {TODO, DEADLINE, EVENT};
    private static String[] taskCommands = {"todo", "deadline", "event"};

    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Adds task to list of tasks.
     *
     * @param t T task to be added to the list of tasks.
     */
    public void addTask(Task t) {
        System.out.println("Got it. I've added this task:");
        tasks.add(t);
        numTasks++;
        System.out.println(t.getTaskDescription());
        System.out.println("Now you have " + numTasks + " tasks in the list.");
    }

    /**
     * Creates and adds task to list of tasks.
     *
     * @param p P parser.
     * @param s S string representation of a single task, loaded from saved file containing task data.
     * @throws IndexOutOfBoundsException if string contains too few slashes separating the relevant information for a
     * particular task type.
     */
    public static void createAndAddTask(Parser p, String s) throws IndexOutOfBoundsException {
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
     * Shows tasks of specific indices only.
     *
     * @param indicesList IndicesList contains indices of tasks to be displayed.
     */
    public void displaySelectedTasks(List<Integer> indicesList) {
        List<Task> selectedTasks = new ArrayList<>();
        for (Integer index : indicesList) {
            Task t = tasks.get(index);
            selectedTasks.add(t);
        }
        int numSelectedTasks = indicesList.size();
        for (int i = 0; i < numSelectedTasks; i++) {
            System.out.println((i + 1) + "." + selectedTasks.get(i).getTaskDescription());
        }
    }

    /**
     * Removes task at given index.
     *
     * @param index Index position of task in list.
     */
    public void deleteTask(int index) {
        if (index <= 0 || index > numTasks) {
            System.out.println("Invalid task index.");
        } else {
            System.out.println("Noted. I've removed this task:");
            index--;
            System.out.println(tasks.get(index).getTaskDescription());
            tasks.remove(index);
            numTasks--;
            System.out.println("Now you have " + numTasks + " tasks in the list.");
        }
    }

    /**
     * Returns task type.
     *
     * @param p P parser.
     * @param s S string representation of a task.
     * @throws IllegalArgumentException if string does not contain the any of the substrings: "todo", "event", "deadline".
     */
    public static TaskType getTaskType(Parser p, String s) throws IllegalArgumentException {
        for (int i = 0; i < taskCommands.length; i++) {
            if (p.prefixedByKeyword(s, taskCommands[i], "")) {
                return TaskType.values()[i];
            }
        }
        throw new IllegalArgumentException("File is corrupted.");
    }

    public List<String> getAllTaskDescriptions() {
        List<String> ll = new ArrayList<>();
        for (Task t : tasks) {
            String taskDescription = t.getTaskDescriptionWOIcon();
            ll.add(taskDescription);
        }
        return ll;
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