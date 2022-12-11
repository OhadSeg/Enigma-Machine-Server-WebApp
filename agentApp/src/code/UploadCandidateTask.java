package code;

import com.google.gson.reflect.TypeToken;
import constants.Constants;
import enigmaDtos.CandidateDTO;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import static components.login.LoginController.showMessage;
import static constants.Constants.GSON_INSTANCE;

public class UploadCandidateTask extends TimerTask {
    private final BlockingQueue<CandidateDTO> candidateQueue;

    public UploadCandidateTask(BlockingQueue<CandidateDTO> candidateQueue) {
        this.candidateQueue = candidateQueue;
    }

    @Override
    public void run() {

        int amountOfCandidates = candidateQueue.size();
        if (amountOfCandidates != 0) {
            ArrayList<CandidateDTO> candidateListToUpload = new ArrayList<>();
            for (int i = 0; i < amountOfCandidates; i++) {
                CandidateDTO candidateDTO = candidateQueue.remove();
                candidateListToUpload.add(candidateDTO);
            }
            //String jsonCandidateQueue = GSON_INSTANCE.toJson(candidateListToUpload, ArrayList.class);
            //,new TypeToken<BlockingQueue<CandidateDTO>>() {
            //                    }.getType()
            String jsonCandidateQueue = GSON_INSTANCE.toJson(candidateListToUpload,new TypeToken<ArrayList<CandidateDTO>>() {}.getType());

            RequestBody body = RequestBody.create(jsonCandidateQueue, MediaType.parse("application/json charset=UTF-8"));

            String finalUrl = HttpUrl
                    .parse(Constants.UPLOAD_CANDIDATES)
                    .newBuilder()
                    .build()
                    .toString();

            Request request = new Request.Builder()
                    .url(finalUrl)
                    .post(body)
                    .build();

            HttpClientUtil.runAsync2(request, new Callback() {

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
                        Platform.runLater(() -> {
                                    showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody);
                                    response.body().close();
                                }
                        );
                    } else {
                        Platform.runLater(() -> {
                            response.body().close();
                        });
                    }
                }
            });
        }
    }
}