package components.login;

import constants.Constants;
import enigmaDtos.AgentDataDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.function.Consumer;

import static components.login.LoginController.showMessage;
import static constants.Constants.GSON_INSTANCE;

public class GetAlliesNamesRefresher extends TimerTask {
    private final Consumer<ObservableList<String>> alliesNamesConsumer;

    public GetAlliesNamesRefresher(Consumer<ObservableList<String>> alliesNamesConsumer) {
        this.alliesNamesConsumer = alliesNamesConsumer;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.ALLIES_NAMES)
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
                    ArrayList<String> alliesNames = GSON_INSTANCE.fromJson(responseBody, ArrayList.class);
                    ObservableList<String> alliesNamesObservList = FXCollections.observableList(alliesNames);
                    alliesNamesConsumer.accept(alliesNamesObservList);
                }
            }
        });
    }
}
