package components.secondTab;

import com.google.gson.reflect.TypeToken;
import components.primary.UboatAppController;
import constants.Constants;
import enigmaDtos.CandidateDTO;
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

    public class ShowCandidatesRefresher extends TimerTask {
    private final Consumer<ArrayList<CandidateDTO>> candidatesListConsumer;
    public ShowCandidatesRefresher(Consumer<ArrayList<CandidateDTO>> candidatesListConsumer) {
        this.candidatesListConsumer = candidatesListConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(Constants.SHOW_CANDIDATES_REFRESHER, new Callback() {

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
                    ArrayList<CandidateDTO> candidateDTOS = GSON_INSTANCE.fromJson(responseBody, new TypeToken<ArrayList<CandidateDTO>>(){}.getType());
                    candidatesListConsumer.accept(candidateDTOS);
                    Platform.runLater(() -> {
                        response.body().close();
                    });
                }
            }
        });
    }
}
