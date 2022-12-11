package servlets;

import components.Battlefield;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.BattlefieldManager;
import users.Uboat;
import users.UserManager;
import utils.BattlefieldStatus;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.IOException;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "UboatReadyRefresherServlet", urlPatterns = {"/uboatReadyRefresher"})
public class UboatReadyRefresherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String uboatNameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Uboat uboat = userManager.getUboat(uboatNameFromSession);
        String battlefieldName = uboat.getBattlefieldName();
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        Battlefield battlefield = battlefieldManager.getBattlefield(battlefieldName);
        synchronized (this) {
            if(uboat.isAllAlliesReady()){
                uboat.startContest(battlefield.getLevel());
                battlefield.setStatus(BattlefieldStatus.ACTIVE);
                response.getWriter().println(GSON_INSTANCE.toJson(true));
            }
            else{
                response.getWriter().println(GSON_INSTANCE.toJson(false));
            }
        }
    }
}
