package enigmaDtos;

import utils.RomanNumbers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FileDetailsDTO implements Serializable {

    int numOfReflectors;
    String abc;
    int amountOfAllRotors;
    int amountOfAllRotorsInUse;
    Set<String> dictionary = new HashSet<>();
    String excludeChars = null;
    private ArrayList<Integer> rotorsInUse = new ArrayList<>();
    private ArrayList<Character> rotorsInitialPositionsInUse = new ArrayList<>();
    private ArrayList<Integer> notchesPositionInUse = new ArrayList<>();
    private RomanNumbers reflectorInUse;
    private String textBeforeCode = null;

    private String textAfterCode = null;

    public FileDetailsDTO (){}
    public FileDetailsDTO (int FileNumOfReflectors, String FileAbc, int FileAmountOfAllRotors, int FileAmountOfAllRotorsInUse, Set<String> dictionary,
                           ArrayList<Integer> rotorsFromUser, ArrayList<Character> rotorsInitalPosFromUser, RomanNumbers reflectorFromUser,
                           ArrayList<Integer> notchesPositionFromUser){
        this.numOfReflectors = FileNumOfReflectors;
        this.abc = FileAbc;
        this.amountOfAllRotors = FileAmountOfAllRotors;
        this.amountOfAllRotorsInUse = FileAmountOfAllRotorsInUse;
        this.dictionary = dictionary;

        this.rotorsInUse = rotorsFromUser;
        this.rotorsInitialPositionsInUse = rotorsInitalPosFromUser;
        this.notchesPositionInUse = notchesPositionFromUser;
        this.reflectorInUse = reflectorFromUser;
    }

    public int getAmountOfAllRotors() {
        return amountOfAllRotors;
    }

    public int getAmountOfAllRotorsInUse() {
        return amountOfAllRotorsInUse;
    }

    public int getNumOfReflectors() {
        return numOfReflectors;
    }

    public String getAbc() {
        return abc;
    }

    public Set<String> getDictionary(){
        return dictionary;
    }

    public ArrayList<Integer> getRotorsInUse() {
        return rotorsInUse;
    }

    public ArrayList<Integer> getNotchesPositionInUse() {
        return notchesPositionInUse;
    }

    public ArrayList<Character> getRotorsInitialPositions() {
        return rotorsInitialPositionsInUse;
    }

    public RomanNumbers getReflectorInUse() {
        return reflectorInUse;
    }
    public void addToStartingPos(Character startingPos) {
        rotorsInitialPositionsInUse.add(startingPos);
    }
    public void addToRotorsInUse(Integer rotorID) {
        rotorsInUse.add(rotorID);
    }
    public void setChosenValidReflector(RomanNumbers chosenValidReflector) {
        this.reflectorInUse = chosenValidReflector;
    }

    public void setNotchesPositionInUse(ArrayList<Integer> notchesPositionInUse) {
        this.notchesPositionInUse = notchesPositionInUse;
    }
    public void setChosenValidInUseRotors(ArrayList<Integer> validInUseRotors) {
        for (Integer value : validInUseRotors)
            this.rotorsInUse.add(new Integer(value));
    }
    public void setChosenValidStartPosRotors(ArrayList<Character> chosenValidStartPosRotors) {
        rotorsInitialPositionsInUse.clear();
        for (Character value : chosenValidStartPosRotors) {
            this.rotorsInitialPositionsInUse.add(new Character(value));
        }
    }

    public void setTextAfterCode(String textAfterCode){
        this.textAfterCode = textAfterCode;
    }

    public String getTextAfterCode() {
        return textAfterCode;
    }

    public String getTextBeforeCode() {
        return textBeforeCode;
    }

    public void setTextBeforeCode(String textBeforeCode) {
        this.textBeforeCode = textBeforeCode;
    }
    public void setExcludeChars(String excludeChars){
        this.excludeChars = excludeChars;
    }

    public String getExcludeChars() {
        return excludeChars;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    public void setAmountOfAllRotors(int amountOfAllRotors) {
        this.amountOfAllRotors = amountOfAllRotors;
    }

    public void setAmountOfAllRotorsInUse(int amountOfAllRotorsInUse) {
        this.amountOfAllRotorsInUse = amountOfAllRotorsInUse;
    }

    public void setNumOfReflectors(int numOfReflectors) {
        this.numOfReflectors = numOfReflectors;
    }
}
