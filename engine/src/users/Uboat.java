package users;

import components.Battlefield;
import components.Enigma;
import enigmaDtos.AllyDataDTO;
import enigmaDtos.CandidateDTO;
import utils.BattlefieldLevel;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Uboat {
    private Enigma enigma;
    private String winningWord;
    private String battlefieldName;

    private boolean isThereWinner = false;
    private Map<String, Ally> alliesInBattlefield = new HashMap<>();
    private BlockingQueue<CandidateDTO> candidateQueue = new LinkedBlockingQueue();

    public Uboat(Enigma enigma, String battlefieldName) {
        this.enigma = enigma;
        this.battlefieldName = battlefieldName;
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public Enigma getEnigma() {
        return enigma;
    }

    public void addAlly(String name, Ally ally) {
        alliesInBattlefield.put(name, ally);
    }
    public void addCandidate(CandidateDTO candidate) {
        candidateQueue.add(candidate);
    }

    public Map<String, Ally> getAlliesInBattlefield() {
        return alliesInBattlefield;
    }

    public boolean isAllAlliesReady() {
        for (Map.Entry<String, Ally> entry : alliesInBattlefield.entrySet()) {
            if (entry.getValue().isReady() == false) {
                return false;
            }
        }
        return true;
    }

    public void startContest(BattlefieldLevel battlefieldLevel) {
        winningWord = enigma.getConfigurationAsDTO().getTextBeforeCode();

        for (Map.Entry<String, Ally> entry : alliesInBattlefield.entrySet()) {
            entry.getValue().setDM(enigma.getConfigurationAsDTO(),battlefieldLevel);
        }
        for (Map.Entry<String, Ally> entry : alliesInBattlefield.entrySet()){
            entry.getValue().startDM();
        }

    }
    public ArrayList<AllyDataDTO> getAllAlliesDataDto(){
        ArrayList<AllyDataDTO> allAlliesDataDto = new ArrayList<>();
        for(Map.Entry<String,Ally> entry : alliesInBattlefield.entrySet()){
            allAlliesDataDto.add(entry.getValue().getAllyDataDTO());
        }
        return allAlliesDataDto;
    }
    public BlockingQueue<CandidateDTO> getCandidateQueue(){
        return candidateQueue;
    }

    public boolean ifThereIsWinner() {
        return isThereWinner;
    }

    public String getWinningWord() {
        return winningWord;
    }
}