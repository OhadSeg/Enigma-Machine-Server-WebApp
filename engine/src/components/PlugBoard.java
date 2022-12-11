package components;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlugBoard implements Serializable {

    private Map<Character, Character> chosenPlugs = new HashMap<>();
    private ArrayList<Pair> plugsFromUser = new ArrayList<>();

    protected void setPlugsBoardFromDto(ArrayList<Pair> chosenPlugsFromUser) {
        this.plugsFromUser = chosenPlugsFromUser;
        chosenPlugs = new HashMap<>();
        for (Pair currPair : chosenPlugsFromUser) {
            this.chosenPlugs.put((Character) currPair.getKey(), (Character) currPair.getValue());
            this.chosenPlugs.put((Character) currPair.getValue(), (Character) currPair.getKey());
        }
    }

    protected Character getMatchingPlugBoardChar(Character originalChar) {
        Character matchingChar = chosenPlugs.get(originalChar);
        if (matchingChar != null) {
            return matchingChar;
        } else {
            return originalChar;
        }
    }

    @Override
    public String toString() {
        StringBuilder plugBoardDetails = new StringBuilder();
        plugBoardDetails.append('<');
        for (int i = 0; i < plugsFromUser.size(); i++) {
            plugBoardDetails.append(plugsFromUser.get(i).getKey());
            plugBoardDetails.append('|');
            plugBoardDetails.append(plugsFromUser.get(i).getValue());
            if (i != plugsFromUser.size() - 1) {
                plugBoardDetails.append(',');
            }
        }
        plugBoardDetails.append('>');
        return plugBoardDetails.toString();
    }
}
