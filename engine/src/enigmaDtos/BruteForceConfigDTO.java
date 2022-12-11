package enigmaDtos;

import utils.RomanNumbers;

import java.io.Serializable;
import java.util.ArrayList;

public class BruteForceConfigDTO {

    private ArrayList<Integer> chosenValidInUseRotors = new ArrayList<>();
    private int rotorsStartPosPermutation = 0;
    private RomanNumbers chosenValidReflector;

    public BruteForceConfigDTO(){}

    public BruteForceConfigDTO(BruteForceConfigDTO configuraion) {
        setChosenValidInUseRotors(configuraion.getChosenValidInUseRotors());
        setRotorsStartPosPermutation(configuraion.getRotorsStartPosPermutation());
        setChosenValidReflector(configuraion.getChosenValidReflector());
    }

    public ArrayList<Integer> getChosenValidInUseRotors() {
        return chosenValidInUseRotors;
    }

    public void setChosenValidInUseRotors(ArrayList<Integer> validInUseRotors) {
        for (Integer value : validInUseRotors)
            this.chosenValidInUseRotors.add(new Integer(value));
    }

    public int getRotorsStartPosPermutation() {
        return rotorsStartPosPermutation;
    }

    public void setRotorsStartPosPermutation(int rotorsStartPosPermutation) {
        this.rotorsStartPosPermutation = rotorsStartPosPermutation;
    }

    public RomanNumbers getChosenValidReflector() {
        return chosenValidReflector;
    }

    public void setChosenValidReflector(RomanNumbers chosenValidReflector) {
        this.chosenValidReflector = chosenValidReflector;
    }
}
