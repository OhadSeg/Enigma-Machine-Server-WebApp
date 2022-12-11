package users;

import components.Battlefield;
import components.Enigma;
import operation.Tests;
import utils.UserType;

import java.util.*;

public class UserManager {
    private final Set<String> usersSet;
    private final Map<String, Uboat> uboatMap = new HashMap<>();
    private final Map<String, Ally> allyMap = new HashMap<>();
    private final Map<String, Agent> agentMap = new HashMap<>();
    private final Tests tests = new Tests();

    public UserManager() {
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public synchronized Enigma getEnigmaFromUboat(String uboatName) {
        return uboatMap.get(uboatName).getEnigma();
    }

    public synchronized boolean isUserExists(String username) {
        return usersSet.contains(username);
    }

    public synchronized void addEntity(String name, Object entity, UserType userType) {
        switch (userType) {
            case UBOAT:
                uboatMap.put(name, (Uboat) entity);
                break;
            case ALLY:
                allyMap.put(name, (Ally) entity);
                break;
            case AGENT:
                agentMap.put(name, (Agent) entity);
                break;
        }
    }

    public synchronized void removeEntity(String name, UserType userType) {
        usersSet.remove(name);
        switch (userType) {
            case UBOAT:
                uboatMap.remove(name);
                break;
            case ALLY:
                allyMap.remove(name);
                break;
            case AGENT:
                agentMap.remove(name);
                break;
        }
    }

    public synchronized Ally getAlly(String name) {
        return allyMap.get(name);
    }

    public synchronized Uboat getUboat(String name) {
        return uboatMap.get(name);
    }

    public synchronized Agent getAgent(String name) {
        return agentMap.get(name);
    }

    public synchronized Tests getTest() {
        return tests;
    }

    public synchronized ArrayList<String> getAlliesNames() {
        ArrayList<String> alliesNames = new ArrayList<>();

        for (Map.Entry<String, Ally> entry : allyMap.entrySet()) {
            if(entry.getValue().getBattlefieldAssignedTo() != null) {
                alliesNames.add(entry.getKey());
            }
        }
        return alliesNames;
    }
}
