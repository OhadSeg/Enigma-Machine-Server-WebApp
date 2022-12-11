package components.primary;

import com.google.gson.reflect.TypeToken;
import constants.Constants;
import enigmaDtos.AgentDataDTO;
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

public class AgentsJoinedRefresher extends TimerTask {
    private final Consumer<ArrayList<AgentDataDTO>> agentsListConsumer;

    public AgentsJoinedRefresher(Consumer<ArrayList<AgentDataDTO>> agentsListConsumer){
        this.agentsListConsumer = agentsListConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(Constants.AGENTS_JOINED_REFRESHER, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("FAILED");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                                AllyAppController.showMessage("Error11: " + response.code(), Alert.AlertType.WARNING, "Something went wrong: " + responseBody);
                                response.body().close();
                            }
                    );
                } else {
                    ArrayList<AgentDataDTO> agentDataDTOS = GSON_INSTANCE.fromJson(responseBody,new TypeToken<ArrayList<AgentDataDTO>>(){}.getType());
                    agentsListConsumer.accept(agentDataDTOS);
                    Platform.runLater(() -> {
                        response.body().close();
                    });
                }
            }
        });
    }
}
