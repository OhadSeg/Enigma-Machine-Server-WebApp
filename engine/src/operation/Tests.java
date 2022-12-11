package operation;

import bruteForce.DecriptionManager;
import components.Enigma;
import enigmaDtos.BruteForceDetailsDTO;
import enigmaDtos.FileDetailsDTO;
import exceptions.MachineDetailsFromFileException;
import exceptions.MachineDetailsFromUserException;
import exceptions.UserInputException;
import javafx.util.Pair;
import utils.RomanNumbers;
import enigmaDtos.TestedMachineDetailsDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Tests {

    private boolean isEnigmaSetValid = false;

    public void runEnigmaSetTests(Enigma eTest) throws MachineDetailsFromFileException {
        StringBuilder xmlInputExceptionMessage = new StringBuilder();
        if (!isABCIsEven(eTest)) {
            xmlInputExceptionMessage.append("XML FILE ERROR 101 : The number of letters in the machine is not even");
            xmlInputExceptionMessage.append(System.lineSeparator());
        }
        if (!isAmountOfRotorsIsValid(eTest)) {
            xmlInputExceptionMessage.append("XML FILE ERROR 102 : The number of rotors selected is greater than the number of available rotors");
            xmlInputExceptionMessage.append(System.lineSeparator());
        }
        if (!isMinimumAmountOfRotors(eTest)) {
            xmlInputExceptionMessage.append("XML FILE ERROR 103 : The number of available rotors is less than 2");
            xmlInputExceptionMessage.append(System.lineSeparator());
        }
        if (!isAllRotorsDifferentValidId(eTest)) {
            xmlInputExceptionMessage.append("XML FILE ERROR 104 : There are rotors with the same ID or there is a rotor with an incorrect ID number");
            xmlInputExceptionMessage.append(System.lineSeparator());
        }
        if (!isAllRotorsMappingValid(eTest)) {
            xmlInputExceptionMessage.append("XML FILE ERROR 105 : There is double mapping/ missing mapping in one or more of the rotors.");
            xmlInputExceptionMessage.append(System.lineSeparator());
        }
        if (!isAllRotorsNotchesValid(eTest)) {
            xmlInputExceptionMessage.append("XML FILE ERROR 106 : There are inappropriate notches in the file");
            xmlInputExceptionMessage.append(System.lineSeparator());
        }
        if (!isAllReflectorsIdsValid(eTest)) {
            xmlInputExceptionMessage.append("XML FILE ERROR 107 : There are reflectors with an incorrect ID number");
            xmlInputExceptionMessage.append(System.lineSeparator());
        }
        if (!isAllReflectorsValid(eTest)) {
            xmlInputExceptionMessage.append("XML FILE ERROR 108 : There are incorrect reflectors");
            xmlInputExceptionMessage.append(System.lineSeparator());
        }
        if (xmlInputExceptionMessage.toString().length() != 0) {
            throw new MachineDetailsFromFileException(xmlInputExceptionMessage.toString());
        }
        isEnigmaSetValid = true;
    }

    public void runMachineDetailsTests(Enigma mEnigma, FileDetailsDTO fileDetailsDTO) throws MachineDetailsFromUserException {
        StringBuilder userInputExceptionMessage = new StringBuilder();

        if (!checkIfAllchosenRotorsAreDifferentFromEachOther(fileDetailsDTO.getRotorsInUse())) {
            userInputExceptionMessage.append("USER INPUT ERROR 201 : One or more rotors are selected multiple times.");
            userInputExceptionMessage.append(System.lineSeparator());
        }
        if (!checkIfAllRotorsAreSelected(fileDetailsDTO.getRotorsInUse(), mEnigma.getAmountOfRotorsInUse())) {
            userInputExceptionMessage.append("USER INPUT ERROR 202 : There are empty fields in the selection of rotors.");
            userInputExceptionMessage.append(System.lineSeparator());
        }
        if (!checkIfAllInitialPosAreSelected(fileDetailsDTO.getRotorsInitialPositions())) {
            userInputExceptionMessage.append("USER INPUT ERROR 203 : There are empty fields in the selection of rotors starting positions.");
            userInputExceptionMessage.append(System.lineSeparator());
        }
        if (!checkIfReflectorIsSelected(fileDetailsDTO.getReflectorInUse())) {
            userInputExceptionMessage.append("USER INPUT ERROR 204 : There are empty field in the selection of the reflector.");
            userInputExceptionMessage.append(System.lineSeparator());
        }
//        if (!checkIfPlugBoardIsValid(fileDetailsDTO.getChosenValidPlugs())) {
//            userInputExceptionMessage.append("USER INPUT ERROR 205 : There is an unpaired letter.");
//            userInputExceptionMessage.append(System.lineSeparator());
//        }
        if (userInputExceptionMessage.toString().length() != 0) {
            throw new MachineDetailsFromUserException(userInputExceptionMessage.toString());

//        if (!isAllRotorsInitialPosValid(machineDetailsDto.getRotorsInitialPositions(), filteredMachineDetails, mEnigma)) {
//            userInputExceptionMessage.append("USER INPUT ERROR 202 : Rotors initial positions not valid");
//            userInputExceptionMessage.append(System.lineSeparator());
//        }
//        if (!isChosenReflectorValid(machineDetailsDto.getReflectorInUse(), filteredMachineDetails, mEnigma)) {
//            userInputExceptionMessage.append("USER INPUT ERROR 203 : Reflector not valid");
//            userInputExceptionMessage.append(System.lineSeparator());
//        }
//        if (!isAllPlugsChosenValid(machineDetailsDto.getPlugsBoardInUse(), filteredMachineDetails, mEnigma)) {
//            userInputExceptionMessage.append("USER INPUT ERROR 204 : Plugs board not valid");
//            userInputExceptionMessage.append(System.lineSeparator());
//        }
//        if (!isAllChosenRotorsValid(machineDetailsDto.getRotorsInUse(), filteredMachineDetails, mEnigma)) {
//            userInputExceptionMessage.append("USER INPUT ERROR 201 : Rotors not valid");
//            userInputExceptionMessage.append(System.lineSeparator());
//        }
        }
    }

    public void runTextToCodeTests(String textToCode, Enigma mEnigma) throws UserInputException {
        StringBuilder userInputExceptionMessage = new StringBuilder();

        if (!checkIfTextToCodeFromUserInDictionary(textToCode, mEnigma)) {
            userInputExceptionMessage.append("USER INPUT ERROR 501 : The entered text does not in the dictionary");
        }
        if (userInputExceptionMessage.toString().length() != 0) {
            throw new UserInputException(userInputExceptionMessage.toString());
        }
    }
    protected void runBruteForceTests(String textToCode, Enigma mEnigma,DecriptionManager mDecriptionManager) throws UserInputException {
        StringBuilder userInputExceptionMessage = new StringBuilder();
        if (!checkIfTextToCodeFromUserValid(textToCode, mEnigma)) {
            userInputExceptionMessage.append("USER INPUT ERROR 501 : The entered text does not match the machine language");
            userInputExceptionMessage.append(System.lineSeparator());
        }
        if(!checkIfAllWordsInDictionary(textToCode,mEnigma,mDecriptionManager)){
            userInputExceptionMessage.append("USER INPUT ERROR 502 : One or more words from the text are not appearing in the dictionary");
            userInputExceptionMessage.append(System.lineSeparator());
        }
        if (userInputExceptionMessage.toString().length() != 0) {
            throw new UserInputException(userInputExceptionMessage.toString());
        }
    }
    public void runBruteForceDetailsFromUserTest(BruteForceDetailsDTO bfDetails)throws UserInputException {
        StringBuilder userInputExceptionMessage = new StringBuilder();

        if(bfDetails.getAgents()==null){
            userInputExceptionMessage.append("USER INPUT ERROR 601 : The agents field is empty");
            userInputExceptionMessage.append(System.lineSeparator());
        }
        if(bfDetails.getLevel()==null){
            userInputExceptionMessage.append("USER INPUT ERROR 602 : The difficulty level field is empty");
            userInputExceptionMessage.append(System.lineSeparator());
        }
        if (userInputExceptionMessage.toString().length() != 0) {
            throw new UserInputException(userInputExceptionMessage.toString());
        }
    }
    private boolean isABCIsEven(Enigma eTest) {
        String ABC = eTest.getAbc();
        return ABC.length() % 2 == 0;
    }

    private boolean isAmountOfRotorsIsValid(Enigma eTest) {
        int countRotors = eTest.getAmountOfRotorsInUse();
        int amountOfAvailableRotors = eTest.getAmountOfAllRotors();
        return countRotors <= amountOfAvailableRotors;
    }

    protected boolean isMinimumAmountOfRotors(Enigma eTest) {
        return eTest.getAmountOfAllRotors() >= 2 && eTest.getAmountOfRotorsInUse() >= 2;
    }

    private boolean isAllRotorsDifferentValidId(Enigma eTest) {    // there is a chance in which there were identical id rotos, and when we put the second in map he deleted the other
        int countXmlRotors = eTest.getAmountOfAllRotorsInXml();
        int countMapEnigmaRotors = eTest.getAmountOfAllRotors();
        boolean isValid = eTest.getMachine().getRotors().checkIfAllRotorsIdsValid();
        return countXmlRotors == countMapEnigmaRotors && isValid;
    }

    private boolean isAllRotorsNotchesValid(Enigma eTest) {
        return eTest.getMachine().getRotors().checkIfAllRotorsNotchValid();
    }

    private boolean isAllReflectorsIdsValid(Enigma eTest) {
        return eTest.getMachine().getReflectors().checkIfAllReflectorsIdsValid();
    }

    private boolean isAllReflectorsValid(Enigma eTest) {
        return eTest.checkIfAllReflectorsValid();
    }

    private boolean isAllRotorsMappingValid(Enigma eTest) {
        return eTest.getMachine().getRotors().checkIfAllRotorsMappingValid(eTest.getAbc().length());
    }

    protected boolean getEnigmaSetValid() {
        return isEnigmaSetValid;
    }

//    private boolean isAllChosenRotorsValid(String rotorsInput, TestedMachineDetailsDTO filteredMachineDetails, Enigma mEnigma) {
//
//        ArrayList<Integer> chosenRotors = new ArrayList<>();
//        try {
//            int[] tempChosenRotors = Arrays.stream(rotorsInput.split(",")).mapToInt(Integer::parseInt).toArray();
//            for (Integer value : tempChosenRotors) {
//                chosenRotors.add(new Integer(value));
//            }
//            if(chosenRotors.stream().distinct().count() != chosenRotors.size()){
//                return false;
//            }
//
//            for (int index : chosenRotors) {
//                if (index < 1 || index > mEnigma.getMachine().getRotors().getAmountOfAllRotors()) {
//                    return false;
//                }
//            }
//            if (chosenRotors.size() != mEnigma.getAmountOfRotorsInUse()) {
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            return false;
//        }
//        filteredMachineDetails.setChosenValidInUseRotors(chosenRotors);
//        return true;
//    }

//    private boolean isChosenReflectorValid(String chosenReflectorInputStr, TestedMachineDetailsDTO filteredMachineDetails, Enigma mEnigma) {
//        try {
//            Integer chosenReflectorInput = Integer.parseInt(chosenReflectorInputStr);
//            if (chosenReflectorInput < 1 || chosenReflectorInput > mEnigma.getAmountOfAllReflectors()) {
//                return false;
//            }
//            filteredMachineDetails.setChosenValidReflector(RomanNumbers.values()[chosenReflectorInput - 1]);
//            return true;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }

//    private boolean isAllRotorsInitialPosValid(String rotorsInitialPosInput, TestedMachineDetailsDTO filteredMachineDetails, Enigma mEnigma) {
//
//        ArrayList<Character> RotorsInitialPosFromUser = new ArrayList<>();
//        for (int i = 0; i < rotorsInitialPosInput.length(); i++) {
//            RotorsInitialPosFromUser.add(rotorsInitialPosInput.charAt(i));
//        }
//
//        String abcFromEnigma = mEnigma.getAbc();
//
//        if (RotorsInitialPosFromUser.size() != mEnigma.getAmountOfRotorsInUse()) {
//            return false;
//        }
//        for (char ch : RotorsInitialPosFromUser) {
//            if (abcFromEnigma.indexOf(ch) == -1) {
//                return false;
//            }
//        }
//        filteredMachineDetails.setChosenValidStartPosRotors(RotorsInitialPosFromUser);
//        return true;
//    }

//    private boolean isAllPlugsChosenValid(String plugsInput, TestedMachineDetailsDTO filteredMachineDetails, Enigma mEnigma) {
//
//        ArrayList<Pair> plugsFromUser = new ArrayList<Pair>();
//        String abcFromEnigma = mEnigma.getAbc();
//
//        if (plugsInput.length() % 2 != 0 || plugsInput.length() > abcFromEnigma.length()) {
//            return false;
//        } else {
//            if (!checkIfAllCharacterInPlugsInputAreDifferent(plugsInput)) {
//                return false;
//            } else {
//                for (int i = 0; i < plugsInput.length(); i++) {
//                    if (abcFromEnigma.indexOf(plugsInput.charAt(i)) == -1) {
//                        return false;
//                    }
//                }
//            }
//            for (int i = 0; i < plugsInput.length() - 1; i += 2) {
//                plugsFromUser.add(new Pair(plugsInput.charAt(i), plugsInput.charAt(i + 1)));
//            }
//        }
//        filteredMachineDetails.setChosenPlugs(plugsFromUser);
//        return true;
//    }

//    private boolean checkIfAllCharacterInPlugsInputAreDifferent(String plugsInput) {
//        for (int i = 0; i < plugsInput.length(); i++) {
//            Character charToCompare = plugsInput.charAt(i);
//            for (int j = i + 1; j < plugsInput.length(); j++) {
//                if (charToCompare == plugsInput.charAt(j)) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

    private boolean checkIfAllchosenRotorsAreDifferentFromEachOther(ArrayList<Integer> chosenRotorsFromUser) {
        Set<Integer> chosenRotorsSizeChecker = new HashSet<>();
        chosenRotorsFromUser.forEach((rotorID) -> chosenRotorsSizeChecker.add(rotorID));
        return chosenRotorsFromUser.size() == chosenRotorsSizeChecker.size();
    }

    private boolean checkIfAllRotorsAreSelected(ArrayList<Integer> chosenRotorsFromUser, Integer numberOfRotorsNeeded) {
        for(Integer rotorID : chosenRotorsFromUser){
            if(rotorID == null){
                return false;
            }
        }
        return true;
    }

    private boolean checkIfAllInitialPosAreSelected(ArrayList<Character> initialPosFromUser) {
        for (Character startingPos : initialPosFromUser) {
            if (startingPos == null) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfReflectorIsSelected(RomanNumbers reflectorFromUser) {
        return reflectorFromUser != null;
    }

    private boolean checkIfPlugBoardIsValid(ArrayList<Pair> plugBoardFromUser) {
        for (Pair plugPair : plugBoardFromUser) {
            if (plugPair == null) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfTextToCodeFromUserValid(String textToCode, Enigma mEnigma) {
        String abcFromEnigma = mEnigma.getAbc();
        for (int i = 0; i < textToCode.length(); i++) {
            if (abcFromEnigma.indexOf(textToCode.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean checkIfTextToCodeFromUserInDictionary(String textToCode, Enigma mEnigma){
        Set<String> dictionary = mEnigma.getDictionary();
        if(dictionary.contains(textToCode)){
            return true;
        }
        return false;
    }

    private boolean checkIfAllWordsInDictionary(String textToCode, Enigma mEnigma,DecriptionManager mDecriptionManager){
        ArrayList<String> words= new ArrayList<>(Arrays.asList(textToCode.split("\\s+")));
        for(String word : words){
            if(!mDecriptionManager.isWordInDictionary(word.toLowerCase())){
                return false;
            }
        }
        return true;
    }
}
