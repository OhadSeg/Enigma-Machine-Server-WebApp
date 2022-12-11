package components;

import generated.CTERotor;
import generated.CTERotors;
import java.io.Serializable;
import java.util.*;
import utils.Direction;
public class Rotors implements Serializable {

    private Map<Integer, Rotor> rotors = new HashMap<>();
    private List<Integer> chosenRotorsOrder = new ArrayList();
    private List<Character> rotorsStartingPos = new ArrayList();
    private int amountOfAllRotorsInXml;
    private int amountOfAllRotors;

    public Rotors(CTERotors cteRotors) {

        List<CTERotor> cteRotorsList = cteRotors.getCTERotor();

        for (CTERotor cteRotor : cteRotorsList) {
            Rotor rotor = new Rotor(cteRotor);
            rotors.put(rotor.getId(), rotor);
            amountOfAllRotorsInXml++;
        }
        amountOfAllRotors = rotors.size();
    }

    public int getAmountOfAllRotors() {
        return amountOfAllRotors;
    }

    protected int getAmountOfAllRotorsInXml() {
        return amountOfAllRotorsInXml;
    }

    protected void setChosenRotorsOrderFromDto(ArrayList<Integer> chosenRotorsFromDto) {
        chosenRotorsOrder = new ArrayList<>();
        for (Integer value : chosenRotorsFromDto) {
            chosenRotorsOrder.add(new Integer(value));
        }
    }

    protected void setChosenRotorsStartingPosFromDto(ArrayList<Character> chosenRotorsStrartingPosFromDto) {
        rotorsStartingPos = new ArrayList<>();
        for (Character value : chosenRotorsStrartingPosFromDto) {
            rotorsStartingPos.add(new Character(value));
        }
        setStartingPointOfRotors();
    }

    protected void setStartingPointOfRotors() {
        for (int i = 0; i < rotorsStartingPos.size(); i++) {
            rotors.get(chosenRotorsOrder.get(i)).setIndexOfWindow(rotorsStartingPos.get(i));
        }
    }

    protected void setStartingPointOfRotorsFromBF(ArrayList<Integer> startingPos) {
        for (int i = 0; i < rotorsStartingPos.size(); i++) {
            rotors.get(chosenRotorsOrder.get(i)).setIndexOfWindowFromBF(startingPos.get(i));
        }
    }

    protected int run(int entryToFirstRotor, Direction directionInRotors) {

        if (directionInRotors == Direction.RIGHTTOLEFT) {
            advancingRotors();
        }
        int currEntry = entryToFirstRotor;
        if (directionInRotors == Direction.RIGHTTOLEFT) {

            for (int i = chosenRotorsOrder.size() - 1; i >= 0; i--) {
                Rotor currRotor = this.rotors.get(chosenRotorsOrder.get(i));
                int nextEntry = currRotor.run(currEntry, directionInRotors);
                currEntry = nextEntry;
            }
        } else {
            for (int i = 0; i < chosenRotorsOrder.size(); i++) {

                Rotor currRotor = this.rotors.get(chosenRotorsOrder.get(i));
                int nextEntry = currRotor.run(currEntry, directionInRotors);
                currEntry = nextEntry;
            }
        }
        return currEntry;
    }

    protected void advancingRotors() {
        for (int i = chosenRotorsOrder.size() - 1; i >= 0; i--) {

            Rotor currRotor = this.rotors.get(chosenRotorsOrder.get(i));
            // if on right rotor
            if (i == chosenRotorsOrder.size() - 1) {
                currRotor.advancingRotor();
            } else {
                Rotor prevRotor = this.rotors.get(chosenRotorsOrder.get(i + 1));
                if (prevRotor.isIfToAdvanceNextRotor() == true) {
                    currRotor.advancingRotor();
                }
            }
        }
    }

    public boolean checkIfAllRotorsIdsValid() {

        for (Map.Entry<Integer, Rotor> rotor : rotors.entrySet()) {
            if (rotor.getKey() < 1 || rotor.getKey() > amountOfAllRotors) {
                return false;
            }
        }
        return true;
    }

    public boolean checkIfAllRotorsNotchValid() {
        for (Map.Entry<Integer, Rotor> rotor : rotors.entrySet()) {
            if (rotor.getValue().validNotchCheck() == false) {
                return false;
            }
        }
        return true;
    }

    public boolean checkIfAllRotorsMappingValid(int abcLength) {
        for (Map.Entry<Integer, Rotor> rotor : rotors.entrySet()) {
            if (rotor.getValue().checkDoubleMapping() == false) {
                return false;
            }
            if(rotor.getValue().getSizeOfRotor() != abcLength){
                return false;
            }
        }
        return true;
    }

    protected ArrayList<Integer> getAllRotorsNotches() {
        ArrayList<Integer> rotorsNotches = new ArrayList<>();
        for (Integer rotorInUseIndex : chosenRotorsOrder) {
            rotorsNotches.add(rotors.get(rotorInUseIndex).getNotch());
        }
        return rotorsNotches;
    }

    public ArrayList<Character> getRotorsWindowPosition(){
        ArrayList<Character> toReturn = new ArrayList<>();
        for(Integer rotorID : chosenRotorsOrder){
            toReturn.add(rotors.get(rotorID).GetLetterInWindow());
        }
        return toReturn;
    }

    public ArrayList<Integer> getNotchesPositionFromWindow(){
        ArrayList<Integer> toReturn = new ArrayList<>();
        for(Integer rotorID : chosenRotorsOrder){
            toReturn.add(rotors.get(rotorID).getNotchDistanceFromStartingPos());
        }
        return toReturn;
    }

    public ArrayList<Integer> getAllInitialNotchPos(){
        ArrayList<Integer> toReturn = new ArrayList<>();
        for(Integer rotorID : chosenRotorsOrder) {
            toReturn.add(rotors.get(rotorID).getInitialNotchPos());
        }
        return toReturn;
    }

    @Override
    public String toString() {
        StringBuilder rotorsDetails = new StringBuilder();
        //For Rotor ID and Notch
        rotorsDetails.append('<');
        for (int i = 0; i < chosenRotorsOrder.size(); i++) {
            rotorsDetails.append(chosenRotorsOrder.get(i));
            if (i != chosenRotorsOrder.size() - 1) {
                rotorsDetails.append(',');
            }
        }
        rotorsDetails.append('>');
        //For Starting position
        ArrayList<Character> RotorsWindowPosition = getRotorsWindowPosition();
        ArrayList<Integer> NotchesPositionFromWindow = getNotchesPositionFromWindow();
        rotorsDetails.append('<');
        for (int i = 0; i < chosenRotorsOrder.size(); i++) {
            rotorsDetails.append(RotorsWindowPosition.get(i) + "(");
            rotorsDetails.append(NotchesPositionFromWindow.get(i)+")");
        }
        for (Character value : RotorsWindowPosition) {
            rotorsDetails.append(value);
        }
        rotorsDetails.append('>');
        return rotorsDetails.toString();
    }
}
