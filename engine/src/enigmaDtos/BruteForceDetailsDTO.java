package enigmaDtos;

import utils.BattlefieldLevel;

public class BruteForceDetailsDTO {
    private Integer agents;
    private BattlefieldLevel level;
    private Integer taskSize;

    public BruteForceDetailsDTO(Integer agents, BattlefieldLevel level, Integer taskSize){
        this.agents = agents;
        this.level = level;
        this.taskSize = taskSize;
    }

    public Integer getAgents() {
        return agents;
    }

    public BattlefieldLevel getLevel() {
        return level;
    }

    public Integer getTaskSize() {
        return taskSize;
    }
}
