package bob.tasks;

public class Todo extends Task {

    public Todo(String s) {
        super(s);
    }

    @Override
    public String getTaskDescription() {
        return "[T]" + super.getTaskDescription();
    }

    @Override
    public String getString() {
        return "todo/" + super.getString();
    }
}