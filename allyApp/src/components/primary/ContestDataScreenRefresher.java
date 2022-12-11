package components.primary;

import com.google.gson.reflect.TypeToken;
import components.Battlefield;
import constants.Constants;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;
import java.io.IOException;
import java.util.Map;
import java.util.TimerTask;
import java.util.function.Consumer;

import static constants.Constants.GSON_INSTANCE;

public class ContestDataScreenRefresher extends TimerTask {

    private final Consumer<Map<String, Battlefield>> battlefieldListConsumer;

    public ContestDataScreenRefresher(Consumer<Map<String, Battlefield>> battlefieldListConsumer){
        this.battlefieldListConsumer = battlefieldListConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(Constants.CONTEST_DATA_REFRESHER, new Callback() {

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
                    Map<String, Battlefield> battlefieldMap = GSON_INSTANCE.fromJson(responseBody, new TypeToken<Map<String,Battlefield>>(){}.getType());
                    battlefieldListConsumer.accept(battlefieldMap);
                    Platform.runLater(() -> {
                        response.body().close();
                    });
                }
            }
        });
    }
}
