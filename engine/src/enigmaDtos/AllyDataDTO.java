package enigmaDtos;

public class AllyDataDTO {
    private String name;
    private int amountOfAgents;
    private int taskSize;

    public AllyDataDTO(String name,int amountOfAgents,int taskSize) {
        this.name = name;
        this.taskSize = taskSize;
        this.amountOfAgents = amountOfAgents;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public String getName() {
        return name;
    }

    public int getAmountOfAgents() {
        return amountOfAgents;
    }

    public void setAmountOfAgents(int amountOfAgents) {
        this.amountOfAgents = amountOfAgents;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }
}
