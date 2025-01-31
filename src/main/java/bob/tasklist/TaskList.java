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

    public void addTask(Task t) {
        System.out.println("Got it. I've added this task:");
        tasks.add(t);
        numTasks++;
        System.out.println(t.getTaskDescription());
        System.out.println("Now you have " + numTasks + " tasks in the list.");
    }

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
            task.markAsDone();
        }
        tasks.add(task);
        numTasks++;
    }

    public void updateTaskCompletionStatus(int index, boolean isDone) {
        if (index <= 0 || index > numTasks) {
            System.out.println("Invalid task index.");
        } else {
            index--;
            if (isDone) {
                System.out.println("Nice! I have marked this task as done:");
                tasks.get(index).markAsDone();
                System.out.println(tasks.get(index).getTaskDescription());
            } else {
                System.out.println("OK, I've marked this task as not done yet:");
                tasks.get(index).markAsNotDone();
                System.out.println(tasks.get(index).getTaskDescription());
            }
        }
    }

    public void displayTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < numTasks; i++) {
            System.out.println((i + 1) + "." + tasks.get(i).getTaskDescription());
        }
    }

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

    public static TaskType getTaskType(Parser p, String s) throws IllegalArgumentException {
        for (int i = 0; i < taskCommands.length; i++) {
            if (p.containsKeyword(s, taskCommands[i], "")) {
                return TaskType.values()[i];
            }
        }
        throw new IllegalArgumentException("File is corrupted.");
    }

    public List<Task> getTaskList() {
        return tasks;
    }
}