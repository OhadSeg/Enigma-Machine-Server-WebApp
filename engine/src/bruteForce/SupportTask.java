package bruteForce;

import components.Enigma;
import enigmaDtos.BruteForceConfigDTO;
import enigmaDtos.BruteForceDetailsDTO;
import enigmaDtos.CandidateDTO;

import java.util.*;
import java.util.concurrent.*;

public class SupportTask {
    private static int base;
    private static Set<String> dictionary;
    private static int taskSize;
    private static int amountOfAgents;
    private static String textToCode;
    private static ArrayList<Enigma> agentsEnigma = new ArrayList<>();
    private static BlockingQueue<CandidateDTO> candidateQueue;
    private static String excludeChars;

    private static int abcSize;

    public SupportTask(int baseFromDM, String excludeCharsFromDM,Set<String> dictionaryFromDM,BlockingQueue<CandidateDTO> candidateQueueFromDM){
        textToCode = new String();
        base = baseFromDM;
        dictionary =new HashSet<>(dictionaryFromDM);
        candidateQueue = candidateQueueFromDM;
        excludeChars = excludeCharsFromDM;
    }

    public void setDetailsFromUser(BruteForceDetailsDTO bdDetails, Enigma enigma) {
        this.taskSize = bdDetails.getTaskSize();
        this.amountOfAgents = bdDetails.getAgents();
        agentsEnigma = new ArrayList<>();
        for (int i = 0; i < amountOfAgents; i++) {
            agentsEnigma.add(enigma.deepCopy());
        }
        abcSize = enigma.getAbc().length();
    }

    public static class Task implements Runnable {
        //private int rotorsStartPosPermutation;
        private BruteForceConfigDTO configuration;
        public Task(BruteForceConfigDTO configuration) {
            this.configuration = new BruteForceConfigDTO(configuration); // לבדוק האם צריך העתקה עמוקה
        }

        @Override
        public void run() throws NoSuchElementException {
            int threadId = Integer.parseInt(Thread.currentThread().getName()) - 1;
            for (int i = 0; i < taskSize; i++) {
                agentsEnigma.get(threadId % amountOfAgents).setConfigurationBF(rotorsPosCalculator(configuration.getRotorsStartPosPermutation()+i),configuration.getChosenValidReflector());
                String result = (String) agentsEnigma.get(threadId % amountOfAgents).execute(textToCode).getKey();
                result = result.toLowerCase();
                String resultAfterFiltering = removeExcludeCharsFromText(result);
                ArrayList<String> words = new ArrayList<>(Arrays.asList(resultAfterFiltering.split("\\s+")));
                // אחרי שמדפיסים את המילון למסך, לבדוק אם יש בתוכו גם רווחים
                for (String word : words) {
                    if (dictionary.contains(word) == false) {
                        resultAfterFiltering = null;
                    }
                }
                if (resultAfterFiltering != null) {
                    candidateQueue.add(new CandidateDTO(result.toUpperCase(), agentsEnigma.get(threadId % amountOfAgents).toString(), null,null,(threadId % amountOfAgents)+1));
                }
            }
        }
    }

    public static ArrayList<Integer> rotorsPosCalculator(int permutation) {
        ArrayList<Integer> rotorsPos = new ArrayList<>();

        while (permutation > 0) {
            int i = permutation / abcSize;
            rotorsPos.add(permutation % abcSize);
            permutation = i;
        }

        int difference = base - rotorsPos.size();
        if(difference>0){
            for (int i = 0; i < difference; i++) {
                rotorsPos.add(0);
            }
        }
        return rotorsPos;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public int amountOfRotorsPosPermutation() {
        return (int) Math.pow(agentsEnigma.get(0).getAbc().length(), base);
    }

    public void setTextToCode(String textToCode) {
        SupportTask.textToCode = textToCode;
    }

    public static String removeExcludeCharsFromText(String textStr) {
        StringBuilder dictionaryStrAfter = new StringBuilder();
        for (int i = 0; i < textStr.length(); i++) {
            if (excludeChars.indexOf(textStr.charAt(i)) == -1) {
                dictionaryStrAfter.append(textStr.charAt(i));
            }
        }
        return dictionaryStrAfter.toString();
    }

    public void resetCandidateQueue(){
        candidateQueue.clear();
    }
}
