package enigmaDtos;

public class AgentDataDTO {
    private String name;
    private int amountOfThreads;
    private int tasksPerPull;

    public AgentDataDTO(String name,int amountOfThreads,int tasksPerPull){
        this.name = name;
        this.amountOfThreads = amountOfThreads;
        this.tasksPerPull = tasksPerPull;
    }

    public int getTasksPerPull() {
        return tasksPerPull;
    }

    public int getAmountOfThreads() {
        return amountOfThreads;
    }

    public String getName() {
        return name;
    }
}
