package bob.tasks;

public class Todo extends Task {

    public Todo(String s) {
        super(s);
    }

    /**
     * Returns task description.
     *
     * @return taskname prefixed by status icon and task label "[T]".
     */
    @Override
    public String getTaskDescription() {
        return "[T]" + super.getTaskDescription();
    }

    /**
     * Returns string representation of task.
     *
     * @return a string representing the task.
     */
    @Override
    public String getString() {
        return "todo/" + super.getString();
    }
}