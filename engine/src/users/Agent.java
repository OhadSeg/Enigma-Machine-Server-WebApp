package users;

import bruteForce.DecriptionManager;
import enigmaDtos.AgentDataDTO;
import enigmaDtos.CandidateDTO;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent {

    private String name;
    private int amountOfThreads;
    private String allyName;
    private String battlefieldAssignedTo;
    private int tasksPerPull;
    private BlockingQueue<CandidateDTO> candidateQueue = new LinkedBlockingQueue<>();
    private AgentDataDTO agentDataDTO;
    public Agent(String name,String allyName, String battlefieldAssignedTo, int amountOfThreads, int tasksPerPull){
        this.name = name;
        this.allyName = allyName;
        this.battlefieldAssignedTo = battlefieldAssignedTo;
        this.amountOfThreads = amountOfThreads;
        this.tasksPerPull = tasksPerPull;
        agentDataDTO = new AgentDataDTO(name,amountOfThreads,tasksPerPull);
    }

    public String getName() {
        return name;
    }

    public int getAmountOfThreads() {
        return amountOfThreads;
    }

    public int getTasksPerPull() {
        return tasksPerPull;
    }

    public String getAllyName() {
        return allyName;
    }

    public String getBattlefieldAssignedTo() {
        return battlefieldAssignedTo;
    }

    public AgentDataDTO getAgentDataDTO() {
        return agentDataDTO;
    }
    public BlockingQueue<CandidateDTO> getCandidateQueue(){
        return candidateQueue;
    }
    public void addCandidate(CandidateDTO candidate) {
        candidateQueue.add(candidate);
    }
}
