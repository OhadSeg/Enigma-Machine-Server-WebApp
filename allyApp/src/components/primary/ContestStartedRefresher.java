package components.primary;

import constants.Constants;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;
import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static constants.Constants.GSON_INSTANCE;

public class ContestStartedRefresher extends TimerTask {
    private final Consumer<Boolean> isContestStartedConsumer;
    public ContestStartedRefresher(Consumer<Boolean> isContestStartedConsumer) {
        this.isContestStartedConsumer = isContestStartedConsumer;
    }
    @Override
    public void run() {
        HttpClientUtil.runAsync(Constants.CONTEST_STARTED_REFRESHER, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("hello");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                                AllyAppController.showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody);
                                response.body().close();
                            }
                    );
                } else {
                    boolean contestStarted = GSON_INSTANCE.fromJson(responseBody,boolean.class);
                    isContestStartedConsumer.accept(contestStarted);
                    Platform.runLater(() -> {
                        response.body().close();
                    });
                }
            }
        });
    }
}
