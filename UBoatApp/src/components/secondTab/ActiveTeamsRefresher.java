package components.secondTab;

import com.google.gson.reflect.TypeToken;
import components.primary.UboatAppController;
import constants.Constants;
import enigmaDtos.AllyDataDTO;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.function.Consumer;

import static constants.Constants.GSON_INSTANCE;

public class ActiveTeamsRefresher extends TimerTask {

    private final Consumer<ArrayList<AllyDataDTO>> alliesListConsumer;

    public ActiveTeamsRefresher(Consumer<ArrayList<AllyDataDTO>> alliesListConsumer){
        this.alliesListConsumer = alliesListConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(Constants.ACTIVE_TEAMS_DATA_REFRESHER, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("hello");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                                UboatAppController.showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody);
                                response.body().close();
                            }
                    );
                } else {
                    ArrayList<AllyDataDTO> allyDataDTOS = GSON_INSTANCE.fromJson(responseBody, new TypeToken<ArrayList<AllyDataDTO>>(){}.getType());
                    alliesListConsumer.accept(allyDataDTOS);
                    Platform.runLater(() -> {
                        response.body().close();
                    });
                }
            }
        });
    }
}
