package components.primary;

import components.Battlefield;
import components.contest.ContestAndTeamController;
import components.contest.ContestTileController;
import components.login.LoginController;
import components.secondTab.ActiveTeamsRefresher;
import components.secondTab.ShowCandidatesRefresher;
import constants.Constants;
import enigmaDtos.AgentDataDTO;
import enigmaDtos.AllyDataDTO;
import enigmaDtos.CandidateDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AllyAppController {
    @FXML
    private ContestAndTeamController contestAndTeamController;
    private Timer timer;
    private Timer contestStartedTimer = new Timer();
    private TimerTask listRefresher;
    private TimerTask activeTeamsListRefresher;
    private TimerTask agentsJoinedRefresher;
    private TimerTask battlefieldDataRefresher;
    private TimerTask showCandidatesRefresher;
    private TimerTask contestStartedRefresher;
    private BorderPane loginComponent;
    @FXML
    private TableView teamsTable;
    @FXML
    private Button readyButton;
    @FXML
    private TableColumn<AllyDataDTO, String> nameColumn;
    @FXML
    private TableColumn<AllyDataDTO, String> amountOfAgentsColumn;
    @FXML
    private TableColumn<AllyDataDTO, String> taskSizeColumn;
    @FXML
    private TableView agentsTable;
    @FXML
    private TableColumn<AgentDataDTO, String> agentNameColumn;
    @FXML
    private TableColumn<AgentDataDTO, String> threadsCountColumn;
    @FXML
    private TableColumn<AgentDataDTO, String> tasksPerPullColumn;
    @FXML
    private LoginController loginController;
    @FXML
    private BorderPane mainBP;
    @FXML
    private FlowPane contestDataFP;
    @FXML
    private Tab contestTab;
    @FXML
    private Spinner<Integer> taskSizeSpinner;
    @FXML
    private ListView<String> candidatesListView;
    @FXML
    private Label winnerNameLabel;

    boolean allyJoined = false;

    @FXML
    public void initialize() {
        loadLoginPage();
        contestTab.setDisable(true);
        //Teams table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setSortable(false);
        amountOfAgentsColumn.setCellValueFactory(new PropertyValueFactory<>("amountOfAgents"));
        amountOfAgentsColumn.setSortable(false);
        taskSizeColumn.setCellValueFactory(new PropertyValueFactory<>("taskSize"));
        taskSizeColumn.setSortable(false);

        //Agents table
        agentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        agentNameColumn.setSortable(false);
        threadsCountColumn.setCellValueFactory(new PropertyValueFactory<>("amountOfThreads"));
        threadsCountColumn.setSortable(false);
        tasksPerPullColumn.setCellValueFactory(new PropertyValueFactory<>("tasksPerPull"));
        tasksPerPullColumn.setSortable(false);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000000, 100);
        taskSizeSpinner.setValueFactory(valueFactory);
        if(contestAndTeamController != null){
            contestAndTeamController.setAppController(this);
        }

        if (loginController != null) {
            loginController.setMainController(this);
        }
    }

    private void loadLoginPage() {
        try {
            URL loginPageUrl = getClass().getResource("/components/login/loginScreen.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMainPanelTo(Parent parent) {
        mainBP.getCenter().setVisible(false);
        mainBP.setLeft(parent);
    }

    public void switchToMainApp() {
        mainBP.getCenter().setVisible(true);
        mainBP.setLeft(null);
    }

    public void startContestDataRefresher() {
        listRefresher = new ContestDataScreenRefresher(this::updateBattlefieldContestsList);
        timer = new Timer();
        timer.schedule(listRefresher, 2000, 2000);
    }

    private void updateBattlefieldContestsList(Map<String, Battlefield> battlefieldMap) {
        Platform.runLater(() ->
        {
            contestDataFP.getChildren().clear();
            for (Map.Entry<String, Battlefield> entry : battlefieldMap.entrySet()) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/components/contest/contestTile.fxml"));
                    Node singleContestTile = loader.load();
                    ContestTileController contestTileController = loader.getController();
                    contestTileController.setMainController(this);
                    contestTileController.setBattlefieldNameLabel(entry.getKey());
                    contestTileController.setDifficultyLevelLabel(entry.getValue().getLevel().name());
                    contestTileController.setIsActiveStatusLabel(entry.getValue().getStatus().name());
                    contestTileController.setAlliesJoinedLabel(entry.getValue().getAllies(), entry.getValue().getAlliesJoined());
                    contestTileController.setJoinVisible(allyJoined);
                    contestTileController.setUboatNameLabel(entry.getValue().getUboatName());
                    contestDataFP.getChildren().add(singleContestTile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void showMessage(String title, Alert.AlertType alertType, String content) {
        Alert alert = new Alert(alertType);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle(title);
        alert.setAlertType(alertType);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void allyClickedToJoin(String battlefieldName) {
        allyJoined = true;
        contestStartedRefresher();
        String finalUrl = HttpUrl
                .parse(Constants.ALLY_JOINS_BATTLEFIELD)
                .newBuilder()
                .addQueryParameter("battlefieldname", battlefieldName)
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
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        //מקרה טוב
                        contestTab.setDisable(false);
                        startActiveTeamsDataRefresher();
                        startUpdateContestAndTeamRefresher();
                        startAgentsJoinedRefresher();
                    });
                }
            }
        });
    }

    public void startActiveTeamsDataRefresher() {
        activeTeamsListRefresher = new ActiveTeamsRefresher(this::updateActiveTeamsList);
        timer = new Timer();
        timer.schedule(activeTeamsListRefresher, 1000, 1000);
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

    public void startUpdateContestAndTeamRefresher() {
        battlefieldDataRefresher = new BattlefieldDataRefresher(this::updateContestAndTeamComponent);
        timer = new Timer();
        timer.schedule(battlefieldDataRefresher, 1000, 1000);
    }

    private void updateContestAndTeamComponent(Battlefield battlefield) {
        contestAndTeamController.updateContestAndTeamData(battlefield);
    }

    @FXML
    public void readyActionListener(ActionEvent event) {

        String finalUrl = HttpUrl
                .parse(Constants.ALLY_READY)
                .newBuilder()
                .addQueryParameter("tasksize", taskSizeSpinner.getValue()+"")
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
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        candidatesListView.getItems().clear();
                        taskSizeSpinner.setDisable(true);
                        readyButton.setDisable(true);
                    });
                }
            }
        });
    }

    public void startAgentsJoinedRefresher() {
        agentsJoinedRefresher = new AgentsJoinedRefresher(this::updateAgentsJoinedList);
        timer = new Timer();
        timer.schedule(agentsJoinedRefresher, 1000, 1000);
    }

    private void updateAgentsJoinedList(ArrayList<AgentDataDTO> agentDataDTOS) {
        Platform.runLater(() ->
        {
            agentsTable.getItems().clear();
            for(AgentDataDTO agentDataDTO : agentDataDTOS){
                agentsTable.getItems().add(agentDataDTO);
            }
        });
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

    public void contestStartedRefresher() {
        contestStartedRefresher = new ContestStartedRefresher(this::updateContestStartedRefresher);
        contestStartedTimer.schedule(contestStartedRefresher, 2000, 2000);
    }

    public void updateContestStartedRefresher(Boolean isEveryoneReady) {
        if (isEveryoneReady == true) {
            startShowCandidatesRefresher();
            contestStartedTimer.cancel();

            // calls other refreshers...
        }
    }
    public void battlefieldFinished(){
        taskSizeSpinner.setDisable(false);
        readyButton.setDisable(false);
    }
    @FXML
    public void logoutActionListener(ActionEvent event) {
        String finalUrl = HttpUrl
                .parse(Constants.ALLY_LOGOUT)
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
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {

                        loadLoginPage();
                    });
                }
            }
        });
    }
}