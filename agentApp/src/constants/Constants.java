package constants;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

public class Constants {
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    public final static String CONTEXT_PATH = "/webApp_Web_exploded";
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String AGENT_LOGIN_PAGE = FULL_SERVER_PATH + "/agentLogin";
    public final static String ALLIES_NAMES = FULL_SERVER_PATH + "/getAlliesNames";
    public final static String ENIGMA_UBOAT = FULL_SERVER_PATH + "/getEnigmaFromUboat";
    public final static String PULL_DTO_TASKS = FULL_SERVER_PATH + "/pullDtoTasks";
    public final static String UPLOAD_CANDIDATES = FULL_SERVER_PATH + "/uploadCandidates";
    public final static String GET_ALLY_DETAILS = FULL_SERVER_PATH + "/getAllyDetails";
    public final static String AGENT_LOGOUT = FULL_SERVER_PATH + "/agentLogout";
    public final static Gson GSON_INSTANCE = new Gson();
}
