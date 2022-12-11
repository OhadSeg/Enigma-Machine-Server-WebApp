package components;

import enigmaDtos.BruteForceConfigDTO;
import enigmaDtos.FileDetailsDTO;
import generated.CTEEnigma;
import enigmaDtos.TestedMachineDetailsDTO;
import javafx.util.Pair;
import utils.RomanNumbers;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Enigma implements Serializable {

    private Machine machine;
    private FileDetailsDTO configurationDTO;

    private String excludeChars;
    private Set<String> dictionary;


    public Enigma(CTEEnigma cteEnigma) {
        this.machine = new Machine(cteEnigma.getCTEMachine());
        excludeChars = cteEnigma.getCTEDecipher().getCTEDictionary().getExcludeChars();
        String dictionaryStr = cteEnigma.getCTEDecipher().getCTEDictionary().getWords();
        String dictionaryStrAfterRemoving = removeExcludeCharsFromDictionary(dictionaryStr);
        dictionary = new HashSet<>(Arrays.asList(dictionaryStrAfterRemoving.split("\\s+")));
        dictionary.remove("");
    }

    public Machine getMachine() {
        return machine;
    }


    public void setEnigmaMachine(FileDetailsDTO configuration) {
        this.configurationDTO = configuration;
        this.configurationDTO.setAbc(machine.getAbc());
        this.configurationDTO.setAmountOfAllRotorsInUse(machine.getCountRotors());
        this.configurationDTO.setNumOfReflectors(machine.getAmountOfAllReflectors());
        machine.insertMachineDetails(configuration);
    }

    public FileDetailsDTO getConfigurationAsDTO() {
        return configurationDTO;
    }

    public void setRotorsStartingPosBF(ArrayList<Integer> startingPos) {
        machine.setStartingPointOfRotorsFromBF(startingPos);
    }

    public void setConfigurationBF(ArrayList<Integer> startingPos, RomanNumbers reflector) {
        machine.setStartingPointOfRotorsFromBF(startingPos);
        machine.setReflector(reflector);
    }

    public void reset() {
        machine.reset();
        configurationDTO.setChosenValidStartPosRotors(machine.getRotors().getRotorsWindowPosition());
        configurationDTO.setNotchesPositionInUse(machine.getRotors().getNotchesPositionFromWindow());
    }

    public ArrayList<Integer> getAllRotorsNotches() {
        return machine.getAllRotorsNotches();
    }

    //להפוך את הפונקציה ל-void
    public Pair execute(String textToCode) {
        configurationDTO.setTextBeforeCode(textToCode);
        long begin = System.nanoTime();
        String textAfterCode = machine.startCodeText(textToCode);
        long end = System.nanoTime();
        Long timeExecute = new Long(end - begin);
        Pair codedTextAndTime = new Pair<>(textAfterCode, timeExecute);
        configurationDTO.setChosenValidStartPosRotors(machine.getRotors().getRotorsWindowPosition());
        configurationDTO.setNotchesPositionInUse(machine.getRotors().getNotchesPositionFromWindow());
        configurationDTO.setTextAfterCode(codedTextAndTime.getKey().toString());
        return codedTextAndTime;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public int getAmountOfAllReflectors() {
        return machine.getAmountOfAllReflectors();
    }

    public int getAmountOfAllRotors() {
        return machine.getAmountOfAllRotors();
    }

    public int getAmountOfRotorsInUse() {
        return machine.getCountRotors();
    }

    public int getAmountOfAllRotorsInXml() {
        return machine.getAmountOfAllRotorsInXml();
    }

    public String getAbc() {
        return machine.getAbc();
    }

    public boolean checkIfAllReflectorsValid() {
        return machine.checkIfAllReflectorsValid();
    }

    public Enigma deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(this);
            byte[] serializedObject1 = bos.toByteArray();
            os.close();

            ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject1);
            ObjectInputStream oInputStream = new ObjectInputStream(bis);
            Enigma enigmaClone = (Enigma) oInputStream.readObject();
            oInputStream.close();
            return enigmaClone;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return machine.toString();
    }

    public String removeExcludeCharsFromDictionary(String textStr) {
        StringBuilder dictionaryStrAfter = new StringBuilder();
        for (int i = 0; i < textStr.length(); i++) {
            if (excludeChars.indexOf(textStr.charAt(i)) == -1) {
                dictionaryStrAfter.append(textStr.charAt(i));
            }
        }
        return dictionaryStrAfter.toString();
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    public String getExcludeChars() {
        return excludeChars;
    }
}