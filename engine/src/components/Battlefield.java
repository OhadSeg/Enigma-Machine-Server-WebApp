package components;

import generated.CTEBattlefield;
import utils.BattlefieldLevel;
import utils.BattlefieldStatus;

public class Battlefield {

    private BattlefieldLevel level;
    private String battleName;
    private int allies;
    private BattlefieldStatus status;
    private int alliesJoined = 0;
    private String uboatName = new String();
    private String winningAlly;


    public Battlefield (CTEBattlefield cteBattlefield){

        this.allies = cteBattlefield.getAllies();
        this.battleName = cteBattlefield.getBattleName();
        this.level = BattlefieldLevel.valueOf(cteBattlefield.getLevel().toUpperCase());
        if(cteBattlefield.getLevel().equals("EASY")){
            this.level = BattlefieldLevel.EASY;
        }
        else{
            this.level = BattlefieldLevel.MEDIUM;
        }
        this.status = BattlefieldStatus.OFFLINE;
    }

    public void setWinningAlly(String winningAlly) {
        this.winningAlly = winningAlly;
    }

    public String getWinningAlly() {
        return winningAlly;
    }

    public void setStatus(BattlefieldStatus status) {
        this.status = status;
    }
    public BattlefieldStatus getStatus(){
        return status;
    }

    public int getAllies() {
        return allies;
    }

    public String getBattleName() {
        return battleName;
    }

    public void setUboatName(String uboatName) {
        this.uboatName = uboatName;
    }
    public String getUboatName(){
        return uboatName;
    }

    public BattlefieldLevel getLevel() {
        return level;
    }

    public void addAllyToCounter(){
        alliesJoined++;
    }

    public int getAlliesJoined() {
        return alliesJoined;
    }
}
