package code;

import components.Enigma;
import enigmaDtos.BruteForceConfigDTO;
import enigmaDtos.CandidateDTO;
import enigmaDtos.FileDetailsDTO;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SupportTask {
    public ArrayList<Enigma> agentsEnigma;
    public int amountOfThreads;
    public static int tasksInQueue = 0;
    public static int totalCompletedTasks = 0;
    public int base;
    public int taskSize;
    public int abcSize;
    public String textToCode;
    public Set<String> dictionary;
    public String excludeChars;
    public String agentName;
    public String allyName;
    public static BlockingQueue<CandidateDTO> candidateQueue = new LinkedBlockingQueue<>();

    public SupportTask(Enigma enigma,
                       int amountOfThreadsFromClient, int taskSizeFromClient,
                       String iAgentName, String iAllyName) {
        FileDetailsDTO fileDetailsDTO = enigma.getConfigurationAsDTO();
        amountOfThreads = amountOfThreadsFromClient;
        taskSize = taskSizeFromClient;
        agentsEnigma = new ArrayList<>();
        for (int i = 0; i < amountOfThreadsFromClient; i++) {
            agentsEnigma.add(enigma.deepCopy());
        }
        base = fileDetailsDTO.getAmountOfAllRotorsInUse();
        abcSize = fileDetailsDTO.getAbc().length();
        textToCode = fileDetailsDTO.getTextAfterCode();
        dictionary = new HashSet<>(enigma.getDictionary());
        excludeChars = enigma.getExcludeChars();

        agentName = iAgentName;
        allyName = iAllyName;
    }

    public ArrayList<Integer> rotorsPosCalculator(int permutation) {
        ArrayList<Integer> rotorsPos = new ArrayList<>();
        while (permutation > 0) {
            int i = permutation / abcSize;
            rotorsPos.add(permutation % abcSize);
            permutation = i;
        }

        int difference = base - rotorsPos.size();
        if (difference > 0) {
            for (int i = 0; i < difference; i++) {
                rotorsPos.add(0);
            }
        }
        return rotorsPos;
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
}