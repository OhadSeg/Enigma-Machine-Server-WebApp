package code;

import bruteForce.CustomThreadFactory;
import com.google.gson.reflect.TypeToken;
import components.Enigma;
import constants.Constants;
import enigmaDtos.BruteForceConfigDTO;
import enigmaDtos.TeamTasksProgressDTO;
import javafx.application.Platform;
import javafx.scene.control.Alert;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import static components.primary.AgentAppController.showMessage;
import static constants.Constants.GSON_INSTANCE;

public class PushTasksToPullTask {

    private BlockingQueue<Runnable> blockingQueue;
    private ThreadPoolExecutor threadPoolExecutor;
    private boolean contestFinished = false;
    private int tasksPerPull;
    private SupportTask supportTask;
    private int totalPulledTasks = 0;
    private Timer uploadCandidatesTimer = new Timer();
    private TimerTask uploadCandidates;
    int amountOfThreads;

    Enigma enigma;

    public PushTasksToPullTask(int amountOfThreads, int tasksPerPull) {
        this.tasksPerPull = tasksPerPull;
        this.amountOfThreads = amountOfThreads;
        blockingQueue = new LinkedBlockingQueue<>(1000);
        threadPoolExecutor = new ThreadPoolExecutor(amountOfThreads, amountOfThreads,
                5, TimeUnit.SECONDS, blockingQueue, new CustomThreadFactory());
    }

    public void setSupportTask(Enigma enigma,
                               int amountOfThreadsFromClient, int taskSizeFromClient,
                               String iAgentName, String iAllyName) {
        supportTask = new SupportTask(enigma, amountOfThreadsFromClient, taskSizeFromClient,
                iAgentName, iAllyName);
    }

    public void start() {
        threadPoolExecutor.prestartAllCoreThreads();
        uploadCandidates = new UploadCandidateTask(SupportTask.candidateQueue);
        uploadCandidatesTimer.schedule(uploadCandidates, 3000, 3000);
        Thread t1 = new Thread(new PushTasksToPullTask.TaskToPoolThread());
        t1.start();

    }

    public class TaskToPoolThread implements Runnable {
        @Override
        public void run() {
            //איך לבדוק שבאמת התור ריק -> יש הסבר במייל
            while (contestFinished == false) {
                if (blockingQueue.isEmpty()) {

                    String finalUrl = HttpUrl
                            .parse(Constants.PULL_DTO_TASKS)
                            .newBuilder()
                            .addQueryParameter("tasksPerPull", tasksPerPull + "")
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
                                Platform.runLater(() -> {
                                            showMessage("Error", Alert.AlertType.WARNING, "Something went wrong: " + responseBody);
                                            response.body().close();
                                        }
                                );
                            } else {
                                ArrayList<BruteForceConfigDTO> pulledTasksDtoList = GSON_INSTANCE.fromJson(responseBody,
                                        new TypeToken<ArrayList<BruteForceConfigDTO>>() {
                                        }.getType());
                                for (BruteForceConfigDTO TaskDto : pulledTasksDtoList) {
                                    try {
                                        blockingQueue.put(new ExecutionTask(TaskDto, supportTask));
                                        SupportTask.tasksInQueue++;
                                        totalPulledTasks++;
                                    } catch (InterruptedException e) {

                                    }
                                    Platform.runLater(() -> {
                                        response.body().close();
                                    });
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    public TeamTasksProgressDTO getTeamTasksProgressData() {
        TeamTasksProgressDTO teamTasksProgressDTO = new TeamTasksProgressDTO(SupportTask.tasksInQueue, totalPulledTasks
                , SupportTask.totalCompletedTasks);
        return teamTasksProgressDTO;
    }
}