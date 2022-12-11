package users;

import enigmaDtos.*;
import utils.BattlefieldLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Ally {

    private int amountOfAgents = 0;
    private String name;
    private int taskSize = 0;
    private String uboatAssignedTo;
    private String battlefieldAssignedTo = null;
    private boolean isReady = false;
    private Map<String, Agent> agentsInAlly = new HashMap<>();
    private DM dm;
    private AllyDataDTO allyDataDTO;
    private BlockingQueue<CandidateDTO> candidateQueue = new LinkedBlockingQueue();

    public Ally(String name) {
        this.name = name;
        allyDataDTO = new AllyDataDTO(name,amountOfAgents,taskSize);
    }

    public void setUboatAssignedTo(String uboatAssignedTo) {
        this.uboatAssignedTo = uboatAssignedTo;
    }

    public int getAmountOfAgents() {
        return amountOfAgents;
    }

    public String getName() {
        return name;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public String getUboatAssignedTo() {
        return uboatAssignedTo;
    }

    public void setBattlefieldAssignedTo(String battlefieldAssignedTo) {
        this.battlefieldAssignedTo = battlefieldAssignedTo;
    }

    public String getBattlefieldAssignedTo() {
        return battlefieldAssignedTo;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public void addAgent(String name, Agent agent) {
        agentsInAlly.put(name, agent);
        amountOfAgents++;
    }

    public Map<String, Agent> getAgentsInAlly() {
        return agentsInAlly;
    }

    public void setDM(FileDetailsDTO fileDetailsDTO, BattlefieldLevel battlefieldLevel) {
        dm = new DM(fileDetailsDTO, battlefieldLevel, taskSize);
    }
    public ArrayList<BruteForceConfigDTO> pullTasksFromDM(int tasksPerPull){
        return dm.pullTasks(tasksPerPull);
    }

    public AllyDataDTO getAllyDataDTO(){
      allyDataDTO.setAmountOfAgents(amountOfAgents);
      allyDataDTO.setTaskSize(taskSize);
      return allyDataDTO;
    }

    public ArrayList<AgentDataDTO> getAllAgentsDataDto(){
        ArrayList<AgentDataDTO> allAgentsDataDto = new ArrayList<>();
        for(Map.Entry<String,Agent> entry : agentsInAlly.entrySet()){
            allAgentsDataDto.add(entry.getValue().getAgentDataDTO());
        }
        return allAgentsDataDto;
    }
    public BlockingQueue<CandidateDTO> getCandidateQueue(){
        return candidateQueue;
    }

    public void startDM(){
        dm.start();
    }
    public void addCandidate(CandidateDTO candidate) {
        candidateQueue.add(candidate);
    }

    public void removeAgent(String name){
        agentsInAlly.remove(name);
        amountOfAgents--;
    }
}