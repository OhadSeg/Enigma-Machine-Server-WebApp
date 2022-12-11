package components.login;

import components.primary.AllyAppController;
import constants.Constants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;

import java.io.IOException;


public class LoginController {

    private AllyAppController mainController;
    @FXML
    private Label helloUserLabel;

    @FXML
    private Label absLabel;

    @FXML
    private TextField userNameTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Button quitButton;

    @FXML
    private Label userNameLabel;


    @FXML
    public void loginButtonActionListener(ActionEvent event) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            showMessage("Empty UserName", Alert.AlertType.WARNING, "User name is empty. You can't login with empty user name.");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("usertype", "ALLY")
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
                        mainController.switchToMainApp();
                        mainController.startContestDataRefresher();
                    });
                }
            }
        });
    }

    public void setMainController(AllyAppController mainController) {
        this.mainController = mainController;
    }
    @FXML
    void activateQuitButton(ActionEvent event) {

    }

    @FXML
    void activateUserNameTextField(ActionEvent event) {

    }

    public void showMessage(String title, Alert.AlertType alertType, String content) {
        Alert alert = new Alert(alertType);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle(title);
        alert.setAlertType(alertType);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
