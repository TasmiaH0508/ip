package eve.storage;

import eve.parser.Parser;

import eve.tasks.Task;

import eve.tasklist.TaskList;

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
     * Loads saved tasks from "TaskData.txt" file and returns a message indicating if tasks have been loaded successfully.
     *
     * @param p P parser.
     * @param taskList taskList list of tasks.
     * @return a message as a string indicating if tasks haven been successfully loaded.
     */
    public String loadSavedTasks(Parser p, TaskList taskList) {
        File savedTaskData = new File("TaskData.txt");
        String message;
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
            message = ""; // design decision: do not inform user that task data has been retrieved
            return message;
        } catch (IOException e) {
            message = "Creation of new file not successful.";
            return message;
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            message = "Some lines in the file appear corrupted. " +
                    "Attempting to remove and recover the remaining data...";
            return message;
        }
    }

    /**
     * Saves tasks to "TaskData.txt" file and returns a boolean flag indication if all the data has been saved.
     *
     * @param taskList TaskList list of tasks to be saved.
     * @return a boolean flag indicating if task data has been successfully saved
     */
    public boolean writeTaskDataToFile(TaskList taskList) {
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
        boolean isSaved;
        try {
            FileWriter fw = new FileWriter("TaskData.txt");
            fw.write(textToAdd);
            fw.close();
            isSaved = true;
            return isSaved;
        } catch (IOException e) {
            isSaved = false;
            return isSaved;
        }
    }
}