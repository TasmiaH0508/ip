package bob.storage;

import bob.parser.Parser;

import bob.tasks.Task;

import bob.tasklist.TaskList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


/**
 * Represents storage for saved task data.
 */
public class Storage {

    /**
     * Loads saved tasks from "TaskData.txt" file.
     *
     * @param p P parser.
     * @param taskList taskList list of tasks.
     */
    public void loadSavedTasks(Parser p, TaskList taskList) {
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
                    taskList.createAndAddTask(p, rawTask);
                }
            } else {
                savedTaskData.createNewFile();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("Some lines in the file appear corrupted. " +
                    "Attempting to remove and recover the remaining data...");
        }
    }

    /**
     * Saves tasks to "TaskData.txt" file.
     *
     * @param taskList TaskList list of tasks to be saved
     */
    public void writeTaskDataToFile(TaskList taskList) {
        List<Task> tasks = taskList.getTaskList();
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