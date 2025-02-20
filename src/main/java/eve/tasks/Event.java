package eve.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Represents an event task, which is a type of task.
 */
public class Event extends Task {

    private String startTimeString;
    private String endTimeString;
    private LocalDate startTime;
    private LocalDate endTime;

    public Event(String taskDescription, String startTimeString, String endTimeString) {
        super(taskDescription);
        this.startTimeString = startTimeString;
        this.endTimeString = endTimeString;
        this.startTime = LocalDate.parse(startTimeString);
        this.endTime = LocalDate.parse(endTimeString);
    }

    /**
     * Returns task description.
     *
     * @return taskname, prefixed by status icon and task label [E], followed by the start and ent time.
     */
    @Override
    public String getTaskDescription() {
        return "[E]" + super.getTaskDescription() + "(from:" + startTime.format(DateTimeFormatter.ofPattern("MMM d yyyy"))
                + " to:" + endTime.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }

    /**
     * Returns string representation of task.
     *
     * @return a string representing the task.
     */
    @Override
    public String getString() {
        return "event/" + super.getString() + "/" + startTimeString + "/" + endTimeString;
    }
}