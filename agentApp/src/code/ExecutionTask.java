package code;

import enigmaDtos.BruteForceConfigDTO;
import enigmaDtos.CandidateDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class ExecutionTask implements Runnable  {

    private BruteForceConfigDTO configuration;
    private SupportTask supportTask;
    public ExecutionTask(BruteForceConfigDTO configuration, SupportTask supportTask){
        this.configuration = new BruteForceConfigDTO(configuration);
        this.supportTask = supportTask;
    }
    @Override
    public void run() throws NoSuchElementException {
        SupportTask.tasksInQueue--;
        int threadId = Integer.parseInt(Thread.currentThread().getName()) - 1;
        for (int i = 0; i < supportTask.taskSize; i++) {
            supportTask.agentsEnigma.get(threadId % supportTask.amountOfThreads).setConfigurationBF(supportTask.rotorsPosCalculator(configuration.getRotorsStartPosPermutation() + i), configuration.getChosenValidReflector());
            String result = (String) supportTask.agentsEnigma.get(threadId % supportTask.amountOfThreads).execute(supportTask.textToCode).getKey();
            result = result.toLowerCase();
            String resultAfterFiltering = supportTask.removeExcludeCharsFromText(result);
            ArrayList<String> words = new ArrayList<>(Arrays.asList(resultAfterFiltering.split("\\s+")));
            for (String word : words) {
                if (supportTask.dictionary.contains(word) == false) {
                    resultAfterFiltering = null;
                }
            }
            if (resultAfterFiltering != null) {
                supportTask.candidateQueue.add(new CandidateDTO(result.toUpperCase(),
                        supportTask.agentsEnigma.get(threadId % supportTask.amountOfThreads).toString(),
                        supportTask.allyName, supportTask.agentName, (threadId % supportTask.amountOfThreads) + 1));
            }
        }
        SupportTask.totalCompletedTasks++;
    }
}