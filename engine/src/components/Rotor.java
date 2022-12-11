package components;

import generated.CTEPositioning;
import generated.CTERotor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import utils.Direction;

public class Rotor implements Serializable {

    private List<Character> leftPartOfTheRotor = new ArrayList();
    private List<Character> rightPartOfTheRotor = new ArrayList();
    private boolean ifToAdvanceNextRotor;
    private int indexOfWindow;
    private int notch;
    private int id;

    public Rotor(CTERotor cteRotor) {

        for (CTEPositioning pos : cteRotor.getCTEPositioning()) {
            leftPartOfTheRotor.add(pos.getLeft().toUpperCase().charAt(0));
            rightPartOfTheRotor.add(pos.getRight().toUpperCase().charAt(0));
        }
        notch = cteRotor.getNotch() - 1;
        id = cteRotor.getId();
        indexOfWindow = 0;
    }

    protected int getNotch() {
        return notch;
    }

    protected int getId() {
        return id;
    }

    protected void setIndexOfWindow(Character startingPosition) {

        int indexOfWindow = 0;

        for (Character ch : rightPartOfTheRotor) {
            if (!startingPosition.equals(ch)) {
                indexOfWindow++;
            } else {
                break;
            }
        }
        this.indexOfWindow = indexOfWindow;
    }

    protected void setIndexOfWindowFromBF(int indexOfWindow){
        this.indexOfWindow = indexOfWindow;
    }

    private void checkIfToAdvanceNextRotor() {
        this.ifToAdvanceNextRotor = (this.indexOfWindow == this.notch);
    }

    protected void advancingRotor() {
        this.indexOfWindow = (this.indexOfWindow + 1) % leftPartOfTheRotor.size(); // no importance to left or right + if past rotor size, return window to start
        checkIfToAdvanceNextRotor();
    }

    protected boolean isIfToAdvanceNextRotor() {
        return ifToAdvanceNextRotor;
    }

    protected int run(int entryToRotor, Direction directionInRotor) {

        int indexOfMatchingCharInRotor;
        if (directionInRotor == Direction.RIGHTTOLEFT) {

            char entryChar = rightPartOfTheRotor.get((indexOfWindow + entryToRotor) % rightPartOfTheRotor.size()); // -1 may be needed for accuration
            indexOfMatchingCharInRotor = leftPartOfTheRotor.indexOf(entryChar);
        } else {
            char entryChar = leftPartOfTheRotor.get((indexOfWindow + entryToRotor) % leftPartOfTheRotor.size()); // -1 may be needed for accuration
            indexOfMatchingCharInRotor = rightPartOfTheRotor.indexOf(entryChar);
        }
        return Math.floorMod(indexOfMatchingCharInRotor - this.indexOfWindow, rightPartOfTheRotor.size());
    }

    protected boolean checkDoubleMapping() {
        Set<Character> leftPartChecker = new HashSet<Character>(leftPartOfTheRotor);
        Set<Character> rightPartChecker = new HashSet<Character>(rightPartOfTheRotor);
        return leftPartChecker.size() == leftPartOfTheRotor.size() && rightPartChecker.size() == rightPartOfTheRotor.size();
    }

    protected boolean validNotchCheck() {
        return notch >= 0 && notch < leftPartOfTheRotor.size();
    }

    public int getSizeOfRotor(){
        return leftPartOfTheRotor.size();
    }

//    @Override
//    public String toString() {
//        return String.valueOf(id) + "(" + String.valueOf(notch + 1) + ")";
//    }
    public Integer getNotchDistanceFromStartingPos(){
        return Math.floorMod(notch-indexOfWindow,leftPartOfTheRotor.size());
    }

    public Integer getInitialNotchPos() {
        return notch + 1;
    }

    public Character GetLetterInWindow(){
        return rightPartOfTheRotor.get(indexOfWindow);
    }
}