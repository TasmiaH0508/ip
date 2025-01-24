import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> tasks;
    private int numTasks = 0;

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
}