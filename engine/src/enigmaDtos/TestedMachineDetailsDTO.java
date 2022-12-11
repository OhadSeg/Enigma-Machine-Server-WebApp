package enigmaDtos;

import java.io.Serializable;
import java.util.ArrayList;
import utils.RomanNumbers;
import javafx.util.Pair;

public class TestedMachineDetailsDTO implements Serializable {

    private ArrayList<Integer> chosenValidInUseRotors = new ArrayList<>();
    private ArrayList<Character> chosenValidStartPosRotors = new ArrayList<>();
    private RomanNumbers chosenValidReflector;
    private ArrayList<Pair> chosenPlugs = new ArrayList<>();

    private ArrayList<Character> rotorsWindowDynamicPosition = new ArrayList<>();

    private ArrayList<Integer> notchesDynamicPosition = new ArrayList<>();

    public TestedMachineDetailsDTO() {}

    public TestedMachineDetailsDTO(TestedMachineDetailsDTO currConfiguration) {
        setChosenValidInUseRotors(currConfiguration.getChosenValidInUseRotors());
        setChosenValidStartPosRotors(currConfiguration.getChosenValidStartPosRotors());
        setChosenValidReflector(currConfiguration.getChosenValidReflector());
        setChosenPlugs(currConfiguration.getChosenValidPlugs());
    }

    public ArrayList<Integer> getChosenValidInUseRotors() {
        return chosenValidInUseRotors;
    }

    public void setChosenValidInUseRotors(ArrayList<Integer> validInUseRotors) {
        for (Integer value : validInUseRotors)
            this.chosenValidInUseRotors.add(new Integer(value));
    }

    public ArrayList<Character> getChosenValidStartPosRotors() {
        return chosenValidStartPosRotors;
    }

    public void setChosenValidStartPosRotors(ArrayList<Character> chosenValidStartPosRotors) {
        for (Character value : chosenValidStartPosRotors) {
            this.chosenValidStartPosRotors.add(new Character(value));
        }
    }

    public RomanNumbers getChosenValidReflector() {
        return chosenValidReflector;
    }

    public void setChosenValidReflector(RomanNumbers chosenValidReflector) {
        this.chosenValidReflector = chosenValidReflector;
    }

    public ArrayList<Pair> getChosenValidPlugs() {
        return chosenPlugs;
    }

    public void setChosenPlugs(ArrayList<Pair> chosenPlugs) {
        for (Pair value : chosenPlugs) {
            this.chosenPlugs.add(value);
        }
    }

    public void setDynamicData(ArrayList<Character> rotorsWindowsPosition, ArrayList<Integer> notchesPositions) {
        rotorsWindowDynamicPosition.clear();
        notchesDynamicPosition.clear();
        rotorsWindowsPosition.forEach((ch) -> this.rotorsWindowDynamicPosition.add(new Character(ch)));
        notchesPositions.forEach((pos) -> this.notchesDynamicPosition.add(new Integer(pos)));
    }

    public void updateDynamicData(ArrayList<Character> rotorsWindowsPosition, ArrayList<Integer> notchesPositions) {
        for (int i = 0; i < rotorsWindowsPosition.size(); i++) {
            rotorsWindowDynamicPosition.set(i, rotorsWindowsPosition.get(i));
            notchesDynamicPosition.set(i, notchesPositions.get(i));
        }
    }

    public ArrayList<Character> getRotorsWindowDynamicPosition() {
        return rotorsWindowDynamicPosition;
    }

    public ArrayList<Integer> getNotchesDynamicPosition() {
        return notchesDynamicPosition;
    }

    public void addToRotorsInUse(Integer rotorID) {
        chosenValidInUseRotors.add(rotorID);
    }

    public void addToStartingPos(Character startingPos) {
        chosenValidStartPosRotors.add(startingPos);
    }

    public void addToPlugBoard(Pair pair) {
        chosenPlugs.add(pair);
    }
}