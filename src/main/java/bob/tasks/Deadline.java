package bob.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Represents a deadline task, which is a type of task.
 */
public class Deadline extends Task {
    private String deadlineString;
    private LocalDate date;

    public Deadline(String taskDescription, String deadlineString) {
        super(taskDescription);
        this.deadlineString = deadlineString;
        date = LocalDate.parse(deadlineString);
    }

    /**
     * Returns task description.
     *
     * @return taskname, prefixed by status icon and task label [D], followed by deadline.
     */
    @Override
    public String getTaskDescription() {
        return "[D]" + super.getTaskDescription() + "(by: " + date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }

    /**
     * Returns string representation of task.
     *
     * @return a string representing the task.
     */
    @Override
    public String getString() {
        return "deadline/" + super.getString() + "/" + deadlineString;
    }
}