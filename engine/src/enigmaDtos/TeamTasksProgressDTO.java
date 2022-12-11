package enigmaDtos;

public class TeamTasksProgressDTO {
    private int tasksInQueue;
    private int totalPulledTasksLabel;
    private int totalCompletedTasksLabel;

    public TeamTasksProgressDTO(int tasksInQueue, int totalPulledTasksLabel
            , int totalCompletedTasksLabel) {
        this.tasksInQueue = tasksInQueue;
        this.totalPulledTasksLabel = totalPulledTasksLabel;
        this.totalCompletedTasksLabel = totalCompletedTasksLabel;
    }

    public int getTasksInQueue() {
        return tasksInQueue;
    }

    public int getTotalPulledTasksLabel() {
        return totalPulledTasksLabel;
    }

    public int getTotalCompletedTasksLabel() {
        return totalCompletedTasksLabel;
    }
}
