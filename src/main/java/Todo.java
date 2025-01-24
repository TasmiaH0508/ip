public class Todo extends Task {

    public Todo(String s) {
        super(s);
    }

    @Override
    public String getTaskDescription() {
        return "[T]" + super.getTaskDescription();
    }
}