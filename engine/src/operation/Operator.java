package operation;

import bruteForce.DecriptionManager;
import bruteForce.SupportTask;
import components.Enigma;
import enigmaDtos.*;
import exceptions.MachineDetailsFromFileException;
import exceptions.MachineDetailsFromUserException;
import exceptions.ReadFileException;
import exceptions.UserInputException;
import generated.CTEEnigma;
import javafx.util.Pair;
import utils.EncryptionMode;
import utils.RomanNumbers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class Operator {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "generated";
    private Enigma enigmaTest;
    private Enigma enigma;
    private String xmlPath;
    private Tests tests = new Tests();
    private TestedMachineDetailsDTO currConfiguration;
    private int amountOfCodedMessages;
    private ArrayList<MachineExecutionDocumentationDTO> machineExeStatsAndHistory = new ArrayList<>();
    DecriptionManager decriptionManager;
    private Random rnd = new Random();

    public void setXmlPath(String inputXmlPath) throws MachineDetailsFromFileException, ReadFileException, JAXBException, FileNotFoundException {
        xmlPath = new String(inputXmlPath);
        setEnigma();
    }

    private void setCurrConfiguration(TestedMachineDetailsDTO currConfiguration) {
        this.currConfiguration = new TestedMachineDetailsDTO(currConfiguration);
    }

    public TestedMachineDetailsDTO getNewCurrConfiguration() {
        //updateDynamicCurrConfiguration();
        return currConfiguration;
    }

    private void setEnigma() throws MachineDetailsFromFileException, ReadFileException, JAXBException, FileNotFoundException {
        CTEEnigma enigma123;
        ifPathValid();
        InputStream inputStream = new FileInputStream(new File(xmlPath));
        enigma123 = desrializeFrom(inputStream);
        enigmaTest = new Enigma(enigma123);
        tests = new Tests();
        tests.runEnigmaSetTests(enigmaTest);
        if (tests.getEnigmaSetValid()) {
            enigma = new Enigma(enigma123);
            decriptionManager = new DecriptionManager(enigma123);
        }
    }

    private void ifPathValid() throws ReadFileException {
        if (!xmlPath.endsWith(".xml")) {
            throw new ReadFileException("ERROR: The path is not a XML file path type");
        }
        Path path = Paths.get(xmlPath);
        if (Files.notExists(path)) {
            throw new ReadFileException(("ERROR: file does not exist"));
        }
    }

    private static CTEEnigma desrializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

//    public void setMachineDetails(TestedMachineDetailsDTO dtoFromDesktopUI) throws MachineDetailsFromUserException {
//        tests.runMachineDetailsTests(enigma, dtoFromDesktopUI);
//        enigma.setEnigmaMachine(dtoFromDesktopUI);
//        currConfiguration = new TestedMachineDetailsDTO(dtoFromDesktopUI);
//        currConfiguration.setDynamicData(enigma.getMachine().getRotors().getRotorsWindowPosition(), enigma.getMachine().getRotors().getNotchesPositionFromWindow());
//        amountOfCodedMessages = 0;
//        MachineExecutionDocumentationDTO newMachineExecution = new MachineExecutionDocumentationDTO(enigma.toString());
//        this.machineExeStatsAndHistory.add(newMachineExecution);
//    }

    public Pair setTextToCode(String textToCode, EncryptionMode mode) throws UserInputException {
        Pair codedTextAndTime = null;
        String textToCodeAfterUpper = textToCode.toUpperCase();
        switch (mode) {
            case WHOLEWORD:
                tests.runTextToCodeTests(textToCodeAfterUpper, enigma);
                codedTextAndTime = enigma.execute(textToCodeAfterUpper);
                addCodedWordToStats(textToCodeAfterUpper, codedTextAndTime);
                break;
            case LETTERAFTERLETTER:
                tests.runTextToCodeTests(textToCodeAfterUpper, enigma);
                codedTextAndTime = enigma.execute(textToCodeAfterUpper);
                break;
            case BRUTEFORCE:
                textToCodeAfterUpper = decriptionManager.removeExcludeCharsFromText(textToCodeAfterUpper);// בודקים
                tests.runBruteForceTests(textToCodeAfterUpper, enigma, decriptionManager);
                codedTextAndTime = enigma.execute(textToCodeAfterUpper);// מפענחים
                decriptionManager.setTextToBruteForce((String) codedTextAndTime.getKey());// מכניסים את המילה לDM
                break;
        }
        updateDynamicCurrConfiguration();
        return codedTextAndTime;
    }

    public void addCodedWordToStats(String textToCodeAfterUpper, Pair codedTextAndTime) {
        amountOfCodedMessages++;
        Pair beforeAndAfterText = new Pair(textToCodeAfterUpper, codedTextAndTime.getKey().toString());
        Pair cryptographicHistoryDataUnit = new Pair(beforeAndAfterText, codedTextAndTime.getValue());
        this.machineExeStatsAndHistory.get(machineExeStatsAndHistory.size() - 1).addNewCodingDataUnit(cryptographicHistoryDataUnit);
    }

    public int getNumOfReflectorsFromEnigma() {
        return enigma.getAmountOfAllReflectors();
    }

    public void updateDynamicCurrConfiguration() {
        currConfiguration.updateDynamicData(enigma.getMachine().getRotors().getRotorsWindowPosition(), enigma.getMachine().getRotors().getNotchesPositionFromWindow());
    }

    public TestedMachineDetailsDTO getResetedConfiguration() {
        return currConfiguration;
    }

    public String getAbcFromEnigma() {
        return enigma.getAbc();
    }

    public int getMaxAmountOfAgents() {
        return decriptionManager.getMaxAmountOfAgents();
    }

    public int getAmountOfAllRotors() {
        return enigma.getAmountOfAllRotors();
    }

    public int getAmountOfAllRotorsInUse() {
        return enigma.getAmountOfRotorsInUse();
    }

    public void resetEnigma() {
        currConfiguration.updateDynamicData(currConfiguration.getChosenValidStartPosRotors(), enigma.getMachine().getRotors().getAllInitialNotchPos());
        enigma.reset();
    }

    public void randomMode() {
        TestedMachineDetailsDTO randomMachineDetailsDTO = new TestedMachineDetailsDTO();
        randomMachineDetailsDTO.setChosenValidInUseRotors(randomInUseRotors());
        randomMachineDetailsDTO.setChosenValidStartPosRotors(randomStartingPos());
        randomMachineDetailsDTO.setChosenValidReflector(randomReflectorInUse());
        randomMachineDetailsDTO.setChosenPlugs(randomPlugBoard());

        //enigma.setEnigmaMachine(randomMachineDetailsDTO);
        amountOfCodedMessages = 0;
        currConfiguration = new TestedMachineDetailsDTO(randomMachineDetailsDTO);
        currConfiguration.setDynamicData(enigma.getMachine().getRotors().getRotorsWindowPosition(), enigma.getMachine().getRotors().getNotchesPositionFromWindow());
        MachineExecutionDocumentationDTO newMachineExecution = new MachineExecutionDocumentationDTO(enigma.toString());
        this.machineExeStatsAndHistory.add(newMachineExecution);
    }

    private ArrayList<Integer> randomInUseRotors() {
        ArrayList<Integer> randomInUseToReturn = new ArrayList<>();
        ArrayList<Integer> randomFrom = new ArrayList<>();
        for (int i = 1; i <= enigma.getAmountOfAllRotors(); i++) {
            randomFrom.add(i);
        }
        Collections.shuffle(randomFrom);
        for (int i = 0; i < enigma.getAmountOfRotorsInUse(); i++) {
            randomInUseToReturn.add(randomFrom.get(i));
        }
        return randomInUseToReturn;
    }

    private ArrayList<Character> randomStartingPos() {
        ArrayList<Character> startingPosToReturn = new ArrayList<>();
        ArrayList<Character> randomFrom = settingAbcToRandom();
        for (int i = 0; i < enigma.getAmountOfRotorsInUse(); i++) {
            startingPosToReturn.add(randomFrom.get(i));
        }
        return startingPosToReturn;
    }

    private ArrayList<Character> settingAbcToRandom() {
        ArrayList<Character> randomFrom = new ArrayList<>();
        String abcStr = enigma.getAbc();
        for (int i = 0; i < abcStr.length(); i++) {
            randomFrom.add(abcStr.charAt(i));
        }
        Collections.shuffle(randomFrom);
        return randomFrom;
    }

    private RomanNumbers randomReflectorInUse() {
        int randInt = rnd.nextInt(enigma.getAmountOfAllReflectors());
        return RomanNumbers.values()[randInt];
    }

    private ArrayList<Pair> randomPlugBoard() {

        int rangeAmountOfPlugs = enigma.getAbc().length() / 2;
        int AmountOfPlugs = rnd.nextInt(rangeAmountOfPlugs + 1);
        ArrayList<Pair> plugBoardToReturn = new ArrayList<>();
        ArrayList<Character> randomFrom = settingAbcToRandom();
        for (int i = 0; i < AmountOfPlugs; i++) {
            plugBoardToReturn.add(new Pair(randomFrom.get(i), randomFrom.get(randomFrom.size() - 1 - i)));
        }
        return plugBoardToReturn;
    }

    // להעביר את הפונקציה הזו לUI
    public String getMachineDetails() {

        StringBuilder allDetails = new StringBuilder();
        allDetails.append("1. Possible amount of rotors / Amount of in-use rotors: ");
        allDetails.append(enigma.getAmountOfAllRotors() + " / " + enigma.getAmountOfRotorsInUse());
        allDetails.append(System.lineSeparator());
        allDetails.append("2. Amount of reflectors: " + enigma.getAmountOfAllReflectors());
        allDetails.append(System.lineSeparator());
        allDetails.append("Amount of messages coded by the machine: " + this.amountOfCodedMessages);
        allDetails.append(System.lineSeparator());
        allDetails.append("Original Configuration: ");
        if (this.machineExeStatsAndHistory.size() != 0) {
            allDetails.append(machineExeStatsAndHistory.get(0).getConfiguration());
        } else {
            allDetails.append("----/----");
        }
        allDetails.append(System.lineSeparator());
        allDetails.append("Current Configuration: ");
        if (this.machineExeStatsAndHistory.size() != 0) {
            allDetails.append(machineExeStatsAndHistory.get(machineExeStatsAndHistory.size() - 1).getConfiguration());
        } else {
            allDetails.append(" ----/----");
        }
        allDetails.append(System.lineSeparator());
        return allDetails.toString();
    }

    public String getConsoleCurrConfiguration() {
        return enigma.toString();
    }

    public void startBruteForce(BruteForceDetailsDTO bdDetails)throws UserInputException {
        tests.runBruteForceDetailsFromUserTest(bdDetails);
        decriptionManager.start(bdDetails, enigma);
    }

    public boolean isBruteForceStillRunning(){
        return !decriptionManager.checkIfFinish() || decriptionManager.thereIsCandidates();
    }

    public CandidateDTO getCandidateFromDM()throws InterruptedException{
        return decriptionManager.getCandidate();
    }

    public Set<String> getDictionary(){
        return decriptionManager.getDictionary();
    }

    public double getProgressFromDM(){
        return decriptionManager.getProgress();
    }

    public void doPause(){
        decriptionManager.doPause();
    }
    public void doResume(){
        decriptionManager.doResume();
    }
    public void doStop(){
        decriptionManager.doStop();
    }
}