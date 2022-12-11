package components.thirdTab;

import components.primary.AppController;
import components.currConfiguration.CurrConfiComponentController;
import enigmaDtos.BruteForceDetailsDTO;
import enigmaDtos.CandidateDTO;
import enigmaDtos.TestedMachineDetailsDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import utils.BattlefieldLevel;
import utils.EncryptionMode;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ThirdTabComponentController {
    @FXML
    private CurrConfiComponentController currConfiComponentController;
    private ArrayList<String> dictionary;
    @FXML
    private ListView<String> dictionaryLV;

    @FXML
    private TextField seachInDictionaryTF;

    private AppController mainController;

    @FXML
    private TextArea textInputTA;
    @FXML
    private TextArea outputTA;
    @FXML
    private ComboBox<Integer> agentsComb;
    @FXML
    private ComboBox<BattlefieldLevel> difficultyLevelComb;
    @FXML
    private Spinner<Integer> missionExtendSpinner;
    @FXML
    private ProgressBar bruteForcePB;
    @FXML
    private ListView<String> candidatesListView;
    @FXML
    private Button pauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button resumeButton;
    @FXML
    private Button processButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button startButton;

    public void initialize() {
        if (currConfiComponentController != null) {
            currConfiComponentController.setThirdTabComponentController(this);
        }
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000000, 100);
        missionExtendSpinner.setValueFactory(valueFactory);
        dictionary = new ArrayList<>();

        ArrayList<BattlefieldLevel> difficultyLevel = new ArrayList<>();
        difficultyLevel.add(BattlefieldLevel.EASY);
        difficultyLevel.add(BattlefieldLevel.MEDIUM);
        difficultyLevel.add(BattlefieldLevel.HARD);
        difficultyLevel.add(BattlefieldLevel.IMPOSSIBLE);
        ObservableList<BattlefieldLevel> difficultyLevelObservList = FXCollections.observableList(difficultyLevel);
        difficultyLevelComb.setItems(difficultyLevelObservList);
    }

    public void setWordsToDictionary(Set<String> dictionary) {
        candidatesListView.getItems().clear();
        this.dictionary.clear();
        dictionaryLV.getItems().clear();
        this.dictionary.addAll(dictionary);
        this.dictionaryLV.getItems().addAll(this.dictionary);

    }


    public void setAgentsComb(int maxAmountOfAgents) {
        ArrayList<Integer> amountOfAgents = new ArrayList<>();
        for (int i = 2; i <= maxAmountOfAgents; i++) {
            amountOfAgents.add(i);
        }
        ObservableList<Integer> amountOfAgentsObservList = FXCollections.observableList(amountOfAgents);
        agentsComb.setItems(amountOfAgentsObservList);
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void startButtonActionListener(ActionEvent event) {
        bruteForcePB.setProgress(0.0);
        startAndResumeFetchButtons();
        candidatesListView.getItems().clear();
        mainController.doBruteForce(new BruteForceDetailsDTO(agentsComb.getValue(), difficultyLevelComb.getValue(), missionExtendSpinner.getValue()));
    }

    public void updateCandidateList(CandidateDTO candidate) {
        Platform.runLater(() -> candidatesListView.getItems().add("Candidate: " + candidate.getCandidateWord() + "  Agent: " + "  Configuration:" + candidate.getConfiguration()));
    }

    public void updateCandidateProgressBar(double progress) {
        Platform.runLater(() -> bruteForcePB.setProgress(progress));
    }

    @FXML
    public void thirdTabProcessButtonActionListener(ActionEvent event) {
        outputTA.clear();
        startButton.setDisable(false);
        String output = mainController.doRunCode(textInputTA.getText(), EncryptionMode.BRUTEFORCE);
        outputTA.setText(output);
    }

    @FXML
    void onClickListViewAction(MouseEvent event) {
        if (textInputTA.getText().equals("")) {
            textInputTA.setText(dictionaryLV.getSelectionModel().getSelectedItem());
        } else {
            textInputTA.setText(textInputTA.getText() + " " + dictionaryLV.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void searchTextFieldActionListener(ActionEvent event) {
        dictionaryLV.getItems().clear();
        dictionaryLV.getItems().addAll(searchInList(seachInDictionaryTF.getText()));
    }

    private List<String> searchInList(String searchString) {
        List<String> searchStringArray = Arrays.asList(searchString.trim().split(" "));

        return dictionary.stream().filter(input -> {
            return searchStringArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }

    @FXML
    void clearButtonActionListener(ActionEvent event) {
        textInputTA.clear();
        outputTA.clear();
    }

    @FXML
    void pauseButtonActionListener(ActionEvent event) {
        stopButton.setDisable(false);
        resumeButton.setDisable(false);
        pauseButton.setDisable(false);
        startButton.setDisable(true);
        processButton.setDisable(true);
        clearButton.setDisable(true);
        resetButton.setDisable(true);
        mainController.doPause();
    }

    @FXML
    void resumeButtonActionListener(ActionEvent event) {
        startAndResumeFetchButtons();
        mainController.doResume();
    }

    @FXML
    void stopButtonActionListener(ActionEvent event) {
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        resumeButton.setDisable(true);
        startButton.setDisable(false);
        processButton.setDisable(false);
        clearButton.setDisable(false);
        resetButton.setDisable(false);
        mainController.doStop();
    }

    @FXML
    void resetButtonActionListener(ActionEvent event) {
        //mainController.doReset();
    }

    public void buildCurrConfiguration(TestedMachineDetailsDTO machineDetails) {
        //currConfiComponentController.buildNewCurrConfiguration(machineDetails);
    }

    public void cleanCurrConfiguration() {
        currConfiComponentController.cleanCurrConfiguration();
    }

    public void stepCurrConfiguration(TestedMachineDetailsDTO machineDetails) {
        //currConfiComponentController.stepCurrConfiguration(machineDetails);
    }

    public void clearAllComponents(){
        bruteForcePB.setProgress(0.0);
        seachInDictionaryTF.clear();
        textInputTA.clear();
        outputTA.clear();
        agentsComb.valueProperty().set(null);
        difficultyLevelComb.valueProperty().set(null);
        candidatesListView.getItems().clear();
    }

    public void machineDetailsLoaded() {
        processButton.setDisable(false);
        clearButton.setDisable(false);
        resetButton.setDisable(false);
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        resumeButton.setDisable(true);
        startButton.setDisable(true);
    }

    public void startAndResumeFetchButtons(){
        pauseButton.setDisable(false);
        stopButton.setDisable(false);
        resumeButton.setDisable(true);
        startButton.setDisable(true);
        processButton.setDisable(true);
        clearButton.setDisable(true);
        resetButton.setDisable(true);
    }
}