public class Deadline extends Task {

    private String deadline;

    public Deadline(String taskDescription, String deadline) {
        super(taskDescription);
        this.deadline = deadline;
    }

    @Override
    public String getTaskDescription() {
        return "[D]" + super.getTaskDescription() + "(by: " + deadline + ")";
    }
}