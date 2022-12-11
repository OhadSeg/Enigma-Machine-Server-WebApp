package enigmaDtos;

import components.Machine;

public class MachineDetailsDTO {
    String rotorsInUse;
    String rotorsInitialPositionsInUse;
    String reflectorInUse;
    String plugsBoardInUse;

    public MachineDetailsDTO(String rotorsFromUser, String rotorsInitalPosFromUser, String reflectorFromUser, String plugBoardFromUser) {
        this.rotorsInUse = rotorsFromUser.toUpperCase();
        this.rotorsInitialPositionsInUse = rotorsInitalPosFromUser.toUpperCase();
        this.reflectorInUse = reflectorFromUser.toUpperCase();
        this.plugsBoardInUse = plugBoardFromUser.toUpperCase();
    }

    public String getRotorsInUse() {
        return rotorsInUse;
    }

    public String getRotorsInitialPositions() {
        return rotorsInitialPositionsInUse;
    }

    public String getReflectorInUse() {
        return reflectorInUse;
    }

    public String getPlugsBoardInUse() {
        return plugsBoardInUse;
    }
}
