package users;

import bruteForce.DecriptionManager;
import bruteForce.SupportTask;
import enigmaDtos.BruteForceConfigDTO;
import enigmaDtos.FileDetailsDTO;
import utils.BattlefieldLevel;
import utils.RomanNumbers;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DM {
    int base;
    private boolean isFinish = false;
    private int PermutationsQuantity = 0;
    private int taskSize;
    private int amountOfReflectors;
    private BattlefieldLevel difficultyLevel;
    BruteForceConfigDTO configuration = new BruteForceConfigDTO(); //the details that the agents will pull from queue.
    private BlockingQueue<BruteForceConfigDTO> blockingQueue;

    public DM(FileDetailsDTO fileDetailsDTO, BattlefieldLevel battlefieldLevel, int taskSize) {
        PermutationsQuantity = (int) Math.pow(fileDetailsDTO.getAbc().length(), fileDetailsDTO.getAmountOfAllRotorsInUse());

        configuration.setChosenValidInUseRotors(fileDetailsDTO.getRotorsInUse());
        configuration.setChosenValidReflector(fileDetailsDTO.getReflectorInUse());
        difficultyLevel = battlefieldLevel;
        this.amountOfReflectors = fileDetailsDTO.getNumOfReflectors();
        this.taskSize = taskSize;

        blockingQueue = new LinkedBlockingQueue<>(1000);
    }

    public void start() {
        Thread t1 = new Thread(new ConfigurationToQueue());
        t1.start();
    }

    public class ConfigurationToQueue implements Runnable {
        @Override
        public void run() {
            switch (difficultyLevel) {
                case EASY:
                    generateEasyModeConfig();
                    break;
                case MEDIUM:
                    executeMediumMode();
                    break;
                case HARD:
                    //executeHardMode();
                    break;
                case IMPOSSIBLE:
                    //executeImpossibleMode();
                    break;
            }
        }
    }

    public void generateEasyModeConfig() {
        int iterations = PermutationsQuantity / taskSize;

        while (iterations > 0 && !isFinish) {
            try {
                blockingQueue.put(new BruteForceConfigDTO(configuration));
                BruteForceConfigDTO newConfiguration = new BruteForceConfigDTO(configuration);
                newConfiguration.setRotorsStartPosPermutation(configuration.getRotorsStartPosPermutation() + taskSize);
                configuration = newConfiguration;
                iterations--;
            } catch (InterruptedException e) {

            }
        }
    }

    public ArrayList<BruteForceConfigDTO> pullTasks(int tasksPerPull) {
        ArrayList<BruteForceConfigDTO> pulledDtoTasks = new ArrayList<>();
        for (int i = 0; i < tasksPerPull; i++) {
            pulledDtoTasks.add(blockingQueue.remove());
        }
        return pulledDtoTasks;
    }

    public void executeMediumMode() {
        for (int i = 0; i < amountOfReflectors; i++) {
            configuration.setChosenValidReflector(RomanNumbers.values()[i]);
            generateEasyModeConfig();
        }
    }
}