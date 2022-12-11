package components.login;

import components.primary.AgentAppController;
import constants.Constants;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static constants.Constants.GSON_INSTANCE;

public class LoginController {

    private AgentAppController mainController;
    @FXML
    private TextField userNameTextField;
    private TimerTask getAlliesNamesRefresher;
    private Timer getAlliesNamesTimer;

    @FXML
    private Button logInButton;

    @FXML
    private Label numOfThreadsLabel;

    @FXML
    private ComboBox<String> teamComboBox;

    @FXML
    private Spinner<Integer> tasksPerPullSpinner;

    @FXML
    private ComboBox<Integer> threadsComboBox;


    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000000, 100);
        tasksPerPullSpinner.setValueFactory(valueFactory);

        setThreadsComb();
        startGetAlliesNamesRefresher();
        //setTeamComb();
    }

    @FXML
    public void loginButtonActionListener(ActionEvent event) {
        String userName = userNameTextField.getText();
        String chosenTeamToJoin = teamComboBox.getValue();
        int chosenAmountOfThreads = threadsComboBox.getValue();
        int chosenTasksPerPull = tasksPerPullSpinner.getValue();

        if (userName.isEmpty()) {
            showMessage("Empty UserName", Alert.AlertType.WARNING, "User name is empty. You can't login with empty user name.");
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.AGENT_LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("usertype", "AGENT")
                .addQueryParameter("team", chosenTeamToJoin)
                .addQueryParameter("threads", chosenAmountOfThreads + "")
                .addQueryParameter("tasksperpull", chosenTasksPerPull + "")
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
                    Platform.runLater(() -> {
                        mainController.switchToMainApp(chosenTeamToJoin);
                    });
                }
            }
        });
    }

    public void setThreadsComb(){
        ArrayList<Integer> amountOfAgents = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            amountOfAgents.add(i);
        }
        ObservableList<Integer> amountOfAgentsObservList = FXCollections.observableList(amountOfAgents);
        threadsComboBox.setItems(amountOfAgentsObservList);
    }

//    protected void setTeamComb(){
//
//        String finalUrl = HttpUrl
//                .parse(Constants.ALLIES_NAMES)
//                .newBuilder()
//                .build()
//                .toString();
//        HttpClientUtil.runAsync(finalUrl, new Callback() {
//
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Platform.runLater(() ->
//                        showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + e.getMessage())
//                );
//            }
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String responseBody = response.body().string();
//                if (response.code() != 200) {
//                    Platform.runLater(() ->
//                            showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody)
//                    );
//                } else {
//                    ArrayList<String> alliesNames = GSON_INSTANCE.fromJson(responseBody,ArrayList.class);
//                    ObservableList<String> alliesNamesObservList = FXCollections.observableList(alliesNames);
//                    teamComboBox.setItems(alliesNamesObservList);
//                }
//            }
//        });
//    }

    public void startGetAlliesNamesRefresher() {
        getAlliesNamesRefresher = new GetAlliesNamesRefresher(this::updateAlliesNamesRefresher);
        getAlliesNamesTimer = new Timer();
        getAlliesNamesTimer.schedule(getAlliesNamesRefresher, 2000, 2000);
    }
    public void updateAlliesNamesRefresher(ObservableList<String> alliesNamesObservList){
        Platform.runLater(() ->
        teamComboBox.setItems(alliesNamesObservList));
    }
    public static void showMessage(String title, Alert.AlertType alertType, String content) {
        Alert alert = new Alert(alertType);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle(title);
        alert.setAlertType(alertType);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void setMainController(AgentAppController mainController) {
        this.mainController = mainController;
    }

    public int getAmountOfTreads(){
        return threadsComboBox.getValue();
    }
    public int getTasksPerPullSize(){
        return tasksPerPullSpinner.getValue();
    }
    public String getAgentName(){
        return userNameTextField.getText();
    }
}
