package bruteForce;

import components.Enigma;
import enigmaDtos.*;
import generated.CTEEnigma;
import utils.BattlefieldLevel;
import utils.RomanNumbers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DecriptionManager {
    private BlockingQueue<Runnable> blockingQueue;
    private ThreadPoolExecutor threadPoolExecutor;
    private SupportTask supportTask;
    private Enigma enigmaForInfo;
    private int maxAmountOfAgents;
    private boolean isFinish;
    private int PermutationsQuantity = 0;
    private int progress = 0;
    private  boolean isPause;
    private Set<String> dictionary;
    private String excludeChars;
    private BattlefieldLevel difficultyLevel;
    private BlockingQueue<CandidateDTO> candidateQueue = new LinkedBlockingQueue<>();

    public DecriptionManager(CTEEnigma cteEnigma) {
        int base = cteEnigma.getCTEMachine().getRotorsCount();
        excludeChars = cteEnigma.getCTEDecipher().getCTEDictionary().getExcludeChars();
        String dictionaryStr = cteEnigma.getCTEDecipher().getCTEDictionary().getWords();
        String dictionaryStrAfterRemoving = removeExcludeCharsFromText(dictionaryStr);
        Set<String> dictionary = new HashSet<>(Arrays.asList(dictionaryStrAfterRemoving.split("\\s+")));
        dictionary.remove("");
        supportTask = new SupportTask(base, cteEnigma.getCTEDecipher().getCTEDictionary().getExcludeChars(), dictionary, candidateQueue);
        this.dictionary = dictionary;
        blockingQueue = new LinkedBlockingQueue<>(1000);
    }

    public int getMaxAmountOfAgents() {
        return maxAmountOfAgents;
    }

    public void setDetailsFromUserToSupportTask(BruteForceDetailsDTO bdDetails, Enigma enigma) {
        blockingQueue.clear();
        supportTask.resetCandidateQueue();
        threadPoolExecutor = new ThreadPoolExecutor(bdDetails.getAgents(), bdDetails.getAgents(), 5, TimeUnit.SECONDS, blockingQueue, new CustomThreadFactory());
        threadPoolExecutor.prestartAllCoreThreads();
        enigmaForInfo = enigma;
        difficultyLevel = bdDetails.getLevel();
        supportTask.setDetailsFromUser(bdDetails, enigma);
    }

    public void setTextToBruteForce(String textToBruteForce) {
        supportTask.setTextToCode(textToBruteForce);
    }

    public void start(BruteForceDetailsDTO bdDetails, Enigma enigma) {
        setDetailsFromUserToSupportTask(bdDetails, enigma);
        progress = 0;
        isFinish = false;
        Thread t1 = new Thread(new TaskToPoolThread());
        t1.start();

    }

    public class TaskToPoolThread implements Runnable {
        @Override
        public void run() {

            BruteForceConfigDTO currConfiguration = createConfigurationFromEnigma();

            switch (difficultyLevel) {
                case EASY:
                    PermutationsQuantity = supportTask.amountOfRotorsPosPermutation();
                    executeEasyMode(currConfiguration);
                    break;
                case MEDIUM:
                    PermutationsQuantity = supportTask.amountOfRotorsPosPermutation() * enigmaForInfo.getAmountOfAllReflectors();
                    executeMediumMode(currConfiguration);
                    break;
                case HARD:
                    PermutationsQuantity = supportTask.amountOfRotorsPosPermutation() * enigmaForInfo.getAmountOfAllReflectors() * factorial(enigmaForInfo.getAmountOfRotorsInUse());
                    executeHardMode();
                    break;
                case IMPOSSIBLE:
                    //PermutationsQuantity = supportTask.amountOfRotorsPosPermutation()
                    executeImpossibleMode();
                    break;
            }
            threadPoolExecutor.shutdown();
            try {
                threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {

            }
            isFinish = true;
        }
    }

    public void executeEasyMode(BruteForceConfigDTO configuration) {
        int iterations = supportTask.amountOfRotorsPosPermutation() / supportTask.getTaskSize();
        while (iterations > 0 && !isFinish) {
            try {
                if (!isPause) {
                    blockingQueue.put(new SupportTask.Task(configuration));
                    BruteForceConfigDTO newConfiguration = new BruteForceConfigDTO(configuration);
                    newConfiguration.setRotorsStartPosPermutation(configuration.getRotorsStartPosPermutation() + supportTask.getTaskSize());
                    configuration = newConfiguration;
                    progress += supportTask.getTaskSize();
                    iterations--;
                }
            } catch (Exception e) {

            }
        }
    }

    public void executeMediumMode(BruteForceConfigDTO configuration) {
        for (int i = 0; i < enigmaForInfo.getAmountOfAllReflectors(); i++) {
            BruteForceConfigDTO currConfiguration = new BruteForceConfigDTO(configuration);
            currConfiguration.setChosenValidReflector(RomanNumbers.values()[i]);
            executeEasyMode(currConfiguration);
        }
    }

    public void executeHardMode() {

    }

    public void executeImpossibleMode() {

    }

    public boolean isWordInDictionary(String word) {
        return dictionary.contains(word);
    }

    public String removeExcludeCharsFromText(String textStr) {
        StringBuilder dictionaryStrAfter = new StringBuilder();
        for (int i = 0; i < textStr.length(); i++) {
            if (excludeChars.indexOf(textStr.charAt(i)) == -1) {
                dictionaryStrAfter.append(textStr.charAt(i));
            }
        }
        return dictionaryStrAfter.toString();
    }

    public boolean checkIfFinish() {
        return isFinish;
    }

    public boolean thereIsCandidates() {
        return !candidateQueue.isEmpty();
    }

    public CandidateDTO getCandidate() throws InterruptedException {
        return candidateQueue.take();
    }

    public double getProgress() {
        return (double) progress / PermutationsQuantity;
    }

    public int factorial(int n) {
        int fact = 1;
        for (int i = 2; i <= n; i++) {
            fact = fact * i;
        }
        return fact;
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    public void doPause(){
        isPause = true;
    }

    public void doResume(){
        isPause = false;
    }
    public void doStop(){
        isFinish = true;

    }

    public BruteForceConfigDTO createConfigurationFromEnigma(){
        BruteForceConfigDTO toReturn = new BruteForceConfigDTO();
        FileDetailsDTO toCopy = enigmaForInfo.getConfigurationAsDTO();
        toReturn.setChosenValidInUseRotors(toCopy.getRotorsInUse());
        toReturn.setChosenValidReflector(toCopy.getReflectorInUse());
        return toReturn;
    }
}