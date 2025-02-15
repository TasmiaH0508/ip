package bob.tasks;

/**
 * Represents a task object.
 */
public class Task {
    private String taskName;
    private boolean isComplete;

    Task(String name) {
        this.taskName = name;
        this.isComplete = false;
    }

    /**
     * Sets isComplete attribute as true.
     */
    public void setAsDone() {
        this.isComplete = true;
    }

    /**
     * Sets isComplete attribute as false.
     */
    public void setAsNotDone() {
        this.isComplete = false;
    }

    /**
     * Returns status icon.
     *
     * @return "[X]" if task is complete and "[ ]" otherwise.
     */
    public String getStatusIcon() {
        if (isComplete) {
            return "[X]";
        } else {
            return "[ ]";
        }
    }

    /**
     * Returns a boolean about whether a task is complete.
     *
     * @return a boolean flag indicating if a task is complete.
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Returns task description.
     *
     * @return taskname prefixed by status icon.
     */
    public String getTaskDescription() {
        return getStatusIcon() + this.taskName;
    }

    /**
     * Returns task name without status icon.
     *
     * @return taskname.
     */
    public String getTaskDescriptionWOIcon() {
        return this.taskName;
    }

    /**
     * Returns string representation of task.
     *
     * @return the isComplete and taskname as a string, where isComlete and taskname is separated by a slash.
     */
    public String getString() {
        return isComplete + "/" + taskName;
    }
}