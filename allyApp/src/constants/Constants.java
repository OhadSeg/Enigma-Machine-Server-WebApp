package constants;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

public class Constants {

    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    public final static String CONTEXT_PATH = "/webApp_Web_exploded";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String CONTEST_DATA_REFRESHER = FULL_SERVER_PATH + "/contestDataRefresher";
    public final static String ALLY_JOINS_BATTLEFIELD = FULL_SERVER_PATH + "/addAllyToBattlefield";
    public final static String BATTLEFIELD_DATA_REFRESHER = FULL_SERVER_PATH + "/battleFieldRefresher";
    public final static String AGENTS_JOINED_REFRESHER = FULL_SERVER_PATH + "/agentsJoinedRefresher";
    public final static String ALLY_READY = FULL_SERVER_PATH + "/allyReady";
    public final static String CONTEST_STARTED_REFRESHER = FULL_SERVER_PATH + "/contestStartedRefresher";
    public final static String ALLY_LOGOUT = FULL_SERVER_PATH + "/allyLogout";

    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public final static Gson GSON_INSTANCE = new Gson();


}
