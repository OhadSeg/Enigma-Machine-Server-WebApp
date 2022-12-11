package components.secondTab;

import components.currConfiguration.CurrConfiComponentController;
import constants.Constants;
import enigmaDtos.AllyDataDTO;
import enigmaDtos.CandidateDTO;
import enigmaDtos.FileDetailsDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import components.primary.UboatAppController;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static components.primary.UboatAppController.showMessage;
import static constants.Constants.GSON_INSTANCE;

public class SecondTabComponentController {
    //private Timer timer;
    private TimerTask activeTeamslistRefresher;
    private TimerTask uboatReadyRefresher;
    private TimerTask showCandidatesRefresher;
    private Timer uboatReadyTimer = new Timer();
    private UboatAppController mainController;
    @FXML
    private CurrConfiComponentController currConfiComponentController;
    @FXML
    private TextField inputTF;
    @FXML
    private TableView teamsTable;
    @FXML
    private TableColumn<AllyDataDTO, String> nameColumn;
    @FXML
    private TableColumn<AllyDataDTO, String> amountOfAgentsColumn;
    @FXML
    private TableColumn<AllyDataDTO, String> taskSizeColumn;
    @FXML
    private TextArea outputTA;
    @FXML
    private Label battlefieldStatusLabel;
    private ArrayList<String> dictionary;
    @FXML
    private ListView<String> dictionaryLV;
    @FXML
    private TextField searchInDictionaryTF;
    @FXML
    private ListView<String> candidatesListView;
    @FXML
    private Button readyButton;
    @FXML
    private Button processButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button clearButton;
    @FXML
    private Label winnerNameLabel;

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setSortable(false);
        amountOfAgentsColumn.setCellValueFactory(new PropertyValueFactory<>("amountOfAgents"));
        amountOfAgentsColumn.setSortable(false);
        taskSizeColumn.setCellValueFactory(new PropertyValueFactory<>("taskSize"));
        taskSizeColumn.setSortable(false);
        readyButton.setDisable(true);
        battlefieldStatusLabel.setText("OFFLINE");
        battlefieldStatusLabel.setStyle("-fx-background-color:red;");

