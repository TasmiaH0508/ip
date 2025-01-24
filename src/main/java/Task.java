class Task {
    String taskName;
    boolean isComplete;

    Task(String name) {
        this.taskName = name;
        this.isComplete = false;
    }

    public void markAsDone() {
        this.isComplete = true;
    }

    public void markAsNotDone() {
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
}