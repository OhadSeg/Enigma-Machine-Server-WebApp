package components;

import enigmaDtos.FileDetailsDTO;
import generated.CTEMachine;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import utils.Direction;
import enigmaDtos.TestedMachineDetailsDTO;
import utils.RomanNumbers;

public class Machine implements Serializable {

    private String abc;
    private Map<Integer, Character> indexToChar = new HashMap<>();
    private Map<Character, Integer> charToIndex = new HashMap<>();
    private PlugBoard plugBoard = new PlugBoard();
    private Reflectors reflectors;
    private int countRotors;
    private Rotors rotors;

    public Machine(CTEMachine cteMachine) {

        this.rotors = new Rotors(cteMachine.getCTERotors());
        this.countRotors = cteMachine.getRotorsCount();
        this.reflectors = new Reflectors(cteMachine.getCTEReflectors());
        String strFromXml = cteMachine.getABC();
        abc = strFromXml.trim().toUpperCase();

        for (int i = 0; i < abc.length(); i++) {

            charToIndex.put(abc.charAt(i), i);
            indexToChar.put(i, abc.charAt(i));
        }
    }

    public Rotors getRotors() {
        return rotors;
    }

    public Reflectors getReflectors() {
        return reflectors;
    }

    protected void insertMachineDetails(FileDetailsDTO machineDetails) {
        rotors.setChosenRotorsOrderFromDto(machineDetails.getRotorsInUse());
        rotors.setChosenRotorsStartingPosFromDto(machineDetails.getRotorsInitialPositions());
        reflectors.setReflectorInUseFromDto(machineDetails.getReflectorInUse());
//        plugBoard.setPlugsBoardFromDto(machineDetails.getChosenValidPlugs());
    }

    protected ArrayList<Integer> getAllRotorsNotches() {
        return rotors.getNotchesPositionFromWindow();
    }

    protected String startCodeText(String textToCode) {
        String returnCodedText = new String();
        for (int i = 0; i < textToCode.length(); i++) {
            char charFromInput = textToCode.charAt(i);
            returnCodedText += run(charFromInput);
        }
        return returnCodedText;
    }

    private Character run(Character inputChar) {

        Direction currDirection = Direction.RIGHTTOLEFT;
        // PLUGBOARD Mapping
        Character flowChar = plugBoard.getMatchingPlugBoardChar(inputChar);
        int entryIdxToNextComponent = charToIndex.get(flowChar);
        // ROTORS Mapping
        entryIdxToNextComponent = rotors.run(entryIdxToNextComponent, currDirection);
        // Reflector Mapping
        entryIdxToNextComponent = reflectors.run(entryIdxToNextComponent);

        currDirection = Direction.LEFTTORIGHT;

        entryIdxToNextComponent = rotors.run(entryIdxToNextComponent, currDirection);

        Character returnChar = indexToChar.get(entryIdxToNextComponent);

        flowChar = plugBoard.getMatchingPlugBoardChar(returnChar);

        return flowChar;
    }

    protected String getAbc() {
        return abc;
    }

    protected int getCountRotors() {
        return countRotors;
    }

    protected void reset() {
        rotors.setStartingPointOfRotors();
    }

    protected int getAmountOfAllReflectors() {
        return reflectors.getTotalReflectors();
    }

    protected int getAmountOfAllRotors() {
        return rotors.getAmountOfAllRotors();
    }

    protected int getAmountOfAllRotorsInXml() {
        return rotors.getAmountOfAllRotorsInXml();
    }

    protected boolean checkIfAllReflectorsValid() {
        return reflectors.checkIfAllReflectorsValid(abc.length());
    }

    protected void setStartingPointOfRotorsFromBF(ArrayList<Integer> startingPos){
        rotors.setStartingPointOfRotorsFromBF(startingPos);
    }

    protected void setReflector(RomanNumbers reflector){
        reflectors.setReflectorInUse(reflector);
    }

    @Override
    public String toString() {
        StringBuilder machineDetails = new StringBuilder();
        machineDetails.append(rotors.toString());
        machineDetails.append(reflectors.toString());
        machineDetails.append(plugBoard.toString());
        return machineDetails.toString();
    }
}


