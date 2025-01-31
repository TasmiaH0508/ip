import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private String deadlineString;
    private LocalDate date;

    public Deadline(String taskDescription, String deadlineString) {
        super(taskDescription);
        this.deadlineString = deadlineString;
        date = LocalDate.parse(deadlineString);
    }

    @Override
    public String getTaskDescription() {
        return "[D]" + super.getTaskDescription() + "(by: " + date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }

    @Override
    public String getString() {
        return "deadline/" + super.getString() + "/" + deadlineString;
    }
}