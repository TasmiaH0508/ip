package bob.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    @Override
    public String getTaskDescription() {
        return "[E]" + super.getTaskDescription() + "(from:" + startTime.format(DateTimeFormatter.ofPattern("MMM d yyyy"))
                + " to:" + endTime.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }

    @Override
    public String getString() {
        return "event/" + super.getString() + "/" + startTimeString + "/" + endTimeString;
    }
}