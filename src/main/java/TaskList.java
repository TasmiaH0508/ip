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

    public void updateTaskCompletionStatus(int index, boolean isDone) {
        if (index <= 0 || index > numTasks) {
            System.out.println("Invalid task index.");
        } else {
            index--;
            if (isDone) {
                System.out.println("Nice, I have marked this task as done:");
                tasks[index].markAsDone();
                System.out.println(tasks[index].getTaskDescription());
            } else {
                System.out.println("OK, I've marked this task as not done yet:");
                tasks[index].markAsNotDone();
                System.out.println(tasks[index].getTaskDescription());
            }
        }
    }

    public void displayTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < numTasks; i++) {
            System.out.println((i + 1) + ". " + tasks[i].getTaskDescription());
        }
    }
}