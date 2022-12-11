package enigmaDtos;

import javafx.util.Pair;

import java.util.ArrayList;

public class MachineExecutionDocumentationDTO {

    String configuration;
    private ArrayList<Pair<Pair<String, String>, Long>> cryptographicHistory = new ArrayList<>();

    public MachineExecutionDocumentationDTO(String configuration) {
        this.configuration = new String(configuration);
    }

    public String getConfiguration() {
        return configuration;
    }

    public void addNewCodingDataUnit(Pair<Pair<String, String>, Long> newCodingDataUnit) {
        cryptographicHistory.add(newCodingDataUnit);
    }


    @Override
    public String toString() {
        StringBuilder dtoDetails = new StringBuilder();
        dtoDetails.append(this.configuration);
        dtoDetails.append(System.lineSeparator());
        dtoDetails.append(System.lineSeparator());
        for (int i = 0; i < cryptographicHistory.size(); i++) {
            dtoDetails.append("     ");
            dtoDetails.append(i + 1 + ". ");
            dtoDetails.append("<" + cryptographicHistory.get(i).getKey().getKey() + ">");
            dtoDetails.append(" --> ");
            dtoDetails.append("<" + cryptographicHistory.get(i).getKey().getValue() + ">");
            dtoDetails.append(" (" + cryptographicHistory.get(i).getValue() + " nano-seconds)");
            dtoDetails.append(System.lineSeparator());
        }
        return dtoDetails.toString();

    }

}

