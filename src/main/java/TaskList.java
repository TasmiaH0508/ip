public class TaskList {
    Task[] tasks;
    int numTasks = 0;

    public TaskList() {
        tasks = new Task[100];
    }

    public void addTask(String taskName) {
        Task newTask = new Task(taskName);
        tasks[numTasks] = newTask;
        numTasks++;
    }

    public void displayTasks() {
        for (int i = 0; i < numTasks; i++) {
            System.out.println(tasks[i].getTaskName());
        }
    }
}