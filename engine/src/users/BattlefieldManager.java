package users;

import components.Battlefield;

import java.util.HashMap;
import java.util.Map;

public class BattlefieldManager {
    private final Map<String, Battlefield> battlefieldMap = new HashMap<>();

    public void addBattlefield(String battlefieldName, Battlefield battleField) {
        battlefieldMap.put(battlefieldName, battleField);
    }

    public boolean isBattlefieldExists(String battlefieldName) {
        return battlefieldMap.get(battlefieldName) != null ? true : false;
    }

    public Map<String, Battlefield> getBattlefieldMap() {
        return battlefieldMap;
    }

    public Battlefield getBattlefield(String battlefieldName){
        return battlefieldMap.get(battlefieldName);
    }
}
