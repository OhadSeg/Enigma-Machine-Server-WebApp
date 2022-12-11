package servlets;

import components.Battlefield;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.*;
import utils.BattlefieldStatus;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.GSON_INSTANCE;

@WebServlet(name = "ContestStartedRefresherServlet", urlPatterns = {"/contestStartedRefresher"})
public class ContestStartedRefresherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String userNameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String battlefieldName;
        if(userManager.getAgent(userNameFromSession) != null) {
            Agent agent = userManager.getAgent(userNameFromSession);
            battlefieldName = agent.getBattlefieldAssignedTo();
        }
        else{
            Ally ally = userManager.getAlly(userNameFromSession);
            battlefieldName = ally.getBattlefieldAssignedTo();
        }
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        Battlefield battlefield = battlefieldManager.getBattlefield(battlefieldName);

        synchronized (this) {
            if(battlefield.getStatus() == BattlefieldStatus.ACTIVE){
                response.getWriter().println(GSON_INSTANCE.toJson(true));
            }
            else{
                response.getWriter().println(GSON_INSTANCE.toJson(false));
            }
        }
    }
}