        if (currConfiComponentController != null) {
            currConfiComponentController.setSecondTabComponentController(this);
        }
        dictionary = new ArrayList<>();
        outputTA.setEditable(false);
    }

    @FXML
    void onClickListViewAction(MouseEvent event) {
        if (inputTF.getText().equals("")) {
            inputTF.setText(dictionaryLV.getSelectionModel().getSelectedItem());
        } else {
            inputTF.setText(inputTF.getText() + " " + dictionaryLV.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void searchTextFieldActionListener(ActionEvent event) {
        dictionaryLV.getItems().clear();
        dictionaryLV.getItems().addAll(searchInList(searchInDictionaryTF.getText()));
    }

    private List<String> searchInList(String searchString) {
        List<String> searchStringArray = Arrays.asList(searchString.trim().split(" "));

        return dictionary.stream().filter(input -> {
            return searchStringArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }

//    public void updateCandidateList(CandidateDTO candidate) {
//        Platform.runLater(() -> candidatesListView.getItems().add("Candidate: " + candidate.getCandidate() + "  Agent: " + candidate.getAgentId() + "  Configuration:" + candidate.getConfiguration()));
//    }

    @FXML
    public void processCodeButtonActionListener(ActionEvent event) {
        String textToCode = inputTF.getText();

        if (textToCode == null) {
            showMessage("Empty UserName", Alert.AlertType.WARNING, "Empty input, Please enter a word to encrypt");
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.ENCRYPT_TEXT)
                .newBuilder()
                .addQueryParameter("textToCode", textToCode)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody)
                    );
                } else {
                    FileDetailsDTO fileDetailsDTO = GSON_INSTANCE.fromJson(responseBody, FileDetailsDTO.class);
                    String textAfterCode = fileDetailsDTO.getTextAfterCode();
                    Platform.runLater(() -> {
                        readyButton.setDisable(false);
                        mainController.updateConfiguration(fileDetailsDTO);
                        outputTA.setText(outputTA.getText() + "\n" + textAfterCode);
                    });
                }
            }
        });
    }

    public void fileLoadedEnablingButtons(String abc) {
        startActiveTeamsDataRefresher();
        for (int i = 0; i < abc.length(); i++) {
            Label letterFromAbcLabel = new Label(abc.charAt(i) + "");
            letterFromAbcLabel.setMinWidth(25);
            letterFromAbcLabel.setAlignment(Pos.CENTER);
            letterFromAbcLabel.setStyle("-fx-border-color: gray;");
        }
        for (int i = 0; i < abc.length(); i++) {
            Button letterFromAbcButton = new Button();
            letterFromAbcButton.setText(abc.charAt(i) + "");
        }
    }

    @FXML
    public void resetMachineButtonActionListener(ActionEvent event) {

        String finalUrl = HttpUrl
                .parse(Constants.RESET_CONFIGURATION)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                        showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody);
                        response.body().close();
                            }
                    );
                } else {
                    FileDetailsDTO fileDetailsDTO = GSON_INSTANCE.fromJson(responseBody, FileDetailsDTO.class);
                    Platform.runLater(() -> {
                        response.body().close();
                        mainController.updateConfiguration(fileDetailsDTO);
                    });
                }
            }
        });
    }

    @FXML
    public void logoutButtonActionListener(ActionEvent event) {

    }

    public void setMainController(UboatAppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void clearButtonActionListener(ActionEvent event) {
        inputTF.clear();
        outputTA.clear();
    }

    public void cleanCurrConfiguration() {
        currConfiComponentController.cleanCurrConfiguration();
    }

    public void stepCurrConfiguration(FileDetailsDTO fileDetailsDTO) {
        currConfiComponentController.stepCurrConfiguration(fileDetailsDTO);
    }

    public void setConfigurationMode(FileDetailsDTO fileDetailsDTO) {
        currConfiComponentController.buildNewCurrConfiguration(fileDetailsDTO);
    }

    public void loadFileMode(String abc) {
        fileLoadedEnablingButtons(abc);
    }

    public void setWordsToDictionary(Set<String> dictionary) {
        candidatesListView.getItems().clear();
        this.dictionary.clear();
        dictionaryLV.getItems().clear();
        this.dictionary.addAll(dictionary);
        this.dictionaryLV.getItems().addAll(this.dictionary);
    }

    public void startActiveTeamsDataRefresher() {
        activeTeamslistRefresher = new ActiveTeamsRefresher(this::updateActiveTeamsList);
        Timer timer = new Timer();
        timer.schedule(activeTeamslistRefresher, 500, 500);
    }

    private void updateActiveTeamsList(ArrayList<AllyDataDTO> alliesDataDTOS) {
        Platform.runLater(() ->
        {
            teamsTable.getItems().clear();
            for(AllyDataDTO allyDTO : alliesDataDTOS){
                teamsTable.getItems().add(allyDTO);
            }
        });
    }

    public void startUboatReadyRefresher() {

        uboatReadyRefresher = new UboatReadyRefresher(this::updateUboatReadyRefresher);
        uboatReadyTimer.schedule(uboatReadyRefresher, 2000, 2000);
    }

    public void updateUboatReadyRefresher(Boolean isEveryoneReady) {
        if (isEveryoneReady == true) {
            uboatReadyTimer.cancel();
            startShowCandidatesRefresher();
            Platform.runLater(() -> {
                candidatesListView.getItems().clear();
                battlefieldStatusLabel.setText("");
                battlefieldStatusLabel.setText("ACTIVE");
                battlefieldStatusLabel.setStyle("-fx-background-color:green;");
            });
            // calls other refreshers...
        }
    }

    public void startShowCandidatesRefresher(){
        showCandidatesRefresher = new ShowCandidatesRefresher(this::updateShowCandidatesRefresher);
        Timer showCandidatesTimer = new Timer();
        showCandidatesTimer.schedule(showCandidatesRefresher,2000,2000);

    }
    public void updateShowCandidatesRefresher(ArrayList<CandidateDTO> candidateDTOS) {
        Platform.runLater(() -> {
            boolean isThereWinner = false;
            String winnerAllyName = "";
            for (CandidateDTO candidate : candidateDTOS) {
                candidatesListView.getItems()
                        .add("Candidate: " + candidate.getCandidateWord()
                                + "  Ally: " + candidate.getAllyName()
                                + "  Agent: " + candidate.getAgentName()
                                + "  ThreadID: " + candidate.getThreadId()
                                + "  Config:" + candidate.getConfiguration());
                if (candidate.IsTheWinningWord()) {
                    isThereWinner = true;
                    winnerAllyName = candidate.getAllyName();
                }
            }
            if (isThereWinner) {
                winnerNameLabel.setText(winnerAllyName);
                battlefieldFinished();
            }
        });
    }
    @FXML
    void readyActionListener(ActionEvent event) {
        winnerNameLabel.setText("");
        readyButton.setDisable(true);
        processButton.setDisable(true);
        resetButton.setDisable(true);
        clearButton.setDisable(true);
        candidatesListView.getItems().clear();
        startUboatReadyRefresher();
    }
    public void battlefieldFinished(){
        uboatReadyRefresher.cancel();
        showCandidatesRefresher.cancel();
        processButton.setDisable(false);
        resetButton.setDisable(false);
        clearButton.setDisable(false);
        battlefieldStatusLabel.setText("OFFLINE");
        battlefieldStatusLabel.setStyle("-fx-background-color:red;");
    }
}