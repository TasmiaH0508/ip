package bob.tasks;

public class Task {
    private String taskName;
    private boolean isComplete;

    Task(String name) {
        this.taskName = name;
        this.isComplete = false;
    }

    public void setAsDone() {
        this.isComplete = true;
    }

    public void setAsNotDone() {
        this.isComplete = false;
    }

    public String getStatusIcon() {
        if (isComplete) {
            return "[X]";
        } else {
            return "[ ]";
        }
    }

    public String getTaskDescription() {
        return getStatusIcon() + this.taskName;
    }

    public String getString() {
        return isComplete + "/" + taskName;
    }
}