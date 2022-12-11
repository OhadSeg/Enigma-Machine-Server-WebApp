package constants;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

public class Constants {

    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    public final static String CONTEXT_PATH = "/webApp_Web_exploded";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String LOAD_FILE = FULL_SERVER_PATH + "/loadFile";
    public final static String SET_CONFIGURATION = FULL_SERVER_PATH + "/setConfiguration";
    public final static String RANDOM_CONFIGURATION = FULL_SERVER_PATH + "/randomConfiguration";
    public final static String RESET_CONFIGURATION = FULL_SERVER_PATH + "/resetConfiguration";
    public final static String ENCRYPT_TEXT = FULL_SERVER_PATH + "/encryptText";
    public final static String ACTIVE_TEAMS_DATA_REFRESHER = FULL_SERVER_PATH + "/activeTeamsDataRefresher";
    public final static String UBOAT_READY_REFRESHER = FULL_SERVER_PATH + "/uboatReadyRefresher";
    public final static String SHOW_CANDIDATES_REFRESHER = FULL_SERVER_PATH + "/showCandidatesRefresher";
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public final static Gson GSON_INSTANCE = new Gson();
}
