package components.primary;

import code.PushTasksToPullTask;
import components.Battlefield;
import components.Enigma;
import components.contest.ContestAndTeamController;
import components.login.LoginController;
import components.secondTab.ShowCandidatesRefresher;
import constants.Constants;
import enigmaDtos.CandidateDTO;
import enigmaDtos.TeamTasksProgressDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static constants.Constants.*;

public class AgentAppController {
    private Timer battlefieldDataTimer = new Timer();
    private TimerTask battlefieldDataRefresher;
    private Timer contestStartedTimer = new Timer();
    private TimerTask contestStartedRefresher;
    private Timer showCandidatesTimer = new Timer();
    private TimerTask showCandidatesRefresher;
    @FXML
    private ContestAndTeamController contestAndTeamController;
    @FXML
    private BorderPane mainBP;
    @FXML
    private Label teamNameLabel;
    @FXML
    private LoginController loginController;
    @FXML
    private ListView<String> candidatesListView;
    @FXML
    private Label tasksInQueueLabel;
    @FXML
    private Label totalPulledTasksLabel;
    @FXML
    private Label totalCompletedTasksLabel;
    @FXML
    private Label winnerNameLabel;
    private PushTasksToPullTask pushTasksToPullTask;
    private GridPane loginComponent;
    private BlockingQueue<CandidateDTO> candidateQueue = new LinkedBlockingQueue<>();

    private Enigma enigma;
    int taskSizeFromAlly;
    String allyName;
    String agentName;

    public static void showMessage(String error, Alert.AlertType warning, String s) {
    }

    @FXML
    public void initialize() {
        loadLoginPage();
        if (loginController != null) {
            loginController.setMainController(this);
        }
    }

    private void loadLoginPage() {
        try {
            URL loginPageUrl = getClass().getResource("/components/login/login.fxml");
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
        mainBP.getTop().setVisible(false);
        mainBP.getCenter().setVisible(false);
        mainBP.setBottom(parent);
    }

    public void switchToMainApp(String teamName) {
        battlefieldDataTimer = new Timer();
        contestStartedTimer = new Timer();
        showCandidatesTimer = new Timer();
        startUpdateContestAndTeamRefresher();
        contestStartedRefresher();
        teamNameLabel.setText(teamName);
        agentName = loginController.getAgentName();
        mainBP.getTop().setVisible(true);
        mainBP.getCenter().setVisible(true);
        mainBP.setBottom(null);
    }

    public void startUpdateContestAndTeamRefresher() {
        battlefieldDataRefresher = new BattlefieldDataRefresher(this::updateContestAndTeamComponent);
        contestStartedTimer.schedule(battlefieldDataRefresher, 500, 500);
    }

    private void updateContestAndTeamComponent(Battlefield battlefield) {
        if(battlefield!= null) {
            contestAndTeamController.updateContestAndTeamData(battlefield);
        }
    }

    public void contestStartedRefresher() {
        contestStartedRefresher = new ContestStartedRefresher(this::updateContestStartedRefresher);
        contestStartedTimer.schedule(contestStartedRefresher, 2000, 2000);
    }

    public void updateContestStartedRefresher(Boolean isEveryoneReady) {
        if (isEveryoneReady == true) {
            candidatesListView.getItems().clear();
            startShowCandidatesRefresher();
            contestStartedTimer.cancel();
            pushTasksToPullTask = new PushTasksToPullTask(loginController.getAmountOfTreads(), loginController.getTasksPerPullSize());
            getAllyData();
            setEnigma();
            pushTasksToPullTask.setSupportTask(enigma, loginController.getAmountOfTreads(),
                    taskSizeFromAlly, agentName, allyName);
            pushTasksToPullTask.start();
            // calls other refreshers...
        }
    }

    private void setEnigma() {
        String finalUrl = HttpUrl
                .parse(ENIGMA_UBOAT)
                .newBuilder()
                .build()
                .toString();
        try {
            Response response = HttpClientUtil.runSync(finalUrl);
            String responseBody = response.body().string();
            enigma = GSON_INSTANCE.fromJson(responseBody, Enigma.class);
        } catch (IOException e) {
        }
    }

    private void getAllyData() {
        String finalUrl = HttpUrl
                .parse(Constants.GET_ALLY_DETAILS)
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
                    Platform.runLater(() ->
                            showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody)
                    );
                } else {
                    ArrayList<String> allyDetails = GSON_INSTANCE.fromJson(responseBody, ArrayList.class);
                    allyName = allyDetails.get(0);
                    taskSizeFromAlly = Integer.parseInt(allyDetails.get(1));
                }
            }
        });
    }

    public void startShowCandidatesRefresher() {
        showCandidatesRefresher = new ShowCandidatesRefresher(this::updateShowCandidatesRefresher);
        showCandidatesTimer.schedule(showCandidatesRefresher, 2000, 2000);

    }

    public void updateShowCandidatesRefresher(ArrayList<CandidateDTO> candidateDTOS) {
        updateTeamTasksProgress();
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
            }
        });
    }

    @FXML
    public void logoutActionListener(ActionEvent event) {
        String finalUrl = HttpUrl
                .parse(AGENT_LOGOUT)
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
                        battlefieldDataTimer.cancel();
                        contestStartedTimer.cancel();
                        showCandidatesTimer.cancel();
                        candidatesListView.getItems().clear();
                        winnerNameLabel.setText("");
                        loadLoginPage();
                    });
                }
            }
        });
    }

    private void updateTeamTasksProgress() {
        if (pushTasksToPullTask != null) {
            TeamTasksProgressDTO teamTasksProgressDTO = pushTasksToPullTask.getTeamTasksProgressData();
            Platform.runLater(() -> {
                tasksInQueueLabel.setText(teamTasksProgressDTO.getTasksInQueue() + "");
                totalPulledTasksLabel.setText(teamTasksProgressDTO.getTotalPulledTasksLabel() + "");
                totalCompletedTasksLabel.setText(teamTasksProgressDTO.getTotalCompletedTasksLabel() + "");
            });
        }
    }
}