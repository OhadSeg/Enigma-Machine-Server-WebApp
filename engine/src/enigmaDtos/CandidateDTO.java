package enigmaDtos;

public class CandidateDTO {

    private String candidateWord;
    private String configuration;
    private String allyName;
    private String agentName;
    private int threadId;
    private boolean isTheWinningWord;

    public CandidateDTO(String candidateWord, String configuration, String allyName, String agentName, int threadId) {
        this.candidateWord = candidateWord;
        this.configuration = configuration;
        this.allyName = allyName;
        this.agentName = agentName;
        this.threadId = threadId;
        isTheWinningWord = false;
    }


    public String getCandidateWord() {
        return candidateWord;
    }

    public String getConfiguration() {
        return configuration;
    }

    public int getThreadId() {
        return threadId;
    }

    public String getAllyName() {
        return allyName;
    }

    public String getAgentName() {
        return agentName;
    }

    public boolean IsTheWinningWord() {
        return isTheWinningWord;
    }

    public void setAsWinningWord() {
        isTheWinningWord = true;
    }
}