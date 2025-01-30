import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TaskList {
    private List<Task> tasks;
    private int numTasks = 0;
    private enum TaskType {TODO, DEADLINE, EVENT};
    private String[] taskCommands = {"todo", "deadline", "event"};

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

    public void createAndAddTask(Parser p, String s, TaskType t) throws IndexOutOfBoundsException {
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

    public TaskType getTaskType(Parser p, String s) throws IllegalArgumentException {
        for (int i = 0; i < taskCommands.length; i++) {
            if (p.containsKeyword(s, taskCommands[i], "")) {
                return TaskType.values()[i];
            }
        }
        throw new IllegalArgumentException("File is corrupted");
    }

    public void loadSavedTasks(Parser p) {
        File savedTaskData = new File("TaskData.txt");
        try {
            if (savedTaskData.exists()) {
                Scanner fileScanner = new Scanner(savedTaskData);
                List<String> rawTaskList = new LinkedList<>();
                while (fileScanner.hasNextLine()) {
                    String taskString = fileScanner.nextLine();
                    rawTaskList.add(taskString);
                }
                for (String rawTask : rawTaskList) {
                    TaskType taskType = getTaskType(p, rawTask);
                    createAndAddTask(p, rawTask, taskType);
                }
            } else {
                savedTaskData.createNewFile();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("Some lines in the file appear corrupted. Attempting to remove and recover the remaining data...");
        }
    }

    public void writeTaskDataToFile() {
        String textToAdd = "";
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            String taskString = t.getString();
            if (i == tasks.size() - 1) {
                textToAdd += taskString;
            } else {
                textToAdd += taskString + System.lineSeparator();
            }
        }
        try {
            FileWriter fw = new FileWriter("TaskData.txt");
            fw.write(textToAdd);
            fw.close();
        } catch (IOException e) {
            System.out.println("Your tasks could not be saved. Sorry for the inconvenience.");
        }
    }
}